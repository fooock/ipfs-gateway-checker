import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';

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
        console.log(msg.body);
      });
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }
}
