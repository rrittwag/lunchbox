import { render } from '@testing-library/vue'
import { createTestingPinia } from '@tests/test-utils'
import router from '@/router'
import Layout from '@/views/Layout.vue'

it('renders', () => {
  const pinia = createTestingPinia()

  const { getByRole } = render(Layout, {
    global: { plugins: [pinia, router] },
  })

  expect(getByRole('banner')).toBeInTheDocument()
  expect(getByRole('main')).toBeInTheDocument()
})
