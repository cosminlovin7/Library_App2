import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {UserInfoDto} from '../../models/user-info-dto';
import {
  MatCell, MatCellDef, MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {FormsModule} from '@angular/forms';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {PageOfUserInfoDto} from '../../models/page-of-user-info-dto';
import {isLoading} from '../../../app.component';
import {MatProgressBar} from '@angular/material/progress-bar';
import {NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-all-users-page',
  host: {
    class: 'routed-sub-component',
  },
  templateUrl: './all-users-page.component.html',
  imports: [
    MatTable,
    MatHeaderCellDef,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatPaginator,
    MatHeaderRowDef,
    MatColumnDef,
    MatCellDef,
    MatRow,
    MatRowDef,
    FormsModule,
    MatRadioGroup,
    MatRadioButton,
    MatProgressBar,
    NgIf,
    MatButton,
  ],
  styleUrl: './all-users-page.component.css'
})
export class AllUsersPageComponent implements OnInit, AfterViewInit {

  DEFAULT_PAGE: number = 0;
  DEFAULT_PAGE_SIZE: number = 5;
  total: number = 0;
  users: UserInfoDto[] = [];
  filterUserStatus: string = 'any';
  page: number = this.DEFAULT_PAGE;
  pageSize: number = this.DEFAULT_PAGE_SIZE;
  loading: boolean = true;

  displayedColumns: string[] = ['id', 'username', 'email', 'phoneNumber', 'enabled', 'authorities', 'actions'];
  dataSource: MatTableDataSource<UserInfoDto> = new MatTableDataSource(this.users);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private snackBar: MatSnackBar) {

  }

  handleOnChangeFilterUserStatusCheckbox(event: any) {
    var me = this;

    if (event && event.value) {
      me.filterUserStatus = event.value;

      me.router.navigate(['dashboard', 'users'], {
        queryParams: {
          userStatus: me.filterUserStatus,
          page: me.page,
          pageSize: me.pageSize
        }
      });
    }
  }

  handleOnClickButtonDownloadIdentityPhotoFile(element: any) {
    let me = this;

    if (null != element) {
      let identityPhotoFile = element.identityPhotoFile;

      if (null != identityPhotoFile) {

        let base64FileContent = identityPhotoFile.base64FileContent;
        let meta = identityPhotoFile.meta;

        if (null != base64FileContent && null != meta) {

          let base64Data = "data:" + meta.fileType + ';base64,' + base64FileContent;

          const downloadLink = document.createElement("a")
          downloadLink.href = base64Data
          downloadLink.download = meta.fileName;
          downloadLink.click()
        }
      }
    }
  }

  handleOnClickButtonEnableUser(element: any) {
    let me = this;

    if (null != element && false == element.enabled) {
      me.loading = true;

      me.http.post("http://localhost:9922/admin/users/" + element.id + "/enable", null).subscribe(
        {
          next() {
            me.snackBar.open("User enabled successfully!", "Success", {
              duration: 2000,  // Duration in milliseconds (optional)
              verticalPosition: "top",
            });

            me.loading = false;
          },
          error(error) {
            console.log(error);

            me.snackBar.open("An error occurred while trying to enable the user!", "Error", {
              duration: 2000,
              verticalPosition: "top",
            });

            me.loading = false;
          },
        }
      )
    }
  }

  handleOnClickButtonViewUserInfo(userInfo: any) {
    let me = this;

    if (null != userInfo) {
      me.router.navigate(['dashboard', 'users', userInfo.id]);
    }
  }

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {

      me.loading = true;
      isLoading.set(true);

      let _filterUserStatus = params['userStatus'];
      let _page = null != params['page'] ? params['page'] : me.DEFAULT_PAGE;
      let _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_PAGE_SIZE;

      let hasAnyParams = false;

      let url = `http://localhost:9922/users`;
      if (null != _filterUserStatus) {

        hasAnyParams = true;

        switch (_filterUserStatus) {
          case 'enabled':
            me.filterUserStatus = _filterUserStatus;
            url = url + "?userStatus=" + _filterUserStatus;

            break;
          case 'disabled':
            me.filterUserStatus = _filterUserStatus;
            url = url + "?userStatus=" + _filterUserStatus;
            break;
          case 'any':
            me.filterUserStatus = _filterUserStatus;
            url = url + "?userStatus=" + _filterUserStatus;
            break;
          default:
            console.warn('Unknown filterUserStatus: ' + _filterUserStatus);
            me.filterUserStatus = 'any';
            hasAnyParams = false;
            break;
        }

      } else {
        me.filterUserStatus = 'any';
      }

      if (null != _page && null != _pageSize) {
        me.page = _page;
        me.pageSize = _pageSize;
      } else {
        me.page = me.DEFAULT_PAGE;
        me.pageSize = me.DEFAULT_PAGE_SIZE;
      }

      if (hasAnyParams) {
        url = url + "&";
      } else {
        hasAnyParams = true;
        url = url + "?";
      }

      url = url + "page=" + me.page + "&pageSize=" + me.pageSize;

      me.http.get<PageOfUserInfoDto>(url).subscribe(
        {
          next(value: PageOfUserInfoDto) {
            me.users = value.ls;
            me.total = value.total;

            me.dataSource.data = me.users;
            setTimeout(function() {
              me.paginator.pageIndex = me.page;
              me.paginator.length = me.total;
            }, 0);

            isLoading.set(false);
            me.loading = false;
          },
          error(error) {
            console.log(error);

            isLoading.set(false);
            me.loading = false;
          },
        }
      )
    })
  }

  ngAfterViewInit() {
    let me = this;

    me.dataSource.paginator = me.paginator;

    me.paginator.page.subscribe(event => {
      if (
        null != event
        && null != event.pageIndex
        && null != event.pageSize
      ) {
        me.page = event.pageIndex; // Current page index
        me.pageSize = event.pageSize; // Selected page size

        me.router.navigate(['dashboard', 'users'], {
          queryParams: {
            userStatus: me.filterUserStatus,
            page: me.page,
            pageSize: me.pageSize
          }
        });
      }
    });
  }
}
