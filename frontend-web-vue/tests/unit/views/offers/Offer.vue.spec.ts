import Offer from '@/views/offers/Offer.vue'
import Badge from '@/views/offers/Badge.vue'
import { gyros } from '@tests/unit/test-data'
import { shallowMount } from '@vue/test-utils'

describe('Offer', () => {
  it('renders snapshot', () => {
    const wrapper = shallowMount(Offer, {
      props: { offer: gyros },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders name, description & tags', () => {
    const wrapper = shallowMount(Offer, {
      props: { offer: gyros },
    })

    expect(wrapper.text()).toContain(gyros.name)
    expect(wrapper.text()).toContain(gyros.description)
    expect(wrapper.text()).toContain('€')
    const items = wrapper.findAllComponents(Badge)
    expect(items.length).toEqual(2)
    expect(items[0].props('label')).toEqual(gyros.tags[0])
    expect(items[1].props('label')).toEqual(gyros.tags[1])
  })

  it('renders offer without price', () => {
    const gyrosWithoutPrice = { ...gyros, price: null }
    const wrapper = shallowMount(Offer, {
      props: { offer: gyrosWithoutPrice },
    })

    expect(wrapper.text()).toContain(gyros.name)
    expect(wrapper.text()).toContain(gyros.description)
    expect(wrapper.text()).not.toContain('€')
    const items = wrapper.findAllComponents(Badge)
    expect(items.length).toEqual(2)
    expect(items[0].props('label')).toEqual(gyros.tags[0])
    expect(items[1].props('label')).toEqual(gyros.tags[1])
  })

  it('hides details for screen size XS', () => {
    const wrapper = shallowMount(Offer, {
      props: { offer: gyros },
    })

    const hiddenItems = wrapper.findAll('.hidden')
    expect(hiddenItems.length).toEqual(2)
    expect(hiddenItems[0].text()).toEqual(gyros.description)
    expect(hiddenItems[1].findAll('badge-stub').length).toEqual(2)
  })

  it('shows details for screen size XS', () => {
    const wrapper = shallowMount(Offer, {
      props: { offer: gyros, showDetailsInXS: true },
    })

    const hiddenItems = wrapper.findAll('.hidden')
    expect(hiddenItems.length).toEqual(0)
  })
})
