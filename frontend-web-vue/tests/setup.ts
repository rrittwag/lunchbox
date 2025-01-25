import { cleanup } from '@testing-library/vue'
import { afterEach } from 'vitest'

// extends Vitest's expect method with methods from jest-dom
import '@testing-library/jest-dom/vitest'

// Testing Library uses CSS to determine visible elements
import '@/assets/style/index.css'

// runs a cleanup after each test case (e.g. clearing jsdom)
afterEach(() => {
  cleanup()
})
