import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgxEchartsModule} from "ngx-echarts";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatMenuModule} from "@angular/material/menu";
import {MatListModule} from "@angular/material/list";
import {KilnApiServiceOptions, KILN_API_SERVICE_OPTIONS} from "./core/kiln-api/kiln-api.service";
import {CommonModule} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {NgxsModule} from "@ngxs/store";
import {KilnStore} from "./kiln/kiln.store";
import {NgxsReduxDevtoolsPluginModule} from "@ngxs/devtools-plugin";
import {KilnModule} from "./kiln/kiln.module";
import {SharedModule} from "./shared/shared.module";
import {DashboardModule} from "./dashboard/dashboard.module";
import {SnackbarStore} from "./core/snackbar/snackbar.store";
import {SharedStateStore} from "./shared/shared.state";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    HttpClientModule,

    DashboardModule,

    NgxEchartsModule.forRoot({
      echarts: () => import('echarts')
    }),

    NgxsModule.forRoot([SnackbarStore, KilnStore, SharedStateStore], {
      developmentMode: true
    }),

    NgxsReduxDevtoolsPluginModule.forRoot(),

    KilnModule,

    BrowserAnimationsModule,
    SharedModule
  ],
  providers: [
    {
      provide: KILN_API_SERVICE_OPTIONS,
      useValue: {
        // endpoint: 'http://192.168.1.157:8080'
        endpoint: 'http://localhost:8080'
      } as KilnApiServiceOptions
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
