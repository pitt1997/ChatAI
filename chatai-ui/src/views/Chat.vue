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
                    <img :src="`/src/assets/svg/${model.icon}.svg`" :alt="model.label" class="model-icon" />
                    <span>{{ model.label }}</span>
                  </span>
                </el-option>
              </el-select>
            </div>
            <div class="user-actions">
              <template v-if="authStore.isAuthenticated">
                <el-dropdown trigger="click" @command="handleUserCommand">
                  <span class="user-dropdown">
                    <el-avatar :size="32" :src="userAvatar" />
                    <span class="username">{{ username }}</span>
                    <el-icon><CaretBottom /></el-icon>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="profile">
                        <el-icon><User /></el-icon>
                        个人信息
                      </el-dropdown-item>
                      <el-dropdown-item command="settings">
                        <el-icon><Setting /></el-icon>
                        设置
                      </el-dropdown-item>
                      <el-dropdown-item divided command="logout">
                        <el-icon><SwitchButton /></el-icon>
                        退出登录
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template v-else>
                <el-button type="primary" @click="handleLogin">登录</el-button>
              </template>
            </div>
          </div>
        </el-header>

        <el-main class="chat-main">
          <section class="message-container" ref="messagesContainer">
            <article v-for="(message, index) in messages" :key="index" :class="['message', message.role]">
              <div class="message-content">
                <!-- 内容区域 -->
                <div class="message-text" v-if="message.role === 'user'">
                  {{ message.content }}
                </div>

                <!-- 显示 Markdown 内容 -->
                <div class="message-text markdown-body" v-else v-html="renderMarkdown(message.content)"></div>

                <!-- 模型标签，仅 AI 显示 -->
                <div v-if="message.role === 'assistant'" class="model-tag">
                  {{ getModelLabel(message.modelType) }}
                </div>
              </div>
            </article>
          </section>

          <div class="input-container">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :rows="3"
              placeholder="输入消息，按 Enter 发送，Shift+Enter 换行"
              @keydown.enter.prevent="handleEnterKey"
            />
            <el-button type="primary" @click="sendMessage" :loading="isSending">发送</el-button>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 个人信息浮窗 -->
    <el-dialog
      v-model="profileDialogVisible"
      title="个人信息"
      width="500px"
      :close-on-click-modal="false"
      class="custom-dialog"
    >
      <div class="profile-content">
        <div class="profile-header">
          <el-avatar :size="64" :src="userAvatar" class="profile-avatar">
            <el-icon><User /></el-icon>
          </el-avatar>
          <div class="profile-info">
            <h3 class="profile-name">{{ profileForm.nickname || profileForm.name }}</h3>
            <p class="profile-email">{{ profileForm.email }}</p>
          </div>
        </div>
        <div class="profile-details">
          <div class="detail-item">
            <span class="detail-label">用户名</span>
            <span class="detail-value">{{ profileForm.name }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">昵称</span>
            <span class="detail-value">{{ profileForm.nickname }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">中文名</span>
            <span class="detail-value">{{ profileForm.cnName }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">手机号</span>
            <span class="detail-value">{{ profileForm.mobile }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">邮箱</span>
            <span class="detail-value">{{ profileForm.email }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">性别</span>
            <span class="detail-value">{{ profileForm.gender === 1 ? '男' : profileForm.gender === 2 ? '女' : '未知' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">组织机构</span>
            <span class="detail-value">{{ profileForm.organizationId }}</span>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 设置浮窗 -->
    <el-dialog
      v-model="settingsDialogVisible"
      title="设置"
      width="500px"
      :close-on-click-modal="false"
      class="custom-dialog"
    >
      <div class="settings-content">
        <div class="settings-section">
          <h3 class="section-title">外观</h3>
          <el-form :model="settingsForm" label-width="0">
            <el-form-item>
              <el-radio-group v-model="settingsForm.theme" class="theme-radio-group">
                <el-radio-button label="light">
                  <el-icon><Sunny /></el-icon>
                  <span>浅色</span>
                </el-radio-button>
                <el-radio-button label="dark">
                  <el-icon><Moon /></el-icon>
                  <span>深色</span>
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </div>

        <div class="settings-section">
          <h3 class="section-title">功能</h3>
          <el-form :model="settingsForm" label-width="0">
            <el-form-item>
              <div class="switch-item">
                <div class="switch-label">
                  <span>消息通知</span>
                  <span class="switch-description">接收新消息通知</span>
                </div>
                <el-switch v-model="settingsForm.notification" />
              </div>
            </el-form-item>
            <el-form-item>
              <div class="switch-item">
                <div class="switch-label">
                  <span>自动滚动</span>
                  <span class="switch-description">新消息自动滚动到底部</span>
                </div>
                <el-switch v-model="settingsForm.autoScroll" />
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="settingsDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSettings">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ChatDotRound,
  Setting,
  User,
  CaretBottom,
  SwitchButton,
  Sunny,
  Moon
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { createWebSocketService } from '@/services/websocket'
import { onBeforeRouteLeave } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const wsService = createWebSocketService('/web/api/ai/chat/websocket')
const messages = ref<Array<{ role: string; content: string; modelType?: string; isReasoning?: boolean }>>([]);
const inputMessage = ref('')
const isSending = ref(false)
const messagesContainer = ref<HTMLElement | null>(null)

// 当前推理中的消息
const currentReasoningMessage = ref<{ role: string; content: string; modelType?: string; isReasoning?: boolean } | null>(null);

// 用户信息
const username = ref('用户')
const userAvatar = ref('')

// 模型选择
const models = [
  { label: 'DeepSeek-R1', value: 'deepseek-r1', icon: 'deepseek' },
  { label: 'DeepSeek-V3', value: 'deepseek-v3', icon: 'deepseek' },
  { label: '本地大模型', value: 'local', icon: 'deepseek' },
  { label: 'GPT-4o', value: 'gpt4o', icon: 'openai' },
  { label: 'Gemini', value: 'gemini', icon: 'google' },
  { label: '文心一言', value: 'yiyan', icon: 'baidu' },
  // { label: 'Claude', value: 'claude', icon: 'claude' },
  { label: '通义千问', value: 'qwen', icon: 'qianwen' },
  { label: '豆包', value: 'doubao', icon: 'doubao' }
]
const selectedModel = ref(models[0].value)

const getModelLabel = (modelType?: string) => {
  const model = models.find(m => m.value === modelType)
  return model ? model.label : '未知模型'
}

const handleModelChange = (value: string) => {
  console.log('Model changed to:', value)
  selectedModel.value = value
}

const handleLogin = () => {
  router.push('/login')
}

// 个人信息相关
const profileDialogVisible = ref(false)
const profileForm = ref({
  name: '',
  nickname: '',
  cnName: '',
  organizationId: '',
  mobile: '',
  email: '',
  gender: 0
})

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const userInfo = await authStore.fetchUserInfo()
    if (userInfo) {
      profileForm.value = userInfo
      username.value = userInfo.nickname || userInfo.name
    }
  } catch (error) {
    console.error('Error fetching user info:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 设置相关
const settingsDialogVisible = ref(false)
const settingsForm = ref({
  theme: 'light',
  notification: true,
  autoScroll: true
})

const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      fetchUserInfo()
      profileDialogVisible.value = true
      break
    case 'settings':
      settingsDialogVisible.value = true
      break
    case 'logout':
      handleLogout()
      break
  }
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

    // 当前正在生成的 AI message（包含思考+回答）
    let currentMessage = ref(null);

    wsService.ws.onmessage = (event) => {
      console.log('WebSocket message received:', event.data);
      try {
        const data = event.data;
        let parsed;
        try {
          parsed = JSON.parse(data);
        } catch (e) {
          console.error('解析服务器消息失败:', data);
          return;
        }

        const type = parsed.type;
        const content = parsed.data;

        const currentModelType = selectedModel.value; // 当前选中的模型
        const isReasoning = isReasoningModel(currentModelType);

        if (isReasoning) {
          if (type === "REASONING") {
            if (!currentMessage.value) {
              // 第一次推理，新建 assistant message
              const newMessage = {
                role: 'assistant',
                content: content,
                modelType: currentModelType,
                isReasoning: true,
              };
              messages.value.push(newMessage);
              currentMessage.value = newMessage;
            } else {
              // 正在推理中，继续追加思考内容
              currentMessage.value.content += content;
            }
            scrollToBottom();

          } else if (type === "IDLE") {
            // 思考结束，不新建message，直接在当前message追加
            if (currentMessage.value) {
              currentMessage.value.content += `\n\n[思考结束，生成回答中...]\n\n`;
              currentMessage.value.isReasoning = false; // 标记推理结束
            }
            scrollToBottom();
          } else if (type === "CONTENT") {
            if (currentMessage.value) {
              // 继续在当前message中追加正式回答内容
              currentMessage.value.content += content;
            } else {
              // 极少情况，CONTENT先到了，保险措施
              const newMessage = {
                role: 'assistant',
                content: content,
                modelType: currentModelType,
              };
              messages.value.push(newMessage);
              currentMessage.value = newMessage;
            }
            scrollToBottom();
          } else if (type === "DONE") {
            // 回答完成，清空 currentMessage
            currentMessage.value = null;
          }
        } else {
          // 非推理型模型的处理逻辑（只有 CONTENT 和 DONE）
          if (type === "CONTENT") {
            if (!currentMessage.value) {
              const newMessage = {
                role: 'assistant',
                content: content,
                modelType: currentModelType,
              };
              messages.value.push(newMessage);
              currentMessage.value = newMessage;
            } else {
              currentMessage.value.content += content;
            }
            scrollToBottom();
          } else if (type === "DONE") {
            currentMessage.value = null;
          }
        }
      } catch (error) {
        console.error('Error handling WebSocket message:', error);
      }
    };

  } else {
    console.log('User is not authenticated, WebSocket connection skipped')
  }

  // 初始化设置
  const savedSettings = localStorage.getItem('settings')
  if (savedSettings) {
    settingsForm.value = JSON.parse(savedSettings)
  }
})

// 判断当前模型是否是推理模型
function isReasoningModel(modelType) {
  // 这里维护一个推理型模型的列表
  const reasoningModels = ['deepseek-r1', '你的其他推理模型'];
  return reasoningModels.includes(modelType);
}


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
    // 如果按住了 Shift，就让 textarea 正常换行
    const textarea = event.target as HTMLTextAreaElement;
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    textarea.value =
        textarea.value.substring(0, start) +
        '\n' +
        textarea.value.substring(end);
    textarea.selectionStart = textarea.selectionEnd = start + 1;
  } else {
    // 直接发送消息
    sendMessage();
  }
}

const saveSettings = () => {
  // 保存设置到本地存储
  localStorage.setItem('settings', JSON.stringify(settingsForm.value))
  ElMessage.success('设置已保存')
  settingsDialogVisible.value = false
}

import { marked } from 'marked'
import hljs from 'highlight.js'
import 'github-markdown-css/github-markdown-light.css'; // 浅色主题
import 'highlight.js/styles/github-dark.css'; // 或 github.css

onMounted(() => {
  nextTick(() => {
    document.querySelectorAll('pre code').forEach((el) => {
      hljs.highlightElement(el as HTMLElement)
    })
  })
})

function renderMarkdown(text: string) {
  return marked.parse(text || '')
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

.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #606266;
}

.el-dropdown-menu {
  padding: 4px 0;
}

.el-dropdown-menu__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.el-dropdown-menu__item .el-icon {
  font-size: 16px;
}

.custom-dialog :deep(.el-dialog__header) {
  margin: 0;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.custom-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.profile-content {
  padding: 0;
}

.profile-header {
  display: flex;
  align-items: center;
  padding: 24px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.profile-avatar {
  margin-right: 16px;
  background-color: #409eff;
  color: white;
}

.profile-avatar .el-icon {
  font-size: 32px;
}

.profile-info {
  flex: 1;
}

.profile-name {
  margin: 0 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.profile-email {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.profile-details {
  padding: 24px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-label {
  color: #606266;
  font-size: 14px;
}

.detail-value {
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

.settings-content {
  padding: 0;
}

.settings-section {
  padding: 24px;
  border-bottom: 1px solid #e4e7ed;
}

.settings-section:last-child {
  border-bottom: none;
}

.section-title {
  margin: 0 0 16px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.theme-radio-group {
  display: flex;
  gap: 8px;
}

.theme-radio-group :deep(.el-radio-button__inner) {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 12px 16px;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.switch-label {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.switch-description {
  font-size: 12px;
  color: #909399;
}

.dialog-footer {
  padding: 16px 24px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

</style>