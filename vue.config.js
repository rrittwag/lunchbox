module.exports = {
  transpileDependencies: ['vuex-module-decorators'],
  devServer: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://lunchbox.rori.info'
      }
    }
  }
}
