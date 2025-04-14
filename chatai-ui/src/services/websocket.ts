import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

class WebSocketService {
  private ws: WebSocket | null = null
  private url: string
  private reconnectAttempts = 0
  private maxReconnectAttempts = 5
  private reconnectTimeout = 5000 // 5秒重连间隔
  private messageQueue: string[] = []
  public messages = ref<Array<{ role: string; content: string }>>([])

  constructor(url: string) {
    this.url = url
  }

  connect() {
    try {
      const authStore = useAuthStore()
      const token = authStore.token
      
      // 检查是否已认证
      if (!authStore.isAuthenticated || !token) {
        console.log('Not authenticated, skipping WebSocket connection')
        return
      }
      
      console.log('Connecting to WebSocket with token:', token)
      
      // 构建带token的WebSocket URL
      const wsUrl = this.url.includes('?') 
        ? `${this.url}&WebSocket-Authorization=${token}`
        : `${this.url}?WebSocket-Authorization=${token}`
      
      console.log('WebSocket URL:', wsUrl)
      
      // 如果已经连接，先断开
      if (this.ws?.readyState === WebSocket.OPEN) {
        this.disconnect()
      }
      
      // 创建WebSocket连接，第二个参数是协议数组
      this.ws = new WebSocket(wsUrl, [])
      
      this.ws.onopen = () => {
        console.log('WebSocket connected successfully')
        this.reconnectAttempts = 0
        this.flushMessageQueue()
      }
      
      this.ws.onmessage = (event) => {
        console.log('WebSocket message received:', event.data)
        try {
          // 尝试解析JSON，如果失败则直接使用文本内容
          let content = event.data
          try {
            const data = JSON.parse(event.data)
            content = data.content || data.message || event.data
          } catch (e) {
            // 如果不是JSON，直接使用文本内容
            content = event.data
          }
          
          // 检查是否已经有助手的最新消息
          const lastMessage = this.messages.value[this.messages.value.length - 1]
          if (lastMessage && lastMessage.role === 'assistant') {
            // 如果是助手的最新消息，追加内容
            lastMessage.content += content
          } else {
            // 否则创建新消息
            this.messages.value.push({
              role: 'assistant',
              content: content
            })
          }
        } catch (error) {
          console.error('Error handling WebSocket message:', error)
        }
      }
      
      this.ws.onclose = (event) => {
        console.log('WebSocket closed:', {
          code: event.code,
          reason: event.reason,
          wasClean: event.wasClean
        })
        this.handleReconnect()
      }
      
      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error)
        ElMessage.error('WebSocket连接错误，请检查网络连接')
      }
    } catch (error) {
      console.error('WebSocket connection error:', error)
      ElMessage.error('WebSocket连接失败，请检查配置')
    }
  }

  private handleReconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      console.log(`Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
      setTimeout(() => this.connect(), this.reconnectTimeout)
    } else {
      console.error('Max reconnection attempts reached')
      ElMessage.error('WebSocket连接失败，请刷新页面重试')
    }
  }

  private flushMessageQueue() {
    while (this.messageQueue.length > 0) {
      const message = this.messageQueue.shift()
      if (message) {
        this.send(message)
      }
    }
  }

  send(message: string) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      console.log('Sending WebSocket message:', message)
      // 直接发送消息，不需要再次JSON序列化
      this.ws.send(message)
    } else {
      console.log('WebSocket not ready, queueing message')
      this.messageQueue.push(message)
      if (this.ws?.readyState === WebSocket.CLOSED) {
        this.handleReconnect()
      }
    }
  }

  disconnect() {
    if (this.ws) {
      console.log('Disconnecting WebSocket')
      this.ws.close()
      this.ws = null
    }
  }
}

export function createWebSocketService(url: string) {
  // 获取环境变量中的API基础URL
  const baseUrl = import.meta.env.VITE_API_BASE_URL.replace('http', 'ws')
  
  // 将相对路径转换为WebSocket URL
  const wsUrl = url.startsWith('/') 
    ? `${baseUrl}${url}`
    : url
  
  console.log('Creating WebSocket service with URL:', wsUrl)
  return new WebSocketService(wsUrl)
} 