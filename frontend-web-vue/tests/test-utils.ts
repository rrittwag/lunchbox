import type { TestingOptions, TestingPinia } from '@pinia/testing'
import { createTestingPinia as createTestingPiniaOriginal } from '@pinia/testing'
import { vi } from 'vitest'

export function createTestingPinia(options?: TestingOptions): TestingPinia {
  return createTestingPiniaOriginal({ ...options, createSpy: vi.fn })
}
