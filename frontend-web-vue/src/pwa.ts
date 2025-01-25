// @ts-expect-error virtual import is not ts compatible
import { registerSW } from 'virtual:pwa-register'

registerSW({
  immediate: true,
})
