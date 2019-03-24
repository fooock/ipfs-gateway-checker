import {Component, Input, OnInit} from '@angular/core';
import {Gateway} from '../gateway';

@Component({
  selector: 'app-table-row',
  templateUrl: './table-row.component.html',
  styleUrls: ['./table-row.component.css']
})
export class TableRowComponent implements OnInit {
  private currentTime: number;

  @Input() gateway: Gateway;

  ngOnInit() {
    setInterval(() => {
      this.currentTime = new Date().getTime();
    }, 1000);
  }

  public getLastUpdate(): string {
    const millis = (this.currentTime - this.gateway.lastUpdate);
    const seconds = Math.floor(millis / 1000);
    if (seconds > 60) {
      return `>${Math.floor(seconds / 60)} min.`;
    }
    return `${seconds} sec.`;
  }
}
