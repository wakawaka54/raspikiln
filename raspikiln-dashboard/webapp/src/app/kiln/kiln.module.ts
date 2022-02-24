import {NgModule} from "@angular/core";
import {NgxsModule} from "@ngxs/store";
import {KilnStore} from "./kiln.store";
import {NgxEchartsModule} from "ngx-echarts";
import {SharedModule} from "../shared/shared.module";
import {KilnStateComponent} from "./components/temperature-state/kiln-state.component";
import {ControlFormComponent} from "./components/control-form/control-form.component";
import {TemperatureComponent} from "./components/temperature/temperature.component";
import {TemperaturePipe} from "./pipes/temperature.pipe";
import {MetricsChartComponent} from "./components/metrics-chart/metrics-chart.component";
import {CustomProgramFormComponent} from "./components/custom-program-form/custom-program-form.component";
import {ProgramStopComponent} from "./components/program-stop/program-stop.component";

const components = [
  ControlFormComponent,
  CustomProgramFormComponent,
  KilnStateComponent,
  TemperatureComponent,
  MetricsChartComponent,
  ProgramStopComponent,

  TemperaturePipe
];

@NgModule({
  declarations: [ ...components ],
  imports: [
    NgxEchartsModule.forChild(),
    NgxsModule.forFeature([KilnStore]),

    SharedModule
  ],
  exports: [ ...components ]
})
export class KilnModule {

}
