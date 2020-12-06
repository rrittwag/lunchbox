export function today(): Date {
  const localNow = new Date()
  return new Date(Date.UTC(localNow.getFullYear(), localNow.getMonth(), localNow.getDate()))
}
