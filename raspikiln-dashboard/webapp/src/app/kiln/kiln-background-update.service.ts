import {interval, Subscription, takeWhile} from "rxjs";
import {Store} from "@ngxs/store";
import {KilnStore} from "./kiln.store";
import {Kiln} from "./kiln.actions";
import GetStatus = Kiln.GetStatus;
import GetHistoricalTemperature = Kiln.GetHistoricalTemperature;
import {Injectable} from "@angular/core";

/**
 * Updates kiln information periodically.
 */
@Injectable({
  providedIn: "root"
})
export class KilnBackgroundUpdateService {
  private updateSubscription?: Subscription;

  constructor(private store$: Store) {
    this.store$.select(KilnStore.autoUpdate).subscribe(autoUpdate => {
      if (autoUpdate) {
        this.startPolling(5000)
      } else {
        this.stopPolling();
      }
    });
  }

  private startPolling(intervalMilliseconds: number) {
    this.updateSubscription = interval(intervalMilliseconds)
      .subscribe(_ => {
        this.store$.dispatch(new GetStatus());
        this.store$.dispatch(new GetHistoricalTemperature(new Date(0)));
      });
  }

  private stopPolling() {
    this.updateSubscription?.unsubscribe();
  }
}
