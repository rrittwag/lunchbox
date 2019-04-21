module.exports = {
  transpileDependencies: [
    /\bvue-awesome\b/,
  ],
  chainWebpack: config => {
    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule
      .use('vue-svg-loader')
      .loader('vue-svg-loader')
  },
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'https://lunchbox.rori.info',
//        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
