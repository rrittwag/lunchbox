import Nav from '/@/views/layout/header/Nav.vue'
import { shallowMount } from '@vue/test-utils'

describe('Nav', () => {
  test('renders snapshot', () => {
    const wrapper = shallowMount(Nav)

    expect(wrapper.element).toMatchSnapshot()
  })

  test.todo('renders slot NavLinks')
})
