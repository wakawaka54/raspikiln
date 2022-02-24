import {Component, Input} from "@angular/core";
import {KilnStatus} from "../../../core/kiln-api/kiln-api.types";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-kiln-custom-program-form',
  templateUrl: './custom-program-form.component.html',
  styleUrls: [
    './custom-program-form.component.scss'
  ]
})
export class CustomProgramFormComponent {

  @Input()
  kilnStatus: KilnStatus;

  customProgramForm: FormGroup;

  constructor(private fb$: FormBuilder) {

  }
}
