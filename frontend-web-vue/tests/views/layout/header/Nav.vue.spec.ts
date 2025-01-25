import Nav from '@/views/layout/header/Nav.vue'
import { render, within } from '@testing-library/vue'

it('renders', () => {
  const { getByRole } = render(Nav, {
    slots: { default: '<li><a href="/">link1</a></li>  <li><a href="/">link2</a></li>' },
  })

  const nav = getByRole('navigation', { name: 'Haupt' })
  const list = within(nav).getByRole('list')
  const navItems = within(list).getAllByRole('listitem')
  expect(navItems).toHaveLength(2)
  expect(navItems[0]).toHaveTextContent('link1')
  expect(navItems[1]).toHaveTextContent('link2')
})
