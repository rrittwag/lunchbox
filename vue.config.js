module.exports = {
  transpileDependencies: ['vuex-module-decorators'],
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://lunchbox.rori.info'
      }
    }
  }
}
