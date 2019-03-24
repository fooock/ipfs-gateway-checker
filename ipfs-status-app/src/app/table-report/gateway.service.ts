import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Gateway} from './gateway';

@Injectable()
export class GatewayService {
  constructor(private client: HttpClient) {
  }

  public getAllGateways(): Observable<Array<Gateway>> {
    return this.client.get<Array<Gateway>>('assets/gateways.json');
  }
}
