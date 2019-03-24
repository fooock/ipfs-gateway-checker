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
  private gatewaysMap: Map<string, Gateway>;
  private gateways: Array<Gateway>;
  private currentTime: number;

  constructor(private ws: RxStompService, private service: GatewayService) {
    this.gatewaysMap = new Map();
  }

  ngOnInit(): void {
    // First request use gateway service
    this.service.getAllGateways()
      .subscribe((gateways: Array<Gateway>) => {
        this.addToMap(gateways);
      }, (error => {
        console.error(error);
      }));

    // Register web socket watcher to receive updates in real time
    this.topicSubscription = this.ws.watch('/topic/report')
      .subscribe((msg: Message) => {
        const gateway: Gateway = JSON.parse(msg.body);
        this.gatewaysMap.set(gateway.name, gateway);
        this.updateGateways();
      });

    // Update every second
    setInterval(() => {
      this.currentTime = new Date().getTime();
    }, 1000);
  }

  private addToMap(gateways: Array<Gateway>): void {
    gateways.forEach(gateway => {
      this.gatewaysMap.set(gateway.name, gateway);
    });
    this.updateGateways();
  }

  private updateGateways(): void {
    this.gateways = Array.from(this.gatewaysMap.values());
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }

  get getGateways(): Array<Gateway> {
    return this.gateways;
  }

  public getLastUpdate(lastUpdate: number): string {
    const millis = (this.currentTime - lastUpdate);
    const seconds = Math.floor(millis / 1000);
    if (seconds > 60) {
      return `>${Math.floor(seconds / 60)} min.`;
    }
    return `${seconds} sec.`;
  }
}
