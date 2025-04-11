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
            <h3>新对话</h3>
            <el-button type="text" @click="handleLogout">退出</el-button>
          </div>
        </el-header>
        
        <el-main class="chat-main">
          <div class="message-container">
            <div v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
              <div class="message-content">
                <div class="message-text">{{ message.content }}</div>
              </div>
            </div>
          </div>
          
          <div class="input-container">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="3"
              placeholder="输入消息..."
              @keyup.enter.ctrl="sendMessage"
            />
            <el-button type="primary" @click="sendMessage" :loading="sending">发送</el-button>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Setting } from '@element-plus/icons-vue'

const router = useRouter()
const inputMessage = ref('')
const sending = ref(false)
const messages = ref([
  { role: 'assistant', content: '你好！我是ChatAI助手，有什么我可以帮你的吗？' }
])

const sendMessage = async () => {
  if (!inputMessage.value.trim()) return
  
  const userMessage = inputMessage.value
  messages.value.push({ role: 'user', content: userMessage })
  inputMessage.value = ''
  
  sending.value = true
  // TODO: 调用后端API
  setTimeout(() => {
    messages.value.push({ role: 'assistant', content: '这是一个模拟回复。' })
    sending.value = false
  }, 1000)
}

const handleLogout = () => {
  router.push('/login')
  ElMessage.success('已退出登录')
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
}

.user .message-content {
  background-color: #409eff;
  color: white;
}

.assistant .message-content {
  background-color: white;
  border: 1px solid #e4e7ed;
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