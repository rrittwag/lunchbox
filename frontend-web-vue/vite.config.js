import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import svgLoader from 'vite-svg-loader'
import { VitePWA } from 'vite-plugin-pwa'

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: [{ find: '@', replacement: '/src' }],
  },
  plugins: [
    vue(),
    svgLoader(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.svg', 'robots.txt', 'safari-pinned-tab.svg', 'apple-touch-icon.png'],
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
      workbox: {
        runtimeCaching: [
          {
            // cache Google Fonts
            urlPattern: '/^https://fonts.(?:googleapis|gstatic).com//',
            handler: 'CacheFirst',
            options: {
              cacheName: 'google-fonts',
              expiration: { maxAgeSeconds: 30 * 24 * 60 * 60, maxEntries: 30 },
              cacheableResponse: { statuses: [0, 200] },
            },
          },
          {
            // cache images
            urlPattern: '/.(?:png|gif|jpg|jpeg|webp|svg)$/',
            handler: 'CacheFirst',
            options: {
              cacheName: 'images',
              expiration: { maxAgeSeconds: 30 * 24 * 60 * 60, maxEntries: 60 },
              cacheableResponse: { statuses: [0, 200] },
            },
          },
          {
            // cache API calls
            urlPattern: /api/,
            handler: 'NetworkFirst',
            options: {
              networkTimeoutSeconds: 10,
              cacheName: 'api-data',
              expiration: { maxEntries: 5 },
              cacheableResponse: { statuses: [0, 200] },
            },
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
