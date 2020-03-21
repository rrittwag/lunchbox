import Header from '@/views/layout/Header.vue'
import RouterLinkIcon from '@/views/layout/header/NavLink.vue'
import { mountUnit } from '@tests/unit/test-util'
import DaySelector from '@/views/offers/DaySelector.vue'

const routeWithTitle = {
  meta: {
    title: 'Mock-Title',
  },
}

const routeWithShowDaySelector = {
  meta: {
    showDaySelector: true,
  },
}

describe('Header', () => {
  test('renders snapshot with DaySelector', () => {
    const mocks = { $route: routeWithShowDaySelector }

    const wrapper = mountUnit(Header, {}, { mocks })

    expect(wrapper.element).toMatchSnapshot()
    const daySelector = wrapper.find(DaySelector)
    expect(daySelector.exists()).toBeTrue()
  })

  test('renders snapshot with title', () => {
    const mocks = { $route: routeWithTitle }

    const wrapper = mountUnit(Header, {}, { mocks })

    expect(wrapper.element).toMatchSnapshot()
    expect(wrapper.text()).toEqual('Mock-Title')
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
