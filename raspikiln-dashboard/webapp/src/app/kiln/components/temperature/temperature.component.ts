import {Component, Input} from "@angular/core";
import {KilnStatus, Temperature} from "../../../core/kiln-api/kiln-api.types";
import {UntilDestroy, untilDestroyed} from "@ngneat/until-destroy";
import {Select} from "@ngxs/store";
import {KilnStore} from "../../kiln.store";
import {Observable} from "rxjs";

@UntilDestroy()
@Component({
  selector: 'app-kiln-temperature',
  templateUrl: './temperature.component.html'
})
export class TemperatureComponent {
  temperature: Temperature;

  @Select(KilnStore.currentKilnStatus)
  private kilnStatus: Observable<KilnStatus>;

  ngOnInit() {
    this.kilnStatus
      .pipe(untilDestroyed(this))
      .subscribe(status => {
        this.temperature = status.temperature;
      })
  }

  unitString(): string {
    switch (this.temperature.unit) {
      case 'celsius':
        return 'C';
      default:
        return 'unknown-unit';
    }
  }
}
