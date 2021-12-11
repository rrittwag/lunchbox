// workaround: jest does not support vue-svg-loader naturally
// https://github.com/visualfanatic/vue-svg-loader/issues/38#issuecomment-407657015

module.exports = {
  process() {
    // TODO: render SVG file
    return 'module.exports = { render: () => {} }'
  },
}
