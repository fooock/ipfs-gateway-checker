import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {ServiceCommon} from './common/service-common';
import {TableReportComponent} from './table-report/table-report.component';
import {GatewayService} from './table-report/gateway.service';
import {HttpClientModule} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    TableReportComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: ServiceCommon.getStompConfig()
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    },
    GatewayService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
