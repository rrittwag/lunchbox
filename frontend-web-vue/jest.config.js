module.exports = {
  preset: '@vue/cli-plugin-unit-jest/presets/typescript-and-babel',
  transform: {
    '^.+\\.svg$': '<rootDir>/tests/jest.vue-svg-loader',
  },
  moduleNameMapper: {
    '^@tests/(.*)$': '<rootDir>/tests/$1',
  },
  setupFilesAfterEnv: ['jest-extended'],
}
