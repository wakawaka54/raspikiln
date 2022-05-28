
export module SharedAction {
  export class ChangeTheme {
    static readonly type = '[Shared] Change Theme';

    constructor(public themeName: string) { }
  }

  export class ToggleToolbar {
    static readonly type = '[Shared] Toggle Toolbar';

    constructor(public enabled: boolean) { }
  }
}
