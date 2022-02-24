import {Action, Selector, State, StateContext} from "@ngxs/store";
import {KilnApiService} from "../core/kiln-api/kiln-api.service";
import {Injectable} from "@angular/core";
import {DashboardConfig, Kiln} from "./kiln.actions";
import {switchMap, tap} from "rxjs";
import {KilnStatus, MetricInfo, SwitchState, Temperature} from "../core/kiln-api/kiln-api.types";
import {KilnBackgroundUpdateService} from "./kiln-background-update.service";
import GetStatus = Kiln.GetStatus;
import StartProgram = Kiln.StartProgram;
import {Snackbar} from "../core/snackbar/snackbar.actions";
import ShowMessage = Snackbar.ShowMessage;
import GetConfig = Kiln.GetConfig;
import EndProgram = Kiln.EndProgram;

export interface KilnState {
  temperature: Temperature;
  temperatureTarget?: Temperature;
  armState: SwitchState;
  zones: string[];
  autoUpdate: boolean;
  dashboard: {
    temperatureMetrics: MetricInfo[];
    targetMetrics: MetricInfo[];
  }
}

@Injectable()
@State<KilnState>({
  name: 'kiln',
  defaults: {
    autoUpdate: true,
    armState: 'off',
    zones: [],
    temperature: {
      value: -100,
      unit: "celsius"
    },
    dashboard: {
      temperatureMetrics: [],
      targetMetrics: []
    }
  }
})
export class KilnStore {

  @Selector([KilnStore])
  static currentKilnStatus(state: KilnState): KilnStatus {
    return {
      zones: state.zones,
      temperature: state.temperature,
      temperatureTarget: state.temperatureTarget,
      armState: state.armState
    };
  }

  @Selector([KilnStore])
  static autoUpdate(state: KilnState): boolean {
    return state.autoUpdate;
  }

  @Selector([KilnStore])
  static dashboardConfig(state: KilnState): DashboardConfig {
    return {
      zones: state.zones,
      temperatureMetrics: state.dashboard.temperatureMetrics,
      targetMetrics: state.dashboard.targetMetrics
    };
  }

  constructor(
    private kilnApi$: KilnApiService,
    updater$: KilnBackgroundUpdateService,
  ) { }

  @Action(GetStatus)
  getStatus(ctx: StateContext<KilnState>) {
    return this.kilnApi$.currentState()
      .pipe(
        tap(kilnState => {
          const state = ctx.getState();
          ctx.setState({
            ...state,
            temperature: kilnState.temperature,
            armState: kilnState.armState,
            temperatureTarget: kilnState.temperatureTarget
          })
        })
      )
  }

  @Action(StartProgram)
  startProgram(ctx: StateContext<KilnState>, { definition }: StartProgram) {
    return this.kilnApi$.startProgram(definition)
      .pipe(switchMap(() => ctx.dispatch(new ShowMessage({
        message: 'Started manual program override.',
        action: 'Close'
      }))))
  }

  @Action(EndProgram)
  endProgram(ctx: StateContext<KilnState>) {
    return this.kilnApi$.endProgram()
      .pipe(
        switchMap(() => ctx.dispatch(new ShowMessage({
          message: 'Stopped program.',
          action: 'Close'
        })))
      )
  }

  @Action(GetConfig)
  getConfig(ctx: StateContext<KilnState>, { config }: GetConfig) {
    const state = ctx.getState();
    return ctx.setState({
      ...state,
      zones: config.zones,
      dashboard: {
        targetMetrics: config.chart.targetMetrics,
        temperatureMetrics: config.chart.temperatureMetrics
      }
    });
  }
}
