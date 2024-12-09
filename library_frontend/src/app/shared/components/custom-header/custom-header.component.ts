import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../../core/services/authentication.service';

@Component({
  selector: 'app-custom-header',
  imports: [],
  templateUrl: './custom-header.component.html',
  styleUrl: './custom-header.component.css'
})
export class CustomHeaderComponent {

  constructor(private router: Router, protected authenticationService: AuthenticationService) {

  }

  handleExploreClick() {
    //#todo
    console.log('#todo');
  }

  handleAllUsersClick() {
    let me = this;

    me.router.navigate(['dashboard/users']);
  }

  handleInventoryClick() {
    //#todo
    console.log('#todo');
  }

  handleLogoutClick() {
    let me = this;

    let success = me.authenticationService.logout();

    if (true === success) {
      me.router.navigate(['login']);
    } else {
      //#todo what?
    }
  }
}
