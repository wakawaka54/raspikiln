import {KilnDashboardConfig, MetricInfo, ProgramDefinition} from "../core/kiln-api/kiln-api.types";

export namespace Kiln {

  export class GetStatus {
    static readonly type = '[Kiln] Get Status';
    constructor() { }
  }

  export class GetHistoricalTemperature {
    static readonly type = '[Kiln] Get Historical Temperature';
    constructor(public from: Date) { }
  }

  export class StartProgram {
    static readonly type = '[Kiln] Start Program';
    constructor(public definition: ProgramDefinition) { }
  }

  export class EndProgram {
    static readonly type = '[Kiln] End Program';
    constructor() { }
  }

  export class GetConfig {
    static readonly type = '[Kiln] Set Config';
    constructor(public config: KilnDashboardConfig) { }
  }
}

export interface DashboardConfig {
  zones: string[];
  temperatureMetrics: MetricInfo[];
  targetMetrics: MetricInfo[];
}
