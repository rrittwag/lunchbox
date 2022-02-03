import { createTestingPinia as createTestingPiniaOriginal, TestingOptions, TestingPinia } from '@pinia/testing'
import { fn } from 'vitest'

export const createTestingPinia = (options?: TestingOptions): TestingPinia =>
  createTestingPiniaOriginal({ ...options, createSpy: fn })
