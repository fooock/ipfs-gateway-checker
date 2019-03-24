import {environment} from '../../environments/environment';
import {InjectableRxStompConfig} from '@stomp/ng2-stompjs';

export class ServiceCommon {
  static getStompConfig(): InjectableRxStompConfig {
    return {
      brokerURL: environment.eventBaseUrl,
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 500
    };
  }
}
