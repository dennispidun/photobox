import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Photo} from "./photo.model";

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit{
  photos: Photo[] = [];

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get<Photo>("/api/photos").subscribe((data) => {
      this.photos.push(data);
    })
  }


}
