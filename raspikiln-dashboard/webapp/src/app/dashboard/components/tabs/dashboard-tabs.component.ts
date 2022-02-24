import {Component, OnInit} from "@angular/core";
import {Select} from "@ngxs/store";
import {KilnStore} from "../../../kiln/kiln.store";
import {filter, map, Observable, switchMap} from "rxjs";
import {KilnStatus} from "../../../core/kiln-api/kiln-api.types";
import {ChartOptions, MetricSeries} from "../../../kiln/components/metrics-chart/metrics-chart.component";
import {DashboardConfig} from "../../../kiln/kiln.actions";
import {UntilDestroy, untilDestroyed} from "@ngneat/until-destroy";

@UntilDestroy()
@Component({
  selector: 'app-dashboard-tabs',
  templateUrl: './dashboard-tabs.component.html',
  styleUrls: [
    './dashboard-tabs.component.scss'
  ]
})
export class DashboardTabsComponent implements OnInit {
  @Select(KilnStore.dashboardConfig)
  dashboardConfig: Observable<DashboardConfig>;

  @Select(KilnStore.currentKilnStatus)
  kilnStatus: Observable<KilnStatus>;

  dashboardMetricOptions: ChartOptions = {
    yAxis: {
      formatter: '{value} °C'
    }
  };

  dashboardMetrics: Observable<MetricSeries[]>

  ngOnInit() {
    this.dashboardMetrics = this.dashboardConfig
      .pipe(
        untilDestroyed(this),
        map(config => this.createMetrics(config))
      );
  }

  createMetrics(config: DashboardConfig): MetricSeries[] {
    const temperatures: MetricSeries[] = config.temperatureMetrics.map(metric => (
      {
        name: metric.name,
        metricName: metric.metricName,
        lineStyle: {
          color: '#E64A19',
          type: 'solid',
          width: 2,
          opacity: 1
        }
      }
    ));

    const targets: MetricSeries[] = config.targetMetrics.map(metric => (
      {
        name: metric.name,
        metricName: metric.metricName,
        lineStyle: {
          color: '#FFAB91',
          type: 'solid',
          width: 2,
          opacity: 0.5,
        }
      }
    ));

    return [ ...temperatures, ...targets ];
  }
}
