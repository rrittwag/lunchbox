import {
  DaySelectorDirection,
  LABEL_GO_TO_NEXT_DAY,
  LABEL_GO_TO_PREVIOUS_DAY,
} from '@/views/offers/dayselector/DaySelector.values'
import DaySelectorButton from '@/views/offers/dayselector/DaySelectorButton.vue'
import userEvent from '@testing-library/user-event'
import { render } from '@testing-library/vue'

describe('daySelectorButton', () => {
  it('renders as previous day', () => {
    const { getByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.PREVIOUS },
    })

    const button = getByRole('button')
    expect(button).toHaveAccessibleName(LABEL_GO_TO_PREVIOUS_DAY)
    expect(button).toBeEnabled()
  })

  it('renders as next day', () => {
    const { getByRole } = render(DaySelectorButton, {
      props: { direction: DaySelectorDirection.NEXT },
    })

    const button = getByRole('button')
    expect(button).toHaveAccessibleName(LABEL_GO_TO_NEXT_DAY)
    expect(button).toBeEnabled()
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
