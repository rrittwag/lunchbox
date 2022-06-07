import { createTestingPinia as createTestingPiniaOriginal, TestingOptions, TestingPinia } from '@pinia/testing'
import { vi } from 'vitest'

export const createTestingPinia = (options?: TestingOptions): TestingPinia =>
  createTestingPiniaOriginal({ ...options, createSpy: vi.fn })
