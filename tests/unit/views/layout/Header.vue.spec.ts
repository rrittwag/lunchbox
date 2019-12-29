import Header from '@/views/layout/Header.vue'
import { mountUnit } from '@tests/unit/test-util'

describe('Header', () => {
  test('renders snapshot', () => {
    const wrapper = mountUnit(Header)
    expect(wrapper.element).toMatchSnapshot()
  })
/*
  test('has nav items in collapse', () => {
    const wrapper = mountUnit(Header)

    const collapse = wrapper.find(BCollapse)
    expect(collapse.exists()).toBeTrue()
    const navitems = collapse.findAll(BNavItem)
    expect(navitems.at(0).text()).toBe('Mittagsangebote')
    expect(navitems.at(1).text()).toBe('Einstellungen')
    expect(navitems.at(2).text()).toBe('Info')
  })*/
})
