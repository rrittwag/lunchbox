module.exports = {
  preset: '@vue/cli-plugin-unit-jest/presets/typescript-and-babel',
  transform: {
    '^.+\\.vue$': 'vue-jest',
    '^.+\\.svg$': '<rootDir>/tests/jest.vue-svg-loader',
  },
  moduleNameMapper: {
    '^/@/(.*)$': '<rootDir>/src/$1',
    '^/@tests/(.*)$': '<rootDir>/tests/$1',
  },
  setupFilesAfterEnv: ['jest-extended'],
}
