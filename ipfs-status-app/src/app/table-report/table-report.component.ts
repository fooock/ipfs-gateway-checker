import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Gateway} from './gateway';

@Component({
  selector: 'app-table-report',
  templateUrl: './table-report.component.html',
  styleUrls: ['./table-report.component.css']
})
export class TableReportComponent implements OnInit, OnDestroy {
  private topicSubscription: Subscription;

  constructor(private ws: RxStompService) {
  }

  ngOnInit(): void {
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
