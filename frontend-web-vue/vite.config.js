import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  plugins: [vue(), svgLoader()],
  server: {
    open: true,
    proxy: {
      '/api': {
        target: 'https://lunchbox.rori.info',
        // target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
