import antfu from '@antfu/eslint-config'

export default antfu({}, {
  files: ['**/*.spec.ts'],
  rules: {
    'ts/no-use-before-define': 'off',
  },
})
