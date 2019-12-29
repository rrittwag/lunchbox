module.exports = {
  chainWebpack: config => {
    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule.use('vue-svg-loader').loader('vue-svg-loader')
  },
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'https://lunchbox.rori.info',
        //        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  pwa: {
    workboxOptions: {
      importWorkboxFrom: 'local', // provide workbox lib locally, not by CDN
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
  },
}
