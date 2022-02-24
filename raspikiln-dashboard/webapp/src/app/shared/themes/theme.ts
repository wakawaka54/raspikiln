
export interface BaseThemeOptions {
  name: string;
  base: string;
  variables: {
    fontMain: string;
    fontSecondary: string;

    bg: string;
    bg2: string;
    bg3: string;
    bg4: string;

    border: string;
    border2: string;
    border3: string;
    border4: string;
    border5: string;

    fg: string;
    fgHeading: string;
    fgText: string;
    fgHighlight: string;
    layoutBg: string;
    separator: string;

    primary: string;
    success: string;
    info: string;
    warning: string;
    danger: string;

    primaryLight: string;
    successLight: string;
    infoLight: string;
    warningLight: string;
    dangerLight: string;
  };
}

export interface ThemeOptions {
  name: string;
  echarts: {
    bg: string;
    textColor: string;
    axisLineColor: string;
    splitLineColor: string;
    itemHoverShadowColor: string;
    tooltipBackgroundColor: string;
    areaOpacity: string;
  }
}
