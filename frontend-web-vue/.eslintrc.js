module.exports = {
  root: true,
  env: {
    node: true,
    'vue/setup-compiler-macros': true,
  },
  extends: [
    'plugin:vue/vue3-recommended',
    'eslint:recommended',
    '@vue/typescript/recommended',
    '@vue/prettier',
  ],
  rules: {
    'no-console': 'error',
    'no-debugger': 'error',
    quotes: ['error', 'single'],
    semi: ['error', 'never'],
    'vue/multi-word-component-names': 'off',
    'vue/attribute-hyphenation': ['error', 'never', { ignore: ['custom-prop'] }],
    'vue/v-on-event-hyphenation': ['error', 'never', { ignore: ['custom-event'] }],
  },
}
