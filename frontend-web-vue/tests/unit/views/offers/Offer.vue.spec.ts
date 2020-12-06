import Offer from '/@/views/offers/Offer.vue'
import { gyros } from '/@tests/unit/test-data'
import { mountUnit } from '/@tests/unit/test-util'
import Badge from '/@/views/offers/Badge.vue'

describe('Offer', () => {
  it('renders snapshot', () => {
    const wrapper = mountUnit(Offer, {
      offer: gyros,
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders name, description & tags', () => {
    const wrapper = mountUnit(Offer, {
      offer: gyros,
    })

    expect(wrapper.text()).toContain(gyros.name)
    expect(wrapper.text()).toContain(gyros.description)
    expect(wrapper.text()).toContain('€')
    const items = wrapper.findAll(Badge)
    expect(items.length).toEqual(2)
    expect(items.at(0).props('label')).toEqual(gyros.tags[0])
    expect(items.at(1).props('label')).toEqual(gyros.tags[1])
  })

  it('renders offer without price', () => {
    const gyrosWithoutPrice = { ...gyros, price: null }
    const wrapper = mountUnit(Offer, {
      offer: gyrosWithoutPrice,
    })

    expect(wrapper.text()).toContain(gyros.name)
    expect(wrapper.text()).toContain(gyros.description)
    expect(wrapper.text()).not.toContain('€')
    const items = wrapper.findAll(Badge)
    expect(items.length).toEqual(2)
    expect(items.at(0).props('label')).toEqual(gyros.tags[0])
    expect(items.at(1).props('label')).toEqual(gyros.tags[1])
  })

  it('hides details for screen size XS', () => {
    const wrapper = mountUnit(Offer, {
      offer: gyros,
    })

    const hiddenItems = wrapper.findAll('.hidden')
    expect(hiddenItems.length).toEqual(2)
    expect(hiddenItems.at(0).text()).toEqual(gyros.description)
    expect(hiddenItems.at(1).findAll(Badge).length).toEqual(2)
  })

  it('shows details for screen size XS', () => {
    const wrapper = mountUnit(Offer, {
      offer: gyros,
      showDetailsInXS: true,
    })

    const hiddenItems = wrapper.findAll('.hidden')
    expect(hiddenItems.length).toEqual(0)
  })
})
