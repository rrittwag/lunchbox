import Header from '@/views/layout/Header.vue'
import NavLink from '@/views/layout/header/NavLink.vue'
import Nav from '@/views/layout/header/Nav.vue'
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

  test('renders header tag and Nav component', () => {
    const wrapper = mount(Header)

    const headerTag = wrapper.get('header')
    const navTag = headerTag.getComponent(Nav)
    expect(navTag.findAllComponents(NavLink)).toHaveLength(3)
  })

  test('has all nav items', () => {
    const wrapper = mount(Header)

    const navitems = wrapper.findAllComponents(NavLink)
    expect(navitems[0].props('to')).toEqual('/')
    expect(navitems[1].props('to')).toEqual('/settings')
    expect(navitems[2].props('to')).toEqual('/about')
  })
})
