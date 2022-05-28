import {Action, Selector, State, StateContext, Store} from "@ngxs/store";
import {SharedAction} from "./shared.actions";
import ChangeTheme = SharedAction.ChangeTheme;
import {Injectable} from "@angular/core";
import {isPreferDarkMode, ThemeService} from "./services/theme.service";
import ToggleToolbar = SharedAction.ToggleToolbar;

export interface SharedState {
  theme: string;
  toolbar: {
    enabled: boolean;
  }
}

@Injectable()
@State<SharedState>({
  name: 'shared',
  defaults: {
    theme: isPreferDarkMode() ? 'dark-theme' : 'light-theme',
    toolbar: {
      enabled: true
    }
  }
})
export class SharedStateStore {

  @Selector([SharedStateStore])
  static theme(state: SharedState): string {
    return state.theme;
  }

  @Selector([SharedStateStore])
  static toolbarEnabled(state: SharedState): boolean {
    return state.toolbar.enabled;
  }

  @Action(ChangeTheme)
  changeTheme(ctx: StateContext<SharedState>, { themeName }: ChangeTheme) {
    const state = ctx.getState();
    return ctx.setState({
      ...state,
      theme: themeName
    });
  }

  @Action(ToggleToolbar)
  toggleToolbar(ctx: StateContext<SharedState>, { enabled }: ToggleToolbar) {
    const state = ctx.getState();
    return ctx.setState({
      ...state,
      toolbar: {
        enabled
      }
    })
  }
}
