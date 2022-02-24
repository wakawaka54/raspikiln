import {Action, State, StateContext, Store} from "@ngxs/store";
import {Snackbar} from "./snackbar.actions";
import ShowMessage = Snackbar.ShowMessage;
import {MatSnackBar} from "@angular/material/snack-bar";
import {Injectable} from "@angular/core";
import {SharedStateStore} from "../../shared/shared.state";

export interface SnackbarState { }

@Injectable()
@State<SnackbarState>({
  name: 'snackbar',
  defaults: { }
})
export class SnackbarStore {
  constructor(
    private snackbarService$: MatSnackBar
  ) { }

  @Action(ShowMessage)
  showMessage(ctx: StateContext<SnackbarState>, { message }: ShowMessage) {
    this.snackbarService$.open(message.message, message.action, {
      duration: 3000,
      verticalPosition: "bottom",
      horizontalPosition: "right"
    })
  }
}
