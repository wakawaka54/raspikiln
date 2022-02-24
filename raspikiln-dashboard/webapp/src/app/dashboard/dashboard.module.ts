import {NgModule} from "@angular/core";
import {DashboardComponent} from "./dashboard.component";
import {DashboardTabsComponent} from "./components/tabs/dashboard-tabs.component";
import {SharedModule} from "../shared/shared.module";
import {KilnModule} from "../kiln/kiln.module";

const components = [
  DashboardComponent,
  DashboardTabsComponent
];

@NgModule({
  declarations: [
    ...components
  ],
  imports: [
    KilnModule,
    SharedModule
  ],
  exports: [
    DashboardComponent
  ]
})
export class DashboardModule {

}
