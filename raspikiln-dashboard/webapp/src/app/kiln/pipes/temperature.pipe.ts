import {Pipe, PipeTransform} from "@angular/core";
import {Temperature} from "../../core/kiln-api/kiln-api.types";

@Pipe({
  name: 'temperatureFormat',
  pure: true
})
export class TemperaturePipe implements PipeTransform {
  transform(value: any): any {
    if (value && (value as Temperature).value) {
      const temperature = value as Temperature;
      return temperature.value.toFixed(2) + ' °C';
    } else {
      return '- °C';
    }
  }
}
