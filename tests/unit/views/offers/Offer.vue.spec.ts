import Offer from '@/views/offers/Offer.vue'
import { gyros } from '@tests/unit/test-data'
import { mountUnit } from '@tests/unit/test-util'
import Badge from '@/views/offers/Badge.vue'

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
    const items = wrapper.findAll(Badge)
    expect(items.length).toEqual(2)
    expect(items.at(0).props('label')).toEqual(gyros.tags[0])
    expect(items.at(1).props('label')).toEqual(gyros.tags[1])
  })
})
