import LocationSelector from '@/views/layout/LocationSelector.vue'
import { mount } from '@vue/test-utils'
import { describe, test, expect } from 'vitest'

describe('LocationSelector', () => {
  test('renders snapshot', () => {
    const wrapper = mount(LocationSelector)

    expect(wrapper.element).toMatchSnapshot()
  })
  /*
  test('renders shortname of selected location', () => {
    mockStore.selectedLocation = hamburg

    const wrapper = mountWithChildren(LocationSelector, {}, { provide })

    const navitemDropdown = wrapper.find(BNavItemDropdown)
    expect(navitemDropdown.exists()).toBeTrue()
    expect(navitemDropdown.props('text')).toBe(hamburg.shortName)
  })

  test('renders drowdown item for every store location', () => {
    mockStore.selectedLocation = hamburg
    mockStore.locations = [hamburg, münchen]

    const wrapper = mountWithChildren(LocationSelector, {}, { provide })

    const dropdownItems = wrapper.findAll(BDropdownItem)
    expect(dropdownItems.length).toBe(2)
    expect(dropdownItems.at(0).text()).toBe(hamburg.name)
    expect(dropdownItems.at(1).text()).toBe(münchen.name)
  })

  test('renders selectedLocation as active', () => {
    mockStore.selectedLocation = münchen
    mockStore.locations = [hamburg, münchen]

    const wrapper = mountWithChildren(LocationSelector, {}, { provide })

    expect(
      wrapper
        .findAll(BDropdownItem)
        .at(0)
        .props('active')
    ).toBeFalse()
    expect(
      wrapper
        .findAll(BDropdownItem)
        .at(1)
        .props('active')
    ).toBeTrue()
  })

  test('WHEN select location  THEN call store.setSelectedLocation', () => {
    mockStore.selectedLocation = hamburg
    mockStore.locations = [hamburg, münchen]
    const wrapper = mountWithChildren(LocationSelector, {}, { provide })

    const itemMünchen = wrapper.findAll(BDropdownItem).at(1)
    itemMünchen.find(BLink).trigger('click')

    expect(mockStore.setSelectedLocation).toBeCalledTimes(1)
    expect(mockStore.setSelectedLocation).toBeCalledWith(münchen)
  })*/
})
