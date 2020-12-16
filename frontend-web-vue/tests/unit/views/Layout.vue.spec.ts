import Layout from '/@/views/Layout.vue'
import { shallowMount } from '@vue/test-utils'
import { mocked } from 'ts-jest/utils'
jest.mock('/@/store/theme')
import { useTheme } from '/@/store/theme'

describe('Layout', () => {
  test('renders snapshot', () => {
    mocked(useTheme)

    const wrapper = shallowMount(Layout)

    expect(wrapper.element).toMatchSnapshot()
  })
})
