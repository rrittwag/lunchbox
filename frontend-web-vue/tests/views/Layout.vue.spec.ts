import Layout from '@/views/Layout.vue'
import { createTestingPinia } from '@tests/test-utils'
import { render } from '@testing-library/vue'
import router from '@/router'

describe('Layout', () => {
  it('renders', () => {
    const pinia = createTestingPinia()

    const { getByRole } = render(Layout, {
      global: { plugins: [pinia, router] },
    })

    expect(getByRole('banner')).toBeInTheDocument()
    expect(getByRole('main')).toBeInTheDocument()
  })
})
