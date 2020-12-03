import OfferBox from '@/views/offers/OfferBox.vue'
import { mensa, gyros, soljanka } from '@tests/unit/test-data'
import { mountUnit } from '@tests/unit/test-util'
import Offer from '@/views/offers/Offer.vue'

describe('OfferBox', () => {
  it('renders snapshot', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  it('renders title & offers', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    expect(wrapper.text()).toEqual(mensa.name)
    const items = wrapper.findAll(Offer)
    expect(items.length).toEqual(2)
  })

  it('renders offers in order', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll(Offer)
    expect(items.at(0).props('offer')).toEqual(gyros)
    expect(items.at(1).props('offer')).toEqual(soljanka)
  })

  it('WHEN offers are empty  THEN render just title', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [],
    })

    expect(wrapper.text()).toEqual(mensa.name)
    const items = wrapper.findAll(Offer)
    expect(items.length).toBe(0)
  })

  it('hide details for screen size XS', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll(Offer)
    expect(items.at(0).props('showDetailsInXS')).toEqual(false)
    expect(items.at(1).props('showDetailsInXS')).toEqual(false)
  })

  it('WHEN clicked  THEN show details for screen size XS', async () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    wrapper.trigger('click')
    await wrapper.vm.$nextTick() // Wait until trigger events have been handled

    const items = wrapper.findAll(Offer)
    expect(items.at(0).props('showDetailsInXS')).toEqual(true)
    expect(items.at(1).props('showDetailsInXS')).toEqual(true)
  })
})