import OfferBox from '@/views/offers/OfferBox.vue'
import { mensa, gyros, soljanka } from '../../test-data'
import { createComponent } from '../../test-util'

describe('OfferBox', () => {
  it('renders title & offers as list items', () => {
    const wrapper = createComponent(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll('b-list-group-item-stub')
    expect(items.length).toEqual(3)
  })

  it('renders title as first list item', () => {
    const wrapper = createComponent(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const title = wrapper.findAll('b-list-group-item-stub').at(0)
    expect(title.text()).toEqual(mensa.name)
  })

  it('renders offers in order', () => {
    const wrapper = createComponent(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll('b-list-group-item-stub')

    const offerItem1 = items.at(1)
    expect(offerItem1.find('.offer-name').text()).toEqual('Gyros')
    expect(offerItem1.find('.offer-price').text()).toEqual('3,50 €')

    const offerItem2 = items.at(2)
    expect(offerItem2.find('.offer-name').text()).toEqual('Soljanka')
    expect(offerItem2.find('.offer-price').text()).toEqual('2,50 €')
  })

  it('renders just title WHEN offers are empty', () => {
    const wrapper = createComponent(OfferBox, {
      provider: mensa,
      offers: [],
    })

    const items = wrapper.findAll('b-list-group-item-stub')
    expect(items.length).toEqual(1)
  })

})
