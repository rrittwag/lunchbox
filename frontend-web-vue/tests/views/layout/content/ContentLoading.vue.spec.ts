import { render } from '@testing-library/vue'
import ContentLoading from '@/views/layout/content/ContentLoading.vue'

it('renders loading', () => {
  const { getByRole } = render(ContentLoading)

  expect(getByRole('heading', { level: 1 })).toHaveTextContent('Loading')
})
