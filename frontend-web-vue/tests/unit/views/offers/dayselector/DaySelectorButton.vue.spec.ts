import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import {
  DaySelectorDirection,
  LABEL_GO_TO_NEXT_DAY,
  LABEL_GO_TO_PREVIOUS_DAY,
} from '@/views/offers/dayselector/DaySelector.values'
import { render } from '@testing-library/vue'
import userEvent from '@testing-library/user-event'

describe('DaySelectorButton', () => {
  it('renders as previous day', () => {
    const { queryByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS },
    })

    expect(queryByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY })).toBeEnabled()
    expect(queryByRole('button', { name: LABEL_GO_TO_NEXT_DAY })).not.toBeInTheDocument()
  })

  it('renders as next day', () => {
    const { queryByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.NEXT },
    })

    expect(queryByRole('button', { name: LABEL_GO_TO_PREVIOUS_DAY })).not.toBeInTheDocument()
    expect(queryByRole('button', { name: LABEL_GO_TO_NEXT_DAY })).toBeEnabled()
  })

  it('renders disabled', () => {
    const { getByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS, disabled: true },
    })

    const button = getByRole('button')
    expect(button).toBeDisabled()
    // Trotz disabled-Zustand kann ein Button bei Klick active werden.
    // -> https://stackoverflow.com/a/12592035
    // Für diesen unschönen Fall unterbinden wir das Stylen des Buttons.
    expect(button.getAttribute('class')).not.toContain('active:')
  })

  it('emits click event', async () => {
    const user = userEvent.setup()
    const { emitted, getByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS },
    })

    await user.click(getByRole('button'))

    expect(emitted()).toHaveProperty('click', [[]])
  })
})
