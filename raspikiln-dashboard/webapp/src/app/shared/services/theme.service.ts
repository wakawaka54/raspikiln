import {Store} from "@ngxs/store";
import {Injectable} from "@angular/core";
import {map, Observable} from "rxjs";
import {ThemeOptions} from "../themes/theme";
import {SharedStateStore} from "../shared.state";
import {LIGHT_THEME} from "../themes/light.theme";
import {DARK_THEME} from "../themes/dark.theme";

@Injectable({
  providedIn: "root"
})
export class ThemeService {

  /**
   * Theme styles in the application.
   * @private
   */
  private static readonly THEME_STYLES = [
    LIGHT_THEME, DARK_THEME
  ];

  constructor(private store$: Store) { }

  getThemeStyle(): Observable<ThemeOptions> {
    return this.store$.select(SharedStateStore.theme).pipe(
      map(name => ThemeService.THEME_STYLES.find(style => style.name === name) ?? LIGHT_THEME),
    );
  }
}


export function isPreferDarkMode(): boolean {
  return window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
}
