module.exports = {
  moduleFileExtensions: ['vue', 'ts', 'js'],
  testEnvironment: 'jsdom',
  transform: {
    '^.+\\.ts$': 'ts-jest',
    '^.+\\.vue$': 'vue-jest',
    '^.+\\.svg$': '<rootDir>/tests/jest.vue-svg-loader',
  },
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '^@tests/(.*)$': '<rootDir>/tests/$1',
  },
}
