import Header from '@/views/layout/Header.vue'
import RouterLinkIcon from '@/views/layout/header/NavLink.vue'
import { mountUnit } from '@tests/unit/test-util'
import DaySelector from '@/views/offers/DaySelector.vue'

describe('Header', () => {
  test('renders snapshot with title as h1 tag', () => {
    const mocks = { $route: routeWithTitleOnly }

    const wrapper = mountUnit(Header, {}, { mocks })

    expect(wrapper.element).toMatchSnapshot()
    expect(wrapper.find('h1').exists()).toBeTrue()
    expect(wrapper.find(DaySelector).exists()).toBeFalse()
    expect(wrapper.text()).toEqual('Mock-Title')
    expect(wrapper.find('h1').classes('sr-only')).toBeFalse()
  })

  test('renders snapshot with DaySelector', () => {
    const mocks = { $route: routeWithShowDaySelector }

    const wrapper = mountUnit(Header, {}, { mocks })

    expect(wrapper.element).toMatchSnapshot()
    expect(wrapper.find('h1').exists()).toBeTrue()
    expect(wrapper.find(DaySelector).exists()).toBeTrue()
    expect(wrapper.find('h1').classes()).toContain('sr-only')
  })

  test('has nav items in collapse', () => {
    const mocks = { $route: routeWithShowDaySelector }

    const wrapper = mountUnit(Header, {}, { mocks })

    const navitems = wrapper.findAll(RouterLinkIcon)
    expect(navitems.at(0).props('to')).toContain('/')
    expect(navitems.at(1).props('to')).toContain('/settings')
    expect(navitems.at(2).props('to')).toContain('/about')
  })
})

// --- mocks 'n' stuff

const routeWithTitleOnly = {
  meta: {
    title: 'Mock-Title',
  },
}

const routeWithShowDaySelector = {
  meta: {
    title: 'Mock-Title',
    showDaySelector: true,
  },
}
