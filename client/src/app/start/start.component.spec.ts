import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {StartComponent} from './start.component';
import {MatIconModule} from '@angular/material';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('StartComponent', () => {
  let httpMock: HttpTestingController;
  let component: StartComponent;
  let fixture: ComponentFixture<StartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        StartComponent,
      ],
      imports: [
        MatIconModule,
        HttpClientTestingModule,
      ]
    })
      .compileComponents();
    httpMock = TestBed.get(HttpTestingController);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', function () {
    expect(component).toBeTruthy();
  });
});
