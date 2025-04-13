import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())
  
  // 打印环境变量
  console.log('Current mode:', mode)
  console.log('API Base URL:', env.VITE_API_BASE_URL)
  
  return {
    plugins: [
      vue(),
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
      },
    },
    server: {
      port: 3000,
      proxy: {
        '/api': {
          target: env.VITE_API_BASE_URL,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, ''),
          ws: true, // 启用WebSocket代理
          configure: (proxy, options) => {
            proxy.on('error', (err, req, res) => {
              console.log('proxy error', err);
              console.log('target URL:', env.VITE_API_BASE_URL);
              res.writeHead(500, {
                'Content-Type': 'application/json',
              });
              res.end(JSON.stringify({
                success: false,
                message: '后端服务未启动，请检查后端服务是否正常运行'
              }));
            });
            
            // 添加WebSocket连接日志
            proxy.on('open', (ws) => {
              console.log('WebSocket proxy opened');
            });
            
            proxy.on('close', (ws) => {
              console.log('WebSocket proxy closed');
            });
          }
        },
      },
    },
  }
}) 