
export module Snackbar {
  export class ShowMessage {
    static readonly type = '[Snackbar] Show Message';
    constructor(public message: SnackbarMessage) { }
  }
}

export interface SnackbarMessage {
  action: string
  message: string;
}
