import { render } from '@testing-library/vue'
import ContentError from '@/views/layout/content/ContentError.vue'

it('renders', () => {
  const { getByRole } = render(ContentError)

  expect(getByRole('heading', { level: 1 })).toHaveTextContent('Error')
})
