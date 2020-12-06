/**
 * Type for describing an error, sent by backend REST API.
 * <p>
 * ApiError corresponds to the default HTTP error response body of Spring.
 */
export interface ApiError extends Error {
  message: string
  path: string
  status: number
  timestamp?: string
  error?: string
}

/**
 * Enhances Fetch API with:
 * <ul>
 *   <li>4xx/5xx responses resulting in ApiError</li>
 *   <li>configurable timeoutMillis (default 10 seconds)</li>
 * </ul>
 *
 * @param url
 * @param options
 * @param timeoutMillis
 */
export function fetchWithTimeout(
  url: string,
  options: RequestInit = {},
  timeoutMillis = 10000
): Promise<Response> {
  const controller = new AbortController()
  const config = { ...options, signal: controller.signal }

  setTimeout(() => {
    controller.abort()
  }, timeoutMillis)

  return fetch(url, config)
    .then(async response => {
      const contentType = response.headers.get('content-type') || ''
      if (!response.ok) {
        // when 4xx/5xx response, Spring backend answers with ApiError (prop 'name' excluded)
        if (contentType.startsWith('application/json'))
          throw { name: 'ApiError', ...(await response.json()) }

        throw createApiError(response.status, url, await response.text())
      }
      /*
      // unpack JSON data automatically
      if (contentType.startsWith('application/json'))
        return response.json()

      // TODO: unpack text() & blob()
*/
      return response
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
