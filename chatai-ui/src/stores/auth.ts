import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export const useAuthStore = defineStore('auth', () => {
  const token = ref('')
  const isAuthenticated = ref(false)

  // 新增方法
  const initialize = () => {
    checkAuth()
  }

  const login = async (username: string, password: string) => {
    try {
      console.log('Login request:', { username, password })
      console.log('Request URL:', '/api/web/login')
      
      // 创建 AbortController 用于超时控制
      const controller = new AbortController()
      const timeoutId = setTimeout(() => controller.abort(), 60000) // 60秒超时
      
      const response = await fetch('/api/web/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password }),
        signal: controller.signal
      }).finally(() => clearTimeout(timeoutId))
      
      console.log('Login response status:', response.status)
      const data = await response.json()
      console.log('Login response data:', data)
      
      // 适配后端返回格式
      if (data.code === 0) {
        token.value = data.data
        isAuthenticated.value = true
        localStorage.setItem('token', data.data)
        ElMessage.success(data.message || '登录成功')
        return true
      } else {
        ElMessage.error(data.message || '登录失败')
        return false
      }
    } catch (error: any) {
      console.error('Login error details:', error)
      if (error.name === 'AbortError') {
        ElMessage.error('请求超时，请检查网络连接或后端服务状态')
      } else {
        ElMessage.error('登录失败，请检查后端服务是否正常运行')
      }
      return false
    }
  }

  const logout = async () => {
    try {
      const response = await fetch('/api/web/logout', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token.value}`
        }
      })
      
      const data = await response.json()
      if (data.code === 0) {
        token.value = ''
        isAuthenticated.value = false
        localStorage.removeItem('token')
        ElMessage.success(data.message || '退出成功')
      } else {
        ElMessage.error(data.message || '退出失败')
      }
    } catch (error) {
      console.error('Logout error:', error)
      ElMessage.error('退出失败，请稍后重试')
    } finally {
      // 确保清除本地状态
      token.value = ''
      isAuthenticated.value = false
      localStorage.removeItem('token')
    }
  }

  const checkAuth = () => {
    const storedToken = localStorage.getItem('token')
    if (storedToken) {
      token.value = storedToken
      isAuthenticated.value = true
    }
    return isAuthenticated.value
  }

  const userInfo = ref<any>(null) // 可根据需要定义更具体的类型

  const fetchUserInfo = async () => {
    try {
      const response = await fetch('/api/web/user/current', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token.value}`
        }
      })

      const data = await response.json()
      console.log('User info response:', data)

      if (data.code === 0) {
        userInfo.value = data.data
        return data.data
      } else {
        ElMessage.error(data.message || '获取用户信息失败')
        return null
      }
    } catch (error) {
      console.error('User info error:', error)
      ElMessage.error('获取用户信息异常，请检查网络或服务状态')
      return null
    }
  }

  // 初始化
  initialize()

  return {
    token,
    isAuthenticated,
    userInfo,
    login,
    logout,
    checkAuth,
    fetchUserInfo
  }

}) 