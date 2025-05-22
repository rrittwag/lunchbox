import userEvent from '@testing-library/user-event'
import { render } from '@testing-library/vue'
import { TODAY, TOMORROW, YESTERDAY } from '@tests/test-data'
import DaySelector from '@/views/offers/DaySelector.vue'
import {
  DaySelectorDirection,
  LABEL_GO_TO_NEXT_DAY,
  LABEL_GO_TO_PREVIOUS_DAY,
} from '@/views/offers/dayselector/DaySelector.values'

it('renders', () => {
  const { getByRole } = render(DaySelector, {
    props: { selectedDay: YESTERDAY },
  })

  expect(getByRole('heading', { level: 2 })).toHaveTextContent('Sonntag 1.12.2019')
  expect(getByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY })).toBeEnabled()
  expect(getByRole('button', { name: LABEL_GO_TO_NEXT_DAY })).toBeEnabled()
})

it('renders disabled previous button', () => {
  const { getByRole } = render(DaySelector, {
    props: { selectedDay: YESTERDAY, disabledPrev: true },
  })

  expect(getByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY })).toBeDisabled()
  expect(getByRole('button', { name: LABEL_GO_TO_NEXT_DAY })).toBeEnabled()
})

it('renders disabled next button', () => {
  const { getByRole } = render(DaySelector, {
    props: { selectedDay: TOMORROW, disabledNext: true },
  })

  expect(getByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY })).toBeEnabled()
  expect(getByRole('button', { name: LABEL_GO_TO_NEXT_DAY })).toBeDisabled()
})

it('emits change event WHEN click previous day', async () => {
  const user = userEvent.setup()
  const { emitted, getByRole } = render(DaySelector, {
    props: { selectedDay: TODAY },
  })

  await user.click(getByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY }))

  expect(emitted()).toHaveProperty('change', [[DaySelectorDirection.PREVIOUS]])
})

it('emits change event WHEN click next day', async () => {
  const user = userEvent.setup()
  const { emitted, getByRole } = render(DaySelector, {
    props: { selectedDay: TODAY },
  })

  await user.click(getByRole('button', { name: LABEL_GO_TO_NEXT_DAY }))

  expect(emitted()).toHaveProperty('change', [[DaySelectorDirection.NEXT]])
})

it('emits change event WHEN pressing left arrow key', async () => {
  const user = userEvent.setup()
  const { emitted } = render(DaySelector, {
    props: { selectedDay: TODAY },
  })

  await user.keyboard('[ArrowLeft]')

  expect(emitted()).toHaveProperty('change', [[DaySelectorDirection.PREVIOUS]])
})

it('emits change event WHEN pressing right arrow key', async () => {
  const user = userEvent.setup()
  const { emitted } = render(DaySelector, {
    props: { selectedDay: TODAY },
  })

  await user.keyboard('[ArrowRight]')

  expect(emitted()).toHaveProperty('change', [[DaySelectorDirection.NEXT]])
})
