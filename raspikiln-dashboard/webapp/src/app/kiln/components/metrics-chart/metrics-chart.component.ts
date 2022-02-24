import {AfterViewInit, Component, Input, OnInit} from "@angular/core";
import {EChartsOption, SeriesOption} from "echarts";
import {KilnApiService} from "../../../core/kiln-api/kiln-api.service";
import {interval, Observable, Subscription, switchMap, takeUntil, takeWhile, timer} from "rxjs";
import {MetricQueryResult} from "../../../core/kiln-api/kiln-api.types";
import {subMinutes, subSeconds} from "date-fns";
import {UntilDestroy, untilDestroyed} from "@ngneat/until-destroy";
import {ThemeService} from "../../../shared/services/theme.service";
import {ThemeOptions} from "../../../shared/themes/theme";
import {FormControl} from "@angular/forms";

@UntilDestroy()
@Component({
  selector: 'app-metrics-chart',
  templateUrl: './metrics-chart.component.html',
  styleUrls: [
    './metrics-chart.component.scss'
  ]
})
export class MetricsChartComponent implements OnInit, AfterViewInit {
  @Input()
  chartOptions: ChartOptions

  @Input()
  metrics: MetricSeries[];

  loaded: boolean = false;
  options: EChartsOption;
  dynamicData: EChartsOption = { };
  timeRangeControl = new FormControl(1800)
  private querySubscription: Subscription;

  constructor(
    private kilnApi$: KilnApiService,
    private themeService$: ThemeService
  ) { }

  ngOnInit() {
    this.liveRefresh();
  }

  ngAfterViewInit() {
    this.themeService$.getThemeStyle().subscribe(theme => this.setOptions(theme));
  }

  timeRangeUpdate() {
    this.liveRefresh();
  }

  private setOptions({ echarts }: ThemeOptions) {
    this.options = {
      backgroundColor: echarts.bg,
      tooltip: {
        trigger: "axis",
        axisPointer: {
          animation: false,
          label: {
            backgroundColor: echarts.tooltipBackgroundColor
          }
        }
      },
      legend: {
        top: 20,
        textStyle: {
          color: echarts.textColor
        }
      },
      grid: {
        bottom: 80
      },
      dataZoom: [
        {
          type: 'slider'
        },
        {
          type: 'inside'
        }
      ],
      color: this.metrics.map(metric => metric.lineStyle.color),
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'time',
        axisLine: {
          lineStyle: {
            color: echarts.axisLineColor
          }
        },
        axisLabel: {
          color: echarts.textColor,
          fontSize: '12'
        }
      },
      yAxis: {
        type: 'value',
        axisLine: {
          lineStyle: {
            color: echarts.axisLineColor,
          },
        },
        splitLine: {
          lineStyle: {
            color: echarts.splitLineColor,
          },
        },
        axisLabel: {
          color: echarts.textColor,
          formatter: this.chartOptions.yAxis.formatter,
          fontSize: '12'
        },
      },
      ...this.dynamicData
    };
  }

  private liveRefresh() {
    this.querySubscription?.unsubscribe();

    this.querySubscription =
      timer(0, 5000).pipe(
        untilDestroyed(this),
        switchMap(() => this.kilnApi$.metrics(subSeconds(new Date(), this.timeRangeControl.value), new Date(), this.metricNames()))
      ).subscribe(queryResults => this.updateMetrics(queryResults));
  }

  private metricNames(): string[] {
    return this.metrics.map(metric => metric.metricName ?? metric.name);
  }

  private updateMetrics(queryResults: MetricQueryResult) {
    const series = this.metrics.map((_, index) => this.createSeries(index, queryResults));

    this.dynamicData = {
      legend: {
        data: series?.map(s => s.name!.toString()) ?? []
      },
      series: series
    };

    this.loaded = true;
  }

  private createSeries(index: number, queryResults: MetricQueryResult): SeriesOption {
    const series = this.metrics[index];
    const data = queryResults.points.map(values => {
      return {
        name: dateValue(values[0]).toString(),
        value: [ dateValue(values[0]).getTime(), rounded(values[index + 1]!!) ]
      }
    });

    return {
      name: series.name,
      type: 'line',
      showSymbol: false,
      animation: false,
      lineStyle: {
        type: series.lineStyle.type,
        width: series.lineStyle.width,
        opacity: series.lineStyle.opacity
      },
      emphasis: {
        scale: false,
        focus: "none",
        lineStyle: {
          width: series.lineStyle.width
        }
      },
      data: data
    };
  }
}

export interface ChartOptions {
  yAxis: {
    formatter?: string
  }
}

export interface MetricSeries {
  name: string;
  metricName?: string;
  lineStyle: {
    color: string;
    type: 'solid' | 'dashed';
    width: number;
    opacity: number;
  }
}

function rounded(value: number): string {
  if (value == null) {
    return value;
  }

  return value.toFixed(2);
}

function dateValue(num: number) {
  return new Date(num * 1000);
}
