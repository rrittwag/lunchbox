/// <reference types="vitest" />
import tailwindcss from '@tailwindcss/vite'
import vue from '@vitejs/plugin-vue'
import Icons from 'unplugin-icons/vite'
import { defineConfig } from 'vite'
import { VitePWA } from 'vite-plugin-pwa'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [
      { find: '@', replacement: '/src' },
      { find: '@tests', replacement: '/tests' },
    ],
  },
  plugins: [
    vue(),
    tailwindcss(),
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
  test: {
    environment: 'happy-dom',
    globals: true,
    setupFiles: '/tests/setup.ts',
    css: true,
    passWithNoTests: true,
  },
})
