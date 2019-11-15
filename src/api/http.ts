import axios, { AxiosError } from 'axios'
import { ApiError } from '@/api'

const http = axios.create({
  baseURL: process.env.BASE_API,
  timeout: 5000, // request timeout
})

http.interceptors.request.use(
  config => {
    // TODO: config Authorization token
    return config
  },
  error => Promise.reject(createApiError(error))
)

http.interceptors.response.use(
  response => response,
  error => Promise.reject(createApiError(error))
)

function createApiError(error: AxiosError): ApiError {
  // eslint-disable-next-line no-console
  console.log('AxiosError: ' + JSON.stringify(error))

  // non 2xx response
  if (error.response) {
    if (!error.response.data)
      return {
        name: 'ApiError',
        message: error.response.statusText,
        path: toString(error.config.url),
        status: error.response.status,
      }
    if (typeof error.response.data === 'string')
      return {
        name: 'ApiError',
        message: error.response.data,
        path: toString(error.config.url),
        status: error.response.status,
      }
    return error.response.data // Spring returns ApiError object
  }

  // no response
  if (error.request)
    return {
      name: 'ApiError',
      message: error.message,
      path: toString(error.config.url),
      status: 0,
    }

  // error on creating request
  return {
    name: 'ApiError',
    message: error.message,
    path: '',
    status: 0,
  }
}

function toString(value: string | undefined): string {
  if (value) return value
  return ''
}

export default http
