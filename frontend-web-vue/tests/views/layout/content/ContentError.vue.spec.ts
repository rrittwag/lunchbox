import ContentError from '@/views/layout/content/ContentError.vue'
import { render } from '@testing-library/vue'

describe('ContentError', () => {
  it('renders', () => {
    const { getByRole } = render(ContentError)

    expect(getByRole('heading', { level: 1 })).toHaveTextContent('Error')
  })
})
