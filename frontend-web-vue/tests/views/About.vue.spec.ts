import About from '@/views/About.vue'
import { render } from '@testing-library/vue'

it('renders', () => {
  const { getByRole } = render(About)

  expect(getByRole('heading', { level: 1 })).toHaveTextContent('Info')
  expect(getByRole('link', { name: 'Github' })).toBeDefined()
})
