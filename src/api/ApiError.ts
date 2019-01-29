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
