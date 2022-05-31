
export interface KilnStatus {
  zones: string[];
  temperature: Temperature;
  temperatureTarget?: Temperature;
  armState: SwitchState;
}

export interface ProgramDefinition {
  name: string;
  options: any;
}

export interface HistoricalEntry<T> {
  timestamp: number;
  value: T;
}

export interface Temperature {
  unit: string;
  value: number;
}

export const TEMPERATURE_UNKNOWN: Temperature = {
  value: -1000,
  unit: "celsius"
}

export interface MetricQueryResult {
  series: MetricQuerySeries[];
  points: ([ number, number? ])[]
}

export interface MetricQuerySeries {
  name: string;
}

export interface KilnDashboardConfig {
  programs: {
    manual: ManualProgramConfig;
    automatic: AutomaticProgramConfig[];
  },
  chart: {
    temperatureMetrics: MetricInfo[];
    targetMetrics: MetricInfo[];
  }
}

export interface ManualProgramConfig {
  name: string;
  controllers: string[];
}

export interface AutomaticProgramConfig {
  name: string;
  steps: AutomaticProgramStep[];
}

export interface AutomaticProgramStep {
  temperature: number;
  ramp: number;
  controller: string;
}

export interface MetricInfo {
  name: string;
  metricName: string;
}

export type SwitchState = 'on' | 'off';
