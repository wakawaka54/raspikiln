import {Component, Input} from "@angular/core";
import {Temperature} from "../../../core/kiln-api/kiln-api.types";

@Component({
  selector: 'app-kiln-temperature',
  templateUrl: './temperature.component.html'
})
export class TemperatureComponent {
  @Input()
  temperature: Temperature;

  unitString(): string {
    switch (this.temperature.unit) {
      case 'celsius':
        return 'C';
      default:
        return 'unknown-unit';
    }
  }
}
