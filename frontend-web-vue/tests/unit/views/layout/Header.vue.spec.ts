import Header from '@/views/layout/Header.vue'
import NavLink from '@/views/layout/header/NavLink.vue'
import { mount } from '@vue/test-utils'
import { createRouterMock, injectRouterMock } from 'vue-router-mock'

describe('Header', () => {
  const router = createRouterMock({})
  beforeEach(() => {
    injectRouterMock(router)
  })

  test('renders snapshot', () => {
    const wrapper = mount(Header)

    expect(wrapper.element).toMatchSnapshot()
  })

  test('has all nav items', () => {
    const wrapper = mount(Header)

    const navitems = wrapper.findAllComponents(NavLink)
    expect(navitems[0].props('to')).toContain('/')
    expect(navitems[1].props('to')).toContain('/settings')
    expect(navitems[2].props('to')).toContain('/about')
  })
})
