import {Component, Input} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {KilnStatus} from "../../../core/kiln-api/kiln-api.types";
import {Store} from "@ngxs/store";
import {Kiln} from "../../kiln.actions";
import StartProgram = Kiln.StartProgram;

@Component({
  selector: 'app-kiln-control-form',
  templateUrl: './control-form.component.html',
  styleUrls: [
    './control-form.component.scss'
  ]
})
export class ControlFormComponent {
  @Input()
  kilnStatus: KilnStatus | null;

  manualProgramForm: FormGroup;

  constructor(
    private fb$: FormBuilder,
    private store$: Store
  ) {
    this.manualProgramForm = fb$.group({
      'temperature': fb$.control('', Validators.required)
    });
  }

  submitManualProgram() {
    if (!this.manualProgramForm.valid) {
      return;
    }

    const value = this.manualProgramForm.value;

    this.store$.dispatch(new StartProgram({
      name: 'manual',
      options: {
        zones: [ ...this.kilnStatus!.zones ],
        temperature: {
          unit: 'celsius',
          value: value.temperature
        }
      }
    }))
  }
}
