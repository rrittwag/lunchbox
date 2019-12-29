module.exports = {
  preset: '@vue/cli-plugin-unit-jest/presets/typescript-and-babel',
  moduleNameMapper: {
    '^@tests/(.*)$': '<rootDir>/tests/$1',
  },
  setupFilesAfterEnv: ['jest-extended'],
}
