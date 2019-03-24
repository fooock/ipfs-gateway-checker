import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Gateway} from './gateway';
import {environment} from '../../environments/environment';

@Injectable()
export class GatewayService {
  baseUrl = environment.baseUrl;

  constructor(private client: HttpClient) {
  }

  public getAllGateways(): Observable<Array<Gateway>> {
    const url = this.baseUrl + '/gateway/all';
    return this.client.get<Array<Gateway>>(url);
  }
}
