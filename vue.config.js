module.exports = {
  transpileDependencies: [
    'vuex-module-decorators',
    /\bvue-awesome\b/
  ],
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
