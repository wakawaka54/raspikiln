import {Component, Input, OnInit} from "@angular/core";
import {FormControl} from "@angular/forms";
import {Select, Selector, Store} from "@ngxs/store";
import {SharedAction} from "../../shared.actions";
import ChangeTheme = SharedAction.ChangeTheme;
import {Observable} from "rxjs";
import {SharedStateStore} from "../../shared.state";

@Component({
  selector: 'app-theme-toggle',
  templateUrl: './theme-toggle.component.html'
})
export class ThemeToggleComponent implements OnInit {
  @Input()
  themes: string[];

  toggleControl = new FormControl(false);

  @Select(SharedStateStore.theme)
  private theme$: Observable<string>;

  constructor(private store$: Store) { }

  ngOnInit() {
    this.store$.selectOnce(SharedStateStore.theme)
      .subscribe(initialTheme => {
        const indexOf = this.themes.indexOf(initialTheme);
        this.toggleControl.setValue(!!indexOf);
      });
  }

  themeToggle() {
    const theme = this.themes[this.toggleControl.value ? 0 : 1];
    this.store$.dispatch(new ChangeTheme(theme));
  }
}
