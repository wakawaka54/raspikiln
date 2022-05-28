import {Component} from "@angular/core";
import {Store} from "@ngxs/store";
import {SharedAction} from "../shared/shared.actions";
import ToggleToolbar = SharedAction.ToggleToolbar;

@Component({
  templateUrl: './kiosk.component.html',
  styleUrls: [
    './kiosk.component.scss'
  ]
})
export class KioskComponent {
  constructor(store: Store) {
    store.dispatch(new ToggleToolbar(false));
  }
}
