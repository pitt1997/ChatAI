<template>
  <div class="chat-container">
    <el-container>
      <el-aside width="250px" class="sidebar">
        <div class="sidebar-header">
          <h3>ChatAI</h3>
        </div>
        <div class="sidebar-content">
          <el-menu default-active="1" class="el-menu-vertical">
            <el-menu-item index="1">
              <el-icon><ChatDotRound /></el-icon>
              <span>新对话</span>
            </el-menu-item>
            <el-menu-item index="2">
              <el-icon><Setting /></el-icon>
              <span>设置</span>
            </el-menu-item>
          </el-menu>
        </div>
      </el-aside>
      
      <el-container>
        <el-header class="chat-header">
          <div class="header-content">
            <div class="model-selector">
              <el-select 
                v-model="selectedModel" 
                placeholder="选择模型" 
                @change="handleModelChange"
                class="model-select"
              >
                <el-option
                  v-for="model in models"
                  :key="model.value"
                  :label="model.label"
                  :value="model.value"
                >
                  <span class="model-option">
                    <img :src="`/src/assets/icons/${model.icon}.svg`" :alt="model.label" class="model-icon" />
                    <span>{{ model.label }}</span>
                  </span>
                </el-option>
              </el-select>
            </div>
            <el-button type="text" @click="handleLogout">退出</el-button>
          </div>
        </el-header>
        
        <el-main class="chat-main">
          <div class="message-container" ref="messagesContainer">
            <div v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
              <div class="message-content">
                <div class="message-text">{{ message.content }}</div>
                <div v-if="message.role === 'assistant'" class="model-tag">
                  {{ getModelLabel(message.modelType) }}
                </div>
              </div>
            </div>
          </div>
          
          <div class="input-container">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="3"
              placeholder="输入消息，按回车键发送..."
              @keydown.enter.prevent="handleEnterKey"
            />
            <el-button type="primary" @click="sendMessage" :loading="isSending">发送</el-button>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Setting, Aim, ChatDotSquare, User, MagicStick, Food } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { createWebSocketService } from '@/services/websocket'
import { onBeforeRouteLeave } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const inputMessage = ref('')
const isSending = ref(false)
const messages = ref<Array<{ role: string; content: string; modelType?: string }>>([])
const messagesContainer = ref<HTMLElement | null>(null)

// 模型选择
const models = [
  { label: 'DeepSeek', value: 'deepseek', icon: 'deepseek' },
  { label: 'GPT-4', value: 'gpt4', icon: 'gpt4' },
  { label: 'Claude', value: 'claude', icon: 'claude' },
  { label: 'Qwen', value: 'qwen', icon: 'qwen' },
  { label: '豆包', value: 'doubao', icon: 'doubao' }
]
const selectedModel = ref(models[0].value)

// WebSocket服务
const wsService = createWebSocketService('/web/api/ai/chat/websocket')

const getModelLabel = (modelType?: string) => {
  const model = models.find(m => m.value === modelType)
  return model ? model.label : '未知模型'
}

const handleModelChange = (value: string) => {
  console.log('Model changed to:', value)
  selectedModel.value = value
  // 可以在这里添加模型切换时的其他逻辑
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

onMounted(() => {
  // 如果已登录，则连接WebSocket
  if (authStore.isAuthenticated) {
    console.log('User is authenticated, connecting to WebSocket...')
    wsService.connect()
    wsService.messages.value = messages.value
    
    // 添加WebSocket消息处理
    wsService.ws.onmessage = (event) => {
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
        const lastMessage = messages.value[messages.value.length - 1]
        if (lastMessage && lastMessage.role === 'assistant') {
          // 如果是助手的最新消息，追加内容
          lastMessage.content += content
        } else {
          // 否则创建新消息，包含当前选择的模型类型
          messages.value.push({
            role: 'assistant',
            content: content,
            modelType: selectedModel.value
          })
        }
        
        scrollToBottom()
      } catch (error) {
        console.error('Error handling WebSocket message:', error)
      }
    }
  } else {
    console.log('User is not authenticated, WebSocket connection skipped')
  }
})

onUnmounted(() => {
  console.log('Component unmounted, disconnecting WebSocket')
  wsService.disconnect()
})

const sendMessage = async () => {
  if (!inputMessage.value.trim()) {
    ElMessage.warning('请输入消息')
    return
  }

  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    return
  }

  isSending.value = true
  try {
    const message = {
      message: inputMessage.value,
      modelType: selectedModel.value
    }
    
    // 添加用户消息，包含模型类型
    messages.value.push({
      role: 'user',
      content: inputMessage.value,
      modelType: selectedModel.value
    })
    
    await scrollToBottom()
    inputMessage.value = ''
    
    // 发送消息到WebSocket
    wsService.send(JSON.stringify(message))
  } catch (error) {
    console.error('Error sending message:', error)
    ElMessage.error('发送消息失败')
  } finally {
    isSending.value = false
  }
}

const handleLogout = async () => {
  // 先断开WebSocket连接
  wsService.disconnect()
  // 调用登出接口
  await authStore.logout()
  // 跳转到登录页面
  router.push('/login')
}

// 监听路由变化，当离开聊天页面时断开WebSocket连接
onBeforeRouteLeave((to, from, next) => {
  if (to.path !== '/chat') {
    wsService.disconnect()
  }
  next()
})

const handleEnterKey = (event: KeyboardEvent) => {
  // 如果按下 Shift + Enter，允许换行
  if (event.shiftKey) {
    return
  }
  // 否则发送消息
  sendMessage()
}
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.sidebar {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.sidebar-content {
  padding: 10px;
}

.chat-header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.model-selector {
  margin-right: 20px;
  min-width: 150px;
}

.model-select {
  width: 100%;
}

.model-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.model-icon {
  width: 16px;
  height: 16px;
  object-fit: contain;
}

.chat-main {
  display: flex;
  flex-direction: column;
  padding: 20px;
  background-color: #f5f7fa;
}

.message-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  margin-bottom: 20px;
}

.message {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
}

.message.user {
  align-items: flex-end;
}

.message.assistant {
  align-items: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 8px;
  position: relative;
}

.user .message-content {
  background-color: #409eff;
  color: white;
}

.assistant .message-content {
  background-color: white;
  border: 1px solid #e4e7ed;
}

.message-text {
  white-space: pre-wrap;
  word-break: break-word;
}

.model-tag {
  position: absolute;
  bottom: -20px;
  left: 0;
  font-size: 12px;
  color: #666;
}

.input-container {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.input-container .el-button {
  height: auto;
}
</style> 