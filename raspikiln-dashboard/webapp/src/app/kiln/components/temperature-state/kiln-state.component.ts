
import {Component, OnInit} from "@angular/core";
import {Select, Selector} from "@ngxs/store";
import {KilnStore} from "../../kiln.store";
import {Observable} from "rxjs";
import {KilnStatus, SwitchState, Temperature} from "../../../core/kiln-api/kiln-api.types";
import {UntilDestroy, untilDestroyed} from "@ngneat/until-destroy";

/**
 * Displays the current kiln temperature along with other status information.
 */
@UntilDestroy()
@Component({
  selector: 'app-kiln-state',
  templateUrl: './kiln-state.component.html'
})
export class KilnStateComponent implements OnInit {
  temperature: Temperature;
  targetTemperature?: Temperature;
  armState: SwitchState;

  @Select(KilnStore.currentKilnStatus)
  private kilnStatus: Observable<KilnStatus>;

  ngOnInit() {
    this.kilnStatus
      .pipe(untilDestroyed(this))
      .subscribe(status => {
        this.temperature = status.temperature;
        this.targetTemperature = status.temperatureTarget;
        this.armState = status.armState;
      })
  }
}
