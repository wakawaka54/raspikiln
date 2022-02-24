import {Action, Selector, State, StateContext, Store} from "@ngxs/store";
import {SharedAction} from "./shared.actions";
import ChangeTheme = SharedAction.ChangeTheme;
import {Injectable} from "@angular/core";
import {isPreferDarkMode, ThemeService} from "./services/theme.service";

export interface SharedState {
  theme: string;
}

@Injectable()
@State<SharedState>({
  name: 'shared',
  defaults: {
    theme: isPreferDarkMode() ? 'dark-theme' : 'light-theme'
  }
})
export class SharedStateStore {

  @Selector([SharedStateStore])
  static theme(state: SharedState): string {
    return state.theme;
  }

  @Action(ChangeTheme)
  changeTheme(ctx: StateContext<SharedState>, { themeName }: ChangeTheme) {
    const state = ctx.getState();
    return ctx.setState({
      ...state,
      theme: themeName
    });
  }
}
