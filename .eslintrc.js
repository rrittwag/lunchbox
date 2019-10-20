module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    'eslint:recommended',
    '@vue/typescript'
  ],
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'warn',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'warn',
    'max-len': ['error', { 'code': 100 }],
    'quotes': ['error', 'single'],
    'semi': ['error', 'never'],
    'arrow-parens': ['error', 'as-needed'],
    '@typescript-eslint/no-unused-vars': ['error', { args: 'none' }],
    '@typescript-eslint/no-explicit-any': ['error'],
    '@typescript-eslint/member-delimiter-style': [
      'error',
      {
          'multiline': {
              'delimiter': 'none',
              'requireLast': true
          },
          'singleline': {
              'delimiter': 'semi',
              'requireLast': false
          }
      }
    ],
  },
  parserOptions: {
    parser: '@typescript-eslint/parser'
  },
  overrides: [
    {
      files: ['**/__tests__/*.{j,t}s?(x)', '**/tests/unit/**/*.spec.{j,t}s?(x)'],
      env: {
        jest: true
      }
    }
  ]
}
