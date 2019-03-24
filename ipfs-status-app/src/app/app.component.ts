import {Component, OnDestroy, OnInit} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
  private topicSubscription: Subscription;

  constructor(private ws: RxStompService) {
  }

  ngOnInit(): void {
    this.topicSubscription = this.ws.watch('/topic/report')
      .subscribe((msg: Message) => {
        console.log(msg);
      });
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }
}
