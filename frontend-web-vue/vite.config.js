import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Icons from 'unplugin-icons/vite'
import { VitePWA } from 'vite-plugin-pwa'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [
      { find: '@tests', replacement: '/tests' },
      { find: '@', replacement: '/src' },
    ],
  },
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: '/tests/setup.ts',
    css: true,
    passWithNoTests: true,
  },
  plugins: [
    vue(),
    Icons({ compiler: 'vue3' }),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.svg', 'robots.txt', 'safari-pinned-tab.svg', 'apple-touch-icon.png', 'fonts/*.woff2'],
      manifest: {
        name: 'Lunchbox',
        short_name: 'Lunchbox',
        theme_color: '#007755',
        lang: 'de',
        icons: [
          {
            src: '/android-chrome-192x192.png',
            sizes: '192x192',
            type: 'image/png',
          },
          {
            src: '/android-chrome-512x512.png',
            sizes: '512x512',
            type: 'image/png',
          },
          {
            src: '/android-chrome-512x512.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'maskable',
          },
        ],
      },
    }),
  ],
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
