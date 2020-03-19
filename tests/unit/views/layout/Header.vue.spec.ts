import Header from '@/views/layout/Header.vue'
import RouterLinkIcon from '@/views/layout/header/NavLink.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('Header', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Header)

    expect(wrapper.element).toMatchSnapshot()
  })

  test('has nav items in collapse', () => {
    const wrapper = mountUnit(Header)

    const navitems = wrapper.findAll(RouterLinkIcon)
    expect(navitems.at(0).props('title')).toContain('Mittagsangebote')
    expect(navitems.at(1).props('title')).toContain('Einstellungen')
    expect(navitems.at(2).props('title')).toContain('Info')
  })
})
