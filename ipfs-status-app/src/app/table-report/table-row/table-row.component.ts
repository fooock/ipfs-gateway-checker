import {Component, Input, OnInit} from '@angular/core';
import {Gateway} from '../gateway';

@Component({
  selector: 'app-table-row',
  templateUrl: './table-row.component.html',
  styleUrls: ['./table-row.component.css']
})
export class TableRowComponent implements OnInit {

  @Input() gateway: Gateway;

  ngOnInit() {
  }

}
