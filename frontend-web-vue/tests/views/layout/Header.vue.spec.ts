import router from '@/router'
import Header from '@/views/layout/Header.vue'
import { render, within } from '@testing-library/vue'

describe('header', () => {
  beforeEach(async () => {
    await router.push('/')
  })

  it('renders page header and nav', () => {
    const { getByRole } = render(Header, {
      global: {
        plugins: [router],
      },
    })

    const banner = getByRole('banner')
    expect(banner).toBeInTheDocument()
    within(banner).getByRole('navigation', { name: 'Haupt' })
  })

  it('renders nav links', () => {
    const { getByRole } = render(Header, {
      global: {
        plugins: [router],
      },
    })

    const nav = getByRole('navigation', { name: 'Haupt' })
    const list = within(nav).getByRole('list')
    const navitems = within(list).getAllByRole('listitem')
    expect(navitems).toHaveLength(3)
    expect(within(navitems[0]).getByRole('link', { current: 'page' })).toHaveAccessibleName('Mittagsangebote')
    expect(within(navitems[1]).getByRole('link', { current: false })).toHaveAccessibleName('Einstellungen')
    expect(within(navitems[2]).getByRole('link', { current: false })).toHaveAccessibleName('Info')
  })
})
