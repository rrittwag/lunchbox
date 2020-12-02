/**
 * Simplifies calling of and reacting to an action.
 * <p>
 * A component usually uses this type to call an async store action (calling API etc.).
 * The return value of Promise<ActionResult> makes it easy to react when the action completed.
 */
export type ActionFunction = () => Promise<ActionResult>

export enum ActionResultType {
  Success,
  Error,
}

export class ActionResult {
  readonly type: ActionResultType
  readonly actionName: string
  readonly errorMessage?: string

  private constructor(type: ActionResultType, actionName: string, errorMessage?: string) {
    this.type = type
    this.actionName = actionName
    this.errorMessage = errorMessage
  }

  isSuccess(): boolean {
    return this.type === ActionResultType.Success
  }

  isError(): boolean {
    return this.type === ActionResultType.Error
  }

  static success(actionName: string): ActionResult {
    return new ActionResult(ActionResultType.Success, actionName, undefined)
  }

  static error(actionName: string, errorMessage: string): ActionResult {
    return new ActionResult(ActionResultType.Error, actionName, errorMessage)
  }
}
