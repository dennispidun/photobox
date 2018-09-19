import {StartComponent} from './start/start.component';
import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

const appRoutes: Routes = [
  {path: '', component: StartComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    StartComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(
      appRoutes,
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
