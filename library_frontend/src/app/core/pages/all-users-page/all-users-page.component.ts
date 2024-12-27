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
  ],
  styleUrl: './all-users-page.component.css'
})
export class AllUsersPageComponent implements OnInit, AfterViewInit {

  users: UserInfoDto[] = [];
  filterUserStatus: string = 'any';
  loading: boolean = true;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {

  }

  applyFilter(): void {
    console.log('apply filter clicked...');
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

  handleOnChangeFilterUserStatusCheckbox(event: any) {
    var me = this;

    if (event && event.value) {
      me.filterUserStatus = event.value;
      me.router.navigate(['dashboard', 'users'], { queryParams: { userStatus: event.value} });
    }
  }

  handleDownloadUserIdentityPhotoFile(id: number) {
    console.log('#todo downloading user identity photo file...');
  }

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
  data = [
    {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
    {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
    {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
    {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
    {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
    {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
    {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
    {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
    {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
    {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
    {position: 11, name: 'Sodium', weight: 22.9897, symbol: 'Na'},
    {position: 12, name: 'Magnesium', weight: 24.305, symbol: 'Mg'},
    {position: 13, name: 'Aluminum', weight: 26.9815, symbol: 'Al'},
    {position: 14, name: 'Silicon', weight: 28.0855, symbol: 'Si'},
    {position: 15, name: 'Phosphorus', weight: 30.9738, symbol: 'P'},
    {position: 16, name: 'Sulfur', weight: 32.065, symbol: 'S'},
    {position: 17, name: 'Chlorine', weight: 35.453, symbol: 'Cl'},
    {position: 18, name: 'Argon', weight: 39.948, symbol: 'Ar'},
    {position: 19, name: 'Potassium', weight: 39.0983, symbol: 'K'},
    {position: 20, name: 'Calcium', weight: 40.078, symbol: 'Ca'},
    // Add more rows as needed
  ];
  dataSource = new MatTableDataSource(this.data);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {
      let filterUserStatus = params['userStatus'];


      let url = `http://localhost:9922/users`;
      if (null != filterUserStatus) {

        switch (filterUserStatus) {
          case 'enabled':
            me.filterUserStatus = filterUserStatus;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          case 'disabled':
            me.filterUserStatus = filterUserStatus;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          case 'any':
            me.filterUserStatus = filterUserStatus;
            url = url + "?userStatus=" + filterUserStatus;
            break;
          default:
            console.warn('Unknown filterUserStatus: ' + filterUserStatus);
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

  ngAfterViewInit() {
    let me = this;

    me.dataSource.paginator = me.paginator;
  }
}

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 11, name: 'Sodium', weight: 22.9897, symbol: 'Na'},
  {position: 12, name: 'Magnesium', weight: 24.305, symbol: 'Mg'},
  {position: 13, name: 'Aluminum', weight: 26.9815, symbol: 'Al'},
  {position: 14, name: 'Silicon', weight: 28.0855, symbol: 'Si'},
  {position: 15, name: 'Phosphorus', weight: 30.9738, symbol: 'P'},
  {position: 16, name: 'Sulfur', weight: 32.065, symbol: 'S'},
  {position: 17, name: 'Chlorine', weight: 35.453, symbol: 'Cl'},
  {position: 18, name: 'Argon', weight: 39.948, symbol: 'Ar'},
  {position: 19, name: 'Potassium', weight: 39.0983, symbol: 'K'},
  {position: 20, name: 'Calcium', weight: 40.078, symbol: 'Ca'},
];
