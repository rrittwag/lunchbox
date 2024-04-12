import path from 'node:path'
import { fileURLToPath } from 'node:url'

import { FlatCompat } from '@eslint/eslintrc'
import js from '@eslint/js'
import pluginVue from 'eslint-plugin-vue'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
})

export default [
  js.configs.recommended,
  ...pluginVue.configs['flat/recommended'],
  ...compat.extends('@vue/eslint-config-typescript/recommended'),
  ...compat.extends('@vue/eslint-config-prettier'),
  {
    files: ['**/*.vue', '**/*.js', '**/*.ts'],
    languageOptions: {
      ecmaVersion: 'latest',
    },
  },
  {
    rules: {
      'no-console': 'error',
      'no-debugger': 'error',
      'vue/multi-word-component-names': 'off',
      'vue/attribute-hyphenation': ['error', 'never', { ignore: ['custom-prop'] }],
      'vue/v-on-event-hyphenation': ['error', 'never', { ignore: ['custom-event'] }],
    },
  },
]
