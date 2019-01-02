import {StartComponent} from './start/start.component';
import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {Picture} from './start/picture/picture.component';

const appRoutes: Routes = [
  {path: '', component: StartComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    Picture
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(
      appRoutes,
    ),
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
