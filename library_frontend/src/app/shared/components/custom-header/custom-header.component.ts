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
    let me = this;

    me.router.navigate(['dashboard/explore']);
  }

  handleProfileClick() {
    let me = this;

    if (null != me.authenticationService.getUserId()) {
      //@Force refresh... not best in town
      me.router.navigate(['/']).then(() => {
        me.router.navigate(['dashboard/users', me.authenticationService.getUserId()]);
      })
    }
  }

  handleAllUsersClick() {
    let me = this;

    me.router.navigate(['dashboard/users']);
  }

  handleInventoryClick() {
    let me = this;

    me.router.navigate(['dashboard/inventory']);
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
