import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default {
  server: {
    port: 5173,        // 固定 5173
    strictPort: true,  // 被占用就报错，不会自动改 5174
    host: true         // 监听 0.0.0.0（含 localhost）
  }
}

