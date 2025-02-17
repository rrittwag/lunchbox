import ContentLoading from '@/views/layout/content/ContentLoading.vue'
import { render } from '@testing-library/vue'

it('renders loading', () => {
  const { getByRole } = render(ContentLoading)

  expect(getByRole('heading', { level: 1 })).toHaveTextContent('Loading')
})
