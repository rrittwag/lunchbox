import OfferBox from '/@/views/offers/OfferBox.vue'
import { mensa, gyros, soljanka } from '/@tests/unit/test-data'
import Offer from '/@/views/offers/Offer.vue'
import { shallowMount } from '@vue/test-utils'

describe('OfferBox', () => {
  it('renders snapshot', () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [gyros, soljanka] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders title & offers', () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [gyros, soljanka] },
    })

    expect(wrapper.text()).toEqual(mensa.name)
    const items = wrapper.findAllComponents(Offer)
    expect(items.length).toEqual(2)
  })

  it('renders offers in order', () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [gyros, soljanka] },
    })

    const items = wrapper.findAllComponents(Offer)
    expect(items[0].props('offer')).toEqual(gyros)
    expect(items[1].props('offer')).toEqual(soljanka)
  })

  it('WHEN offers are empty  THEN render just title', () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [] },
    })

    expect(wrapper.text()).toEqual(mensa.name)
    const items = wrapper.findAllComponents(Offer)
    expect(items.length).toBe(0)
  })

  it('hide details for screen size XS', () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [gyros, soljanka] },
    })

    const items = wrapper.findAllComponents(Offer)
    expect(items[0].props('showDetailsInXS')).toEqual(false)
    expect(items[1].props('showDetailsInXS')).toEqual(false)
  })

  it('WHEN clicked  THEN show details for screen size XS', async () => {
    const wrapper = shallowMount(OfferBox, {
      props: { provider: mensa, offers: [gyros, soljanka] },
    })

    await wrapper.trigger('click')
    await wrapper.vm.$nextTick() // Wait until trigger events have been handled

    const items = wrapper.findAllComponents(Offer)
    expect(items[0].props('showDetailsInXS')).toEqual(true)
    expect(items[1].props('showDetailsInXS')).toEqual(true)
  })
})
