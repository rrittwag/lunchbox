import { render } from '@testing-library/vue'
import About from '@/views/About.vue'

it('renders', () => {
  const { getByRole } = render(About)

  expect(getByRole('heading', { level: 1 })).toHaveTextContent(/Mittagspause/)
  expect(getByRole('link', { name: /Spiel mit/ })).toBeDefined()
})
