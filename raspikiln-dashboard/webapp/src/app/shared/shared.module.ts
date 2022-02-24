import {NgModule} from "@angular/core";
import {MatListModule} from "@angular/material/list";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {MatIconModule} from "@angular/material/icon";
import {CommonModule} from "@angular/common";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatButtonModule} from "@angular/material/button";
import {MatTabsModule} from "@angular/material/tabs";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {ThemeToggleComponent} from "./components/theme-toggle/theme-toggle.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatSelectModule} from "@angular/material/select";

const common = [
  CommonModule,
  ReactiveFormsModule
];

const material = [
  MatListModule,
  MatSidenavModule,
  MatMenuModule,
  MatButtonToggleModule,
  MatIconModule,
  MatGridListModule,
  MatProgressSpinnerModule,
  MatButtonModule,
  MatTabsModule,
  MatCardModule,
  MatFormFieldModule,
  MatInputModule,
  MatSnackBarModule,
  MatSlideToggleModule,
  MatToolbarModule,
  MatSelectModule,
];

const components = [
  ThemeToggleComponent
];

@NgModule({
  declarations: [
    ...components,
  ],
  imports: [
    ...common,
    ...material,
  ],
  exports: [
    ...common,
    ...material,
    ...components
  ]
})
export class SharedModule {

}
