import {Environment} from "./environment.type";

export const environment: Environment = {
  production: false,
  kiln: {
    httpApi: 'http://localhost:8080'
    // httpApi: 'http://192.168.1.157:8080'
  }
};
