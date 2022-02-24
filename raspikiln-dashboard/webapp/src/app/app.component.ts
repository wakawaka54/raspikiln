import {Component, HostBinding, OnInit} from '@angular/core';
import {Select} from "@ngxs/store";
import {SharedStateStore} from "./shared/shared.state";
import {Observable} from "rxjs";
import {OverlayContainer} from "@angular/cdk/overlay";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  @HostBinding("class")
  public cssClass: string;

  @Select(SharedStateStore.theme)
  private theme$: Observable<string>;

  title = 'webapp';

  constructor(private overlayContainer: OverlayContainer) { }

  ngOnInit() {
    this.themeToggle();
  }

  private themeToggle() {
    let previousTheme: string | null = null;
    this.theme$.subscribe(theme => {
      this.cssClass = theme;

      if (previousTheme) {
        this.overlayContainer.getContainerElement().classList.remove(previousTheme)
      }

      this.overlayContainer.getContainerElement().classList.add(theme);
      previousTheme = theme;
    })
  }
}
