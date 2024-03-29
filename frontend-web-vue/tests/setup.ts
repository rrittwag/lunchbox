import { afterEach } from 'vitest'
import { cleanup } from '@testing-library/vue'

// extends Vitest's expect method with methods from jest-dom
import '@testing-library/jest-dom/vitest'

// Testing Library uses CSS to determine visible elements
import '@/assets/style/index.scss'

// runs a cleanup after each test case (e.g. clearing jsdom)
afterEach(() => {
  cleanup()
})
