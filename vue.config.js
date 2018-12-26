module.exports = {
  transpileDependencies: ['vuex-module-decorators'],
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
//        target: 'https://lunchbox.rori.info'
        target: 'http://localhost:8080'
      }
    }
  }
}
