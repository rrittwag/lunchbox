export enum DaySelectorDirection {
  PREVIOUS = 'prev',
  NEXT = 'next',
}

export interface DaySelectorEvent {
  direction: DaySelectorDirection
  day: Date
}
