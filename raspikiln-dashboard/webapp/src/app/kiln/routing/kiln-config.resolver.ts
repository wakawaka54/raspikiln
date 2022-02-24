import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Observable, switchMap} from "rxjs";
import {Store} from "@ngxs/store";
import {KilnApiService} from "../../core/kiln-api/kiln-api.service";
import {Kiln} from "../kiln.actions";
import GetConfig = Kiln.GetConfig;
import {Injectable} from "@angular/core";

@Injectable({ providedIn: 'root' })
export class KilnConfigResolver implements Resolve<Observable<any>> {

  constructor(
    private kilnApi$: KilnApiService,
    private store$: Store
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
    return this.kilnApi$.config()
      .pipe(switchMap(config => this.store$.dispatch(new GetConfig(config))));
  }
}
