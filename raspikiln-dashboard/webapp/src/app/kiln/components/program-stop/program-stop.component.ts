import {Component, Input} from "@angular/core";
import {Store} from "@ngxs/store";
import {Kiln} from "../../kiln.actions";
import EndProgram = Kiln.EndProgram;

@Component({
  selector: 'app-kiln-program-stop',
  templateUrl: './program-stop.component.html'
})
export class ProgramStopComponent {
  @Input()
  enabled: boolean = true;

  constructor(private store$: Store) { }

  stopProgram() {
    this.store$.dispatch(new EndProgram());
  }
}
