import {BaseThemeOptions, ThemeOptions} from "./theme";

const palette = {
  primary: '#6200ee',
  success: '#60af20',
  info: '#0495ee',
  warning: '#ff9f05',
  danger: '#b00020',
};

const baseThemeOptions: BaseThemeOptions = {
  name: 'light-theme',
  base: 'light',
  variables: {
    fontMain: 'Roboto, sans-serif',
    fontSecondary: 'Roboto, sans-serif',

    bg: '#ffffff',
    bg2: '#f5f5f5',
    bg3: '#ebebeb',
    bg4: '#e0e0e0',

    border: '#ffffff',
    border2: '#f5f5f5',
    border3: '#ebebeb',
    border4: '#e0e0e0',
    border5: '#b3b3b3',

    fg: '#838383',
    fgHeading: '#1a2138',
    fgText: '#1a2138',
    fgHighlight: palette.primary,
    layoutBg: '#ebebeb',
    separator: '#ebebeb',

    primary: palette.primary,
    success: palette.success,
    info: palette.info,
    warning: palette.warning,
    danger: palette.danger,

    primaryLight: '#903df4',
    successLight: '#8fcf50',
    infoLight: '#40bbf4',
    warningLight: '#ffbe43',
    dangerLight: '#cf3341',
  },
}

const baseThemeVariables = baseThemeOptions.variables;

export const LIGHT_THEME: ThemeOptions = {
  name: baseThemeOptions.name,
  echarts: {
    bg: baseThemeVariables.bg,
    textColor: baseThemeVariables.fgText,
    axisLineColor: baseThemeVariables.fgText,
    splitLineColor: baseThemeVariables.separator,
    itemHoverShadowColor: 'rgba(0, 0, 0, 0.5)',
    tooltipBackgroundColor: baseThemeVariables.primary,
    areaOpacity: '0.7',
  }
}
