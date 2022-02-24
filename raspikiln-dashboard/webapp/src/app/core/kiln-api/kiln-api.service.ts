import {HttpClient, HttpParams} from "@angular/common/http";
import {
  HistoricalEntry,
  KilnDashboardConfig,
  KilnStatus,
  MetricQueryResult,
  ProgramDefinition,
  Temperature
} from "./kiln-api.types";
import {Observable} from "rxjs";
import {Inject, Injectable, InjectionToken} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class KilnApiService {
  constructor(private http$: HttpClient, @Inject(KILN_API_SERVICE_OPTIONS) private options: KilnApiServiceOptions) { }

  config(): Observable<KilnDashboardConfig> {
    return this.http$.get<KilnDashboardConfig>(this.apiUrl('v1/kiln/config'));
  }

  startProgram(definition: ProgramDefinition): Observable<KilnStatus> {
    return this.http$.post<KilnStatus>(this.apiUrl('v1/kiln/programs/start'), definition);
  }

  endProgram(): Observable<KilnStatus> {
    return this.http$.post<KilnStatus>(this.apiUrl('v1/kiln/programs/stop'), { });
  }

  currentState(): Observable<KilnStatus> {
    return this.http$.get<KilnStatus>(this.apiUrl('v1/kiln/current/state'))
  }

  historicalTemperature(from: Date): Observable<HistoricalEntry<Temperature>[]> {
    return this.http$.get<HistoricalEntry<Temperature>[]>(this.apiUrl('v1/kiln/historical/temperature'));
  }

  historicalSwitchState(from: Date): Observable<HistoricalEntry<number>[]> {
    return this.http$.get<HistoricalEntry<number>[]>(this.apiUrl('v1/kiln/historical/switchState'));
  }

  metrics(from: Date, to: Date, metricNames: string[]): Observable<MetricQueryResult> {
    return this.http$.get<MetricQueryResult>(
      this.apiUrl('v1/timeseries'),
      {
        params: {
          start: Math.round(from.getTime() / 1000.0),
          end: Math.round(to.getTime() / 1000.0),
          metricNames: metricNames.join(',')
        }
      }
    )
  }

  private apiUrl(path: string) {
    return `${this.options.endpoint}/${path}`;
  }
}

export const KILN_API_SERVICE_OPTIONS = new InjectionToken<KilnApiServiceOptions>("kilnApiServiceOptions")

export interface KilnApiServiceOptions {
  endpoint: string;
}
