import OfferBox from '@/views/offers/OfferBox.vue'
import { mensa, gyros, soljanka } from '@tests/unit/test-data'
import { mountUnit } from '@tests/unit/test-util'
import { BListGroupItem } from '@tests/unit/bootstrap-vue-components'

describe('OfferBox', () => {
  it('renders title & offers as list items', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll(BListGroupItem)
    expect(items.length).toEqual(3)
  })

  it('renders title as first list item', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const title = wrapper.findAll(BListGroupItem).at(0)
    expect(title.text()).toEqual(mensa.name)
  })

  it('renders offers in order', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [gyros, soljanka],
    })

    const items = wrapper.findAll(BListGroupItem)

    const offerItem1 = items.at(1)
    expect(offerItem1.find('.offer-name').text()).toEqual('Gyros')
    expect(offerItem1.find('.offer-price').text()).toEqual('3,50 €')

    const offerItem2 = items.at(2)
    expect(offerItem2.find('.offer-name').text()).toEqual('Soljanka')
    expect(offerItem2.find('.offer-price').text()).toEqual('2,50 €')
  })

  it('WHEN offers are empty  THEN render just title', () => {
    const wrapper = mountUnit(OfferBox, {
      provider: mensa,
      offers: [],
    })

    const items = wrapper.findAll(BListGroupItem)
    expect(items.length).toEqual(1)
  })

})
