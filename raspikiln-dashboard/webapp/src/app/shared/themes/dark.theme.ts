import {BaseThemeOptions, ThemeOptions} from "./theme";

const palette = {
  primary: '#e91d63',
  success: '#60af20',
  info: '#0495ee',
  warning: '#ff9f05',
  danger: '#b00020',
};

const baseThemeOptions: BaseThemeOptions = {
  name: 'dark-theme',
  base: 'dark',
  variables: {
    fontMain: 'Roboto, sans-serif',
    fontSecondary: 'Roboto, sans-serif',

    bg: '#383838',
    bg2: '#292929',
    bg3: '#1f1f1f',
    bg4: '#141414',

    border: '#383838',
    border2: '#292929',
    border3: '#1f1f1f',
    border4: '#141414',
    border5: '#141414',

    fg: '#808080',
    fgHeading: '#ffffff',
    fgText: '#ffffff',
    fgHighlight: palette.primary,
    layoutBg: '#1f1f1f',
    separator: '#1f1f1f',

    primary: palette.primary,
    success: palette.success,
    info: palette.info,
    warning: palette.warning,
    danger: palette.danger,

    primaryLight: '#f24681',
    successLight: '#8fcf50',
    infoLight: '#40bbf4',
    warningLight: '#ffbe43',
    dangerLight: '#cf3341',
  }
}

const baseThemeVariables = baseThemeOptions.variables;

export const DARK_THEME: ThemeOptions = {
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
