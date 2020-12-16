import Header from '/@/views/layout/Header.vue'
import NavLink from '/@/views/layout/header/NavLink.vue'
import { mount } from '@vue/test-utils'
import router from '/@/router'

describe('Header', () => {
  test('renders snapshot', () => {
    const wrapper = mount(Header, {
      global: { plugins: [router] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  test('has all nav items', () => {
    const wrapper = mount(Header, {
      global: { plugins: [router] },
    })

    const navitems = wrapper.findAllComponents(NavLink)
    expect(navitems[0].props('to')).toContain('/')
    expect(navitems[1].props('to')).toContain('/settings')
    expect(navitems[2].props('to')).toContain('/about')
  })
})
