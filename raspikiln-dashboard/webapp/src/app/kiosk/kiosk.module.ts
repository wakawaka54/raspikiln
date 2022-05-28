import {NgModule} from "@angular/core";
import {KioskComponent} from "./kiosk.component";
import {SharedModule} from "../shared/shared.module";
import {KilnModule} from "../kiln/kiln.module";
import {BigTemperatureComponent} from "./components/big-temperature/big-temperature.component";
import {ControlsComponent} from "./components/controls/controls.component";

/**
 * Used for the kiln's kiosk mode.
 */
@NgModule({
  declarations: [
    KioskComponent,

    BigTemperatureComponent,
    ControlsComponent
  ],
  imports: [
    KilnModule,
    SharedModule
  ],
  exports: [
    KioskComponent
  ]
})
export class KioskModule {

}
