import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Gateway} from './gateway';
import {GatewayService} from './gateway.service';

@Component({
  selector: 'app-table-report',
  templateUrl: './table-report.component.html',
  styleUrls: ['./table-report.component.css']
})
export class TableReportComponent implements OnInit, OnDestroy {
  private topicSubscription: Subscription;

  constructor(private ws: RxStompService, private service: GatewayService) {
  }

  ngOnInit(): void {
    this.service.getAllGateways()
      .subscribe((gateways: Array<Gateway>) => {
        console.log(gateways);
      }, (error => {
        console.error(error);
      }));
    this.topicSubscription = this.ws.watch('/topic/report')
      .subscribe((msg: Message) => {
        const gateway: Gateway = JSON.parse(msg.body);
        console.log(gateway);
      });
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }
}
