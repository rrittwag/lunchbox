import Header from '/@/views/layout/Header.vue'
import RouterLinkIcon from '/@/views/layout/header/NavLink.vue'
import { mountUnit } from '/@tests/unit/test-util'

describe('Header', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Header)

    expect(wrapper.element).toMatchSnapshot()
  })

  test('has all nav items', () => {
    const wrapper = mountUnit(Header)

    const navitems = wrapper.findAll(RouterLinkIcon)
    expect(navitems.at(0).props('to')).toContain('/')
    expect(navitems.at(1).props('to')).toContain('/settings')
    expect(navitems.at(2).props('to')).toContain('/about')
  })
})
