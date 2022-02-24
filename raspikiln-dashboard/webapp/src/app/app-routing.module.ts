import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {KilnConfigResolver} from "./kiln/routing/kiln-config.resolver";

const routes: Routes = [
  {
    path: '**',
    component: DashboardComponent,
    resolve: {
      kilnConfig: KilnConfigResolver
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      enableTracing: true
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
