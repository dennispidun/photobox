import { Component, Input } from '@angular/core';

@Component({
  selector: 'picture',
  templateUrl: './picture.component.html'
})
export class Picture {
  @Input() picture: any;

  constructor() {
  }
}
