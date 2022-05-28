import {Component, OnInit} from "@angular/core";
import {UntilDestroy} from "@ngneat/until-destroy";
import {Temperature} from "../../../core/kiln-api/kiln-api.types";
import {Observable} from "rxjs";
import {Select} from "@ngxs/store";
import {KilnStore} from "../../../kiln/kiln.store";

@UntilDestroy()
@Component({
  selector: 'app-kiosk-big-temperature',
  templateUrl: './big-temperature.component.html',
  styleUrls: [
    './big-temperature.component.scss'
  ]
})
export class BigTemperatureComponent implements OnInit {

  @Select(KilnStore.currentTemperature)
  kilnTemperature: Observable<Temperature>;

  ngOnInit() {

  }
}
