import { ApiError } from '@/api/ApiError'

/**
 * Enhances Fetch API with:
 * <ul>
 *   <li>4xx/5xx responses resulting in ApiError</li>
 *   <li>configurable timeout (default 10 seconds)</li>
 * </ul>
 *
 * @param url
 * @param options
 * @param time
 */
export function fetchWithTimeout(url: string, options: RequestInit = {}, time = 10000) {
  const controller = new AbortController()
  const config = { ...options, signal: controller.signal }

  setTimeout(() => {
    controller.abort()
  }, time)

  return fetch(url, config)
    .then(async response => {
      if (response.ok) return response

      // when 4xx/5xx response, Spring backend answers with ApiError (prop 'name' excluded)
      if (response.headers.get('content-type') === 'application/json')
        throw { name: 'ApiError', ...(await response.json()) }

      throw createApiError(response.status, url, await response.text())
    })
    .catch(error => {
      // eslint-disable-next-line no-console
      console.log(error)
      return Promise.reject(error)
    })
}

function createApiError(status: number, url: string, message: string): ApiError {
  return {
    name: 'ApiError',
    message: message,
    path: url,
    status: status,
  }
}
