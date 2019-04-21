// workaround: jest does not support vue-svg-loader naturally
// https://github.com/visualfanatic/vue-svg-loader/issues/38#issuecomment-407657015

const vueJest = require('vue-jest/lib/template-compiler');

module.exports = {
  process(content) {
    const { render } = vueJest({
      content,
      attrs: {
        functional: false,
      },
    });

    return `module.exports = { render: ${render} }`;
  },
};
