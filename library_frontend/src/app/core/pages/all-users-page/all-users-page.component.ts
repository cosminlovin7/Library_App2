import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-all-users-page',
  imports: [],
  templateUrl: './all-users-page.component.html',
  styleUrl: './all-users-page.component.css'
})
export class AllUsersPageComponent {

  constructor(private http: HttpClient, private router: Router) {
    http.get('http://localhost:9922/users').subscribe(
      {
        next: (value) => { console.log(value); },
        error: (error) => { console.log(error); },
      }
    )
  }

}
