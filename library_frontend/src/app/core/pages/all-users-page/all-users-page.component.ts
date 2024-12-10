import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {UserInfoDto} from '../../models/user-info-dto';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-all-users-page',
  host: {
    class: 'routed-sub-component',
  },
  imports: [
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './all-users-page.component.html',
  styleUrl: './all-users-page.component.css'
})
export class AllUsersPageComponent implements OnInit {

  users: UserInfoDto[] = [];
  filterUserStatus: string = 'any';
  loading: boolean = true;

  isFilterActiveEnabled: boolean = false;
  isFilterActiveDisabled: boolean = false;
  isFilterActiveAny: boolean = true; //activated, by default

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {

  }

  enableUser(id: number) {
    console.log('#todo enable user id: ', id);
  }

  handleOnChangeFilterUserStatusEnabledCheckbox(event: any) {
    var me = this;

    if (event && event.target && event.target.value) {
      me.router.navigate(['dashboard', 'users'], { queryParams: { userStatus: 'enabled'} });
    }
  }

  handleOnChangeFilterUserStatusDisabledCheckbox(event: any) {
    var me = this;

    if (event && event.target && event.target.value) {
      me.router.navigate(['dashboard', 'users'], { queryParams: { userStatus: 'disabled'} });
    }
  }

  handleOnChangeFilterUserStatusAnyCheckbox(event: any) {
    var me = this;

    if (event && event.target && event.target.value) {
      me.router.navigate(['dashboard', 'users'], { queryParams: { userStatus: 'any'} });
    }
  }

  handleDownloadUserIdentityPhotoFile(id: number) {
    console.log('#todo downloading user identity photo file...');
  }

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {
      let filterUserStatus = params['userStatus'];


      let url = `http://localhost:9922/users`;
      if (null != filterUserStatus) {

        me.isFilterActiveEnabled = false;
        me.isFilterActiveDisabled = false;
        me.isFilterActiveAny = false;

        switch (filterUserStatus) {
          case 'enabled':
            me.isFilterActiveEnabled = true;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          case 'disabled':
            me.isFilterActiveDisabled = true;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          case 'any':
            me.isFilterActiveAny = true;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          default:
            console.warn('Unknown filterUserStatus: ' + filterUserStatus);
            me.isFilterActiveAny = true; //mark it active, as it is the default
            break;
        }
      }
      me.http.get<UserInfoDto[]>(url).subscribe(
        {
          next(value: UserInfoDto[]) {
            console.log(value);

            me.users = value;

            me.loading = false;
          },
          error(error) {
            console.log(error);

            me.loading = false;
          },
        }
      )
    })
  }
}
