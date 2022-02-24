
export module SharedAction {
  export class ChangeTheme {
    static readonly type = '[Shared] Change Theme';

    constructor(public themeName: string) { }
  }
}
