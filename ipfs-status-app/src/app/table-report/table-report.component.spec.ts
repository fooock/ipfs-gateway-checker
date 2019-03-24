import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TableReportComponent} from './table-report.component';

describe('TableReportComponent', () => {
  let component: TableReportComponent;
  let fixture: ComponentFixture<TableReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TableReportComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
