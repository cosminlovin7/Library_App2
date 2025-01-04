import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserInfoDto} from '../../models/user-info-dto';
import {isLoading} from '../../../app.component';
import {HttpClient} from '@angular/common/http';
import {MatSnackBar} from '@angular/material/snack-bar';
import {NgForOf, NgIf} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow, MatRowDef, MatTable, MatTableDataSource
} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {BookWrapperInfoDto} from '../../models/book-wrapper-info-dto';
import {BookCollectionDto} from '../../models/book-collection-dto';
import {PageOfBookInfoDto} from '../../models/page-of-book-info-dto';
import {BookLoanDto} from '../../models/book-loan-dto';
import {PageOfBookWrapperInfoDto} from '../../models/page-of-book-wrapper-info-dto';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatInput, MatSuffix} from '@angular/material/input';
import {MatIcon} from '@angular/material/icon';
import {UsernameDialogComponent} from '../../../shared/components/username-dialog/username-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {EmailDialogComponent} from '../../../shared/components/email-dialog/email-dialog.component';
import {PhoneNumberDialogComponent} from '../../../shared/components/phone-number-dialog/phone-number-dialog.component';
import {AuthenticationService} from '../../services/authentication.service';

@Component({
  selector: 'app-user-details-page',
  imports: [
    NgIf,
    FormsModule,
    NgForOf,
    MatButton,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
    ReactiveFormsModule,
    MatDatepickerInput,
    MatInput,
    MatDatepicker,
    MatDatepickerToggle,
    MatIcon,
    MatSuffix
  ],
  templateUrl: './user-details-page.component.html',
  styleUrl: './user-details-page.component.css'
})
export class UserDetailsPageComponent implements OnInit, AfterViewInit {

  DEFAULT_TAB_INDEX = 0;

  DEFAULT_BOOK_STOCK_PAGE = 0;
  DEFAULT_BOOK_STOCK_PAGE_SIZE = 5;

  userId!: string;
  userInfo: UserInfoDto | null = null;
  activeTabIndex: number = 0;

  book_loan_ls: BookLoanDto[] = [];
  book_loan_displayedColumns: string[] = ['id', 'loanStatus', 'loanedOn', 'loanExpireOn', 'returnedOn', 'title', 'author', 'collection', 'isbn', 'loanFineAmount', 'actions'];
  book_loan_dataSource: MatTableDataSource<BookLoanDto> = new MatTableDataSource(this.book_loan_ls);

  constructor(
    protected authenticationService: AuthenticationService,
    private dialog: MatDialog,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  bookLoanLoanStatusFilterForm = new FormGroup({
    opt_LOANED: new FormControl(false, []),
    opt_EXPIRED: new FormControl(false, []),
    opt_RETURNED: new FormControl(false, []),
  })

  book_stock_page = this.DEFAULT_BOOK_STOCK_PAGE;
  book_stock_pageSize = this.DEFAULT_BOOK_STOCK_PAGE_SIZE;
  book_stock_total: number = 0;
  book_stock_ls: BookWrapperInfoDto[] = [];
  book_stock_displayedColumns: string[] = ['id', 'quantity', 'availableQuantity', 'title', 'author', 'collection', 'editure', 'isbn', 'actions'];
  book_stock_dataSource: MatTableDataSource<BookWrapperInfoDto> = new MatTableDataSource(this.book_stock_ls);
  book_stock_select_book_categories_ls: BookCollectionDto[] = [];
  @ViewChild(MatPaginator) book_stock_paginator!: MatPaginator;
  bookStockFiltersForm = new FormGroup({
    title: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    author: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    editure: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    collection: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    yearOfPublication: new FormControl('', [Validators.pattern('\\d{4}')])
  })

  addBookLoan_currStep: number = 0;
  addBookLoan_step1SelectedRow: BookWrapperInfoDto | null = null;
  addBookLoan_step2LoanInfoForm = new FormGroup({
    loanedOn: new FormControl({value: new Date(), disabled: true}, [Validators.required, Validators.pattern('^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$\n')]),
    loanExpireOn: new FormControl('', [Validators.required, Validators.pattern('^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) (0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$\n')]),
  })

  setActiveTab(index: number) {
    let me = this;

    me.activeTabIndex = index;

    me.reloadPage();
  }

  markBookAsReturned(bookLoan: any) {
    let me = this;

    console.log(bookLoan);
    if (null != bookLoan) {
      let url = `http://localhost:9922/book-loans/${bookLoan.id}/end-book-loan`;

      isLoading.set(true);
      me.http.post<void>(url, null).subscribe(
        {
          next() {
            me.snackBar.open("Book loan ended successfully!", "Success", {
              duration: 2000,  // Duration in milliseconds (optional)
              verticalPosition: "top",
            });

            isLoading.set(false);

            me.refreshBookLoansData()
          },
          error(error) {
            console.log(error);

            me.snackBar.open("An error occurred while trying to end the book loan!", "Error", {
              duration: 2000,  // Duration in milliseconds (optional)
              verticalPosition: "top",
            });

            isLoading.set(false);
          },
        }
      )
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

  ngOnInit(): void {
    let me = this;

    me.userId = me.route.snapshot.paramMap.get('id')!;

    if (me.userId != me.authenticationService.getUserId()?.toString() && ! me.authenticationService.hasRoleAdmin()) {
      me.router.navigate(['/']);
    }

    if (null != me.userId) {
      let url = `http://localhost:9922/users/${me.userId}`;

      isLoading.set(true);
      me.http.get<UserInfoDto>(url).subscribe(
        {
          next(value: UserInfoDto) {
            me.userInfo = value;

            isLoading.set(false);
          },
          error(error) {
            console.log(error);

            isLoading.set(false);
          },
        }
      )
    }

    me.route.queryParams.subscribe(params => {
      me.activeTabIndex = null != params['tabIndex'] ? parseInt(params['tabIndex']) : me.DEFAULT_TAB_INDEX;

      switch (me.activeTabIndex) {
        case 0:
          if (null != params['loanStatus']) {
            let loanStatusLs = params['loanStatus'];

            let _opt_LOANED = false;
            let _opt_EXPIRED = false;
            let _opt_RETURNED = false;

            for (let i = 0; i < loanStatusLs.length; i++) {
              let loanStatus = loanStatusLs[i];

              if (loanStatus === 'LOANED') {
                _opt_LOANED = true;
              } else if (loanStatus === 'EXPIRED') {
                _opt_EXPIRED = true;
              } else if (loanStatus === 'RETURNED') {
                _opt_RETURNED = true;
              }
            }

            me.bookLoanLoanStatusFilterForm.get('opt_LOANED')?.setValue(_opt_LOANED);
            me.bookLoanLoanStatusFilterForm.get('opt_EXPIRED')?.setValue(_opt_EXPIRED);
            me.bookLoanLoanStatusFilterForm.get('opt_RETURNED')?.setValue(_opt_RETURNED);
          } else {
            //by default, all will be activated
            // me.bookLoanLoanStatusFilterForm.get('opt_LOANED')?.setValue(true);
            // me.bookLoanLoanStatusFilterForm.get('opt_EXPIRED')?.setValue(true);
            // me.bookLoanLoanStatusFilterForm.get('opt_RETURNED')?.setValue(true);
          }
          break;
        case 1:

          //@NOTE: Reset current step to 0
          me.addBookLoan_currStep = 0;
          me.addBookLoan_step1SelectedRow = null;

          me.addBookLoan_step2LoanInfoForm.get('loanedOn')?.setValue(new Date());
          me.addBookLoan_step2LoanInfoForm.get('loanExpireOn')?.setValue('');

          //@NOTE: Reset filters
          me.bookStockFiltersForm.get('title')?.setValue('');
          me.bookStockFiltersForm.get('author')?.setValue('');
          me.bookStockFiltersForm.get('editure')?.setValue('');
          me.bookStockFiltersForm.get('collection')?.setValue('');
          me.bookStockFiltersForm.get('yearOfPublication')?.setValue('');

          break;
        default:
          console.warn('Unknown tabIndex: ' + me.activeTabIndex);
          return;
          break;
      }


      switch (me.activeTabIndex) {
        case 0:
          me.refreshBookLoansData();
          break;
        case 1:
          let promiseLs = [];

          let url= `http://localhost:9922/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

          if (me.authenticationService.hasRoleAdmin()) {
            url = `http://localhost:9922/admin/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;
          }

          let __title = me.bookStockFiltersForm.get("title")?.getRawValue();
          let __author = me.bookStockFiltersForm.get("author")?.getRawValue();
          let __editure = me.bookStockFiltersForm.get("editure")?.getRawValue();
          let __collection = me.bookStockFiltersForm.get("collection")?.getRawValue();
          let __yearOfPublication = me.bookStockFiltersForm.get("yearOfPublication")?.getRawValue();

          if (null != __title) {
            url = url + "&title=" + __title;
          }
          if (null != __author) {
            url = url + "&author=" + __author;
          }
          if (null != __editure) {
            url = url + "&editure=" + __editure;
          }
          if (null != __collection) {
            url = url + "&collection=" + __collection;
          }
          if (null != __yearOfPublication) {
            url = url + "&yearOfPublication=" + __yearOfPublication;
          }

          promiseLs.push(new Promise<void>((resolve, reject) => {
            me.http.get<PageOfBookWrapperInfoDto>(url).subscribe(
              {
                next(value: PageOfBookWrapperInfoDto) {

                  console.log(value);

                  me.book_stock_ls = value.ls;
                  me.book_stock_total = value.total;

                  me.book_stock_dataSource = new MatTableDataSource(me.book_stock_ls);
                  me.book_stock_dataSource.paginator = me.book_stock_paginator;

                  setTimeout(function () {
                    me.book_stock_paginator.pageIndex = me.book_stock_page;
                    me.book_stock_paginator.length = me.book_stock_total;
                  }, 0);

                  resolve();
                },
                error(error) {
                  console.log(error);

                  reject();
                },
              }
            )
          }));

          url = `http://localhost:9922/book-collections`;
          promiseLs.push(new Promise<void>((resolve, reject) => {
            me.http.get<BookCollectionDto[]>(url).subscribe(
              {
                next(value: BookCollectionDto[]) {

                  console.log(value);
                  me.book_stock_select_book_categories_ls = value;

                  resolve();
                },
                error(error) {
                  console.log(error);

                  reject();
                },
              }
            )
          }));

          Promise.all(promiseLs).then(
            () => {
              console.log('Page loaded successfully');

              isLoading.set(false);
            }
          ).catch(
            () => {
              console.log('Error while loading the page');

              isLoading.set(false);
            }
          );

          break;
        default:
          console.warn('Unknown tabIndex: ' + me.activeTabIndex);
          return;
          break;
      }
    });
  }

  ngAfterViewInit() {
    let me = this;

    me.book_stock_dataSource.paginator = me.book_stock_paginator;
  }

  reloadPage() {
    let me = this;

    let _bookLoansQueryParams = null;
    let _addBookLoanQueryParams = null;

    console.log('reloading...');
    switch (me.activeTabIndex) {
      case 0:

        let __opt_LOANED = null, __opt_EXPIRED = null, __opt_RETURNED = null;

        __opt_LOANED = me.bookLoanLoanStatusFilterForm.get("opt_LOANED")?.getRawValue();
        __opt_EXPIRED = me.bookLoanLoanStatusFilterForm.get("opt_EXPIRED")?.getRawValue();
        __opt_RETURNED = me.bookLoanLoanStatusFilterForm.get("opt_RETURNED")?.getRawValue();

        let loanStatusLs = [];
        if (__opt_LOANED) {
          loanStatusLs.push('LOANED');
        }
        if (__opt_EXPIRED) {
          loanStatusLs.push('EXPIRED');
        }
        if (__opt_RETURNED) {
          loanStatusLs.push('RETURNED');
        }

        _bookLoansQueryParams = {
          tabIndex: me.activeTabIndex,

          ...((loanStatusLs.length > 0 && {loanStatus: loanStatusLs})),
        };
        break;
      case 1:
        _addBookLoanQueryParams = {
          tabIndex: me.activeTabIndex,
        }
        break;
      default:
        console.warn('Unknown tab index', me.activeTabIndex);
        return;
        break;
    }

    switch (me.activeTabIndex) {
      case 0:
        me.router.navigate(['dashboard', 'users', me.userId], { queryParams: _bookLoansQueryParams });
        break;
      case 1:
        me.router.navigate(['dashboard', 'users', me.userId], { queryParams: _addBookLoanQueryParams });
        break;
      default:
        console.warn('Unknown tab index', me.activeTabIndex);
        return;
        break;
    }
  }

  refreshUserInfoData() {
    let me = this;

    if (null != me.userId) {
      let url = `http://localhost:9922/users/${me.userId}`;

      isLoading.set(true);
      me.http.get<UserInfoDto>(url).subscribe(
        {
          next(value: UserInfoDto) {
            me.userInfo = value;

            isLoading.set(false);
          },
          error(error) {
            console.log(error);

            isLoading.set(false);
          },
        }
      )
    }
  }

  refreshBookLoansData() {
    let me = this;

    let url = null;
    let __opt_LOANED = null, __opt_EXPIRED = null, __opt_RETURNED = null;

    let _userId = me.userId;

    if (null != _userId) {
      url = `http://localhost:9922/users/${_userId}/book-loans`;

      __opt_LOANED = me.bookLoanLoanStatusFilterForm.get("opt_LOANED")?.getRawValue();
      __opt_EXPIRED = me.bookLoanLoanStatusFilterForm.get("opt_EXPIRED")?.getRawValue();
      __opt_RETURNED = me.bookLoanLoanStatusFilterForm.get("opt_RETURNED")?.getRawValue();

      let loanStatusLs = [];
      if (__opt_LOANED) {
        loanStatusLs.push('LOANED');
      }
      if (__opt_EXPIRED) {
        loanStatusLs.push('EXPIRED');
      }
      if (__opt_RETURNED) {
        loanStatusLs.push('RETURNED');
      }

      if (loanStatusLs.length > 0) {
        url = url + '?loanStatus=';

        for (let i = 0; i < loanStatusLs.length - 1; i++) {
          let loanStatus = loanStatusLs[i];

          url = url + loanStatus + ','
        }
        url = url + loanStatusLs[loanStatusLs.length - 1];
      }

      isLoading.set(true);
      me.http.get<BookLoanDto[]>(url).subscribe(
        {
          next(value: BookLoanDto[]) {

            me.book_loan_ls = value;
            me.book_loan_dataSource.data = me.book_loan_ls;

            console.log(value);
            isLoading.set(false);
          },
          error(error) {
            console.log(error);

            isLoading.set(false);
          },
        }
      )
    }
  }

  refreshBookStockData() {
    let me = this;

    let url = null;
    let __title = null, __author = null, __editure = null, __collection = null, __yearOfPublication = null;

    url = `http://localhost:9922/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

    if (me.authenticationService.hasRoleAdmin()) {
      url = `http://localhost:9922/admin/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;
    }

    __title = me.bookStockFiltersForm.get("title")?.getRawValue();
    __author = me.bookStockFiltersForm.get("author")?.getRawValue();
    __editure = me.bookStockFiltersForm.get("editure")?.getRawValue();
    __collection = me.bookStockFiltersForm.get("collection")?.getRawValue();
    __yearOfPublication = me.bookStockFiltersForm.get("yearOfPublication")?.getRawValue();

    if (null != __title) {
      url = url + "&title=" + __title;
    }
    if (null != __author) {
      url = url + "&author=" + __author;
    }
    if (null != __editure) {
      url = url + "&editure=" + __editure;
    }
    if (null != __collection) {
      url = url + "&collection=" + __collection;
    }
    if (null != __yearOfPublication) {
      url = url + "&yearOfPublication=" + __yearOfPublication;
    }

    isLoading.set(true);
    me.http.get<PageOfBookWrapperInfoDto>(url).subscribe(
      {
        next(value: PageOfBookWrapperInfoDto) {

          console.log(value);

          me.book_stock_ls = value.ls;
          me.book_stock_total = value.total;

          me.book_stock_dataSource.data = me.book_stock_ls;
          setTimeout(function() {
            me.book_stock_paginator.pageIndex = me.book_stock_page;
            me.book_stock_paginator.length = me.book_stock_total;
          }, 0);

          isLoading.set(false);
        },
        error(error) {
          console.log(error);

          isLoading.set(false);
        },
      }
    )
  }

  handleOnLoanStatusCheckboxChange(event: any) {
    let me = this;

    me.reloadPage();
  }

  handleBookStockFiltersFormSubmit() {
    var me = this;

    console.log('called2');
    if (me.bookStockFiltersForm.invalid) {

      console.warn('Invalid filters form');

      return;
    }

    me.refreshBookStockData();
  }

  handleBookStockFiltersFormReset() {
    var me = this;

    me.bookStockFiltersForm.get('title')?.setValue('');
    me.bookStockFiltersForm.get('author')?.setValue('');
    me.bookStockFiltersForm.get('editure')?.setValue('');
    me.bookStockFiltersForm.get('collection')?.setValue('');
    me.bookStockFiltersForm.get('yearOfPublication')?.setValue('');

    me.refreshBookStockData();
  }

  handleBookStockPaginatorChange(event: any) {
    var me = this;

    if (null != event) {
      me.book_stock_page = event.pageIndex;
      me.book_stock_pageSize = event.pageSize;

      me.refreshBookStockData();
    }
  }

  handleStep1OnBookStockRowClick(row: any) {
    let me = this;

    me.addBookLoan_step1SelectedRow = row;
  }

  handleOnClickAddBookLoanFooterButtonSubmit() {
    let me = this;

    if (me.addBookLoan_currStep === 2) {
      //all good
    } else {
      console.warn('Bad logic');
      return;
    }

    const loanedOn = me.addBookLoan_step2LoanInfoForm.get("loanedOn")?.getRawValue();
    const loanExpireOn = me.addBookLoan_step2LoanInfoForm.get("loanExpireOn")?.getRawValue();

    const _loanedOn = me.convertDateToDateTimeFormat(loanedOn);
    const _loanExpireOn = me.convertDateToDateTimeFormat(loanExpireOn);
    const _loanedBookWrapperId = me.addBookLoan_step1SelectedRow?.id;
    const _ownerUserId = me.userInfo?.id;

    let canContinue = true;
    if (null == _loanedOn) {
      canContinue = false;

      console.warn('Missing information about loanedOn');
    }
    if (null == _loanExpireOn) {
      canContinue = false;

      console.warn('Missing information about loanExpireOn');
    }
    if (null == _loanedBookWrapperId) {
      canContinue = false;

      console.warn('Missing information about loanedBookWrapperId');
    }
    if (null == _ownerUserId) {
      canContinue = false;

      console.warn('Missing information about ownerUserId');
    }

    if (! canContinue) {
      me.snackBar.open("An error occurred while trying to create a book loan!", "Error", {
        duration: 2000,  // Duration in milliseconds (optional)
        verticalPosition: "top",
      });

      return;
    }

    let bookLoanCreateCmd = {
      "loanedOn": _loanedOn,
      "loanExpireOn": _loanExpireOn,
      "loanedBookWrapperId": _loanedBookWrapperId,
      "ownerUserId": _ownerUserId,
    }

    let url = `http://localhost:9922/book-loans/create-book-loan`;
    isLoading.set(true);
    me.http.post<void>(url, bookLoanCreateCmd).subscribe({
      next() {

        me.snackBar.open("Book loan created successfully!", "Error", {
          duration: 2000,  // Duration in milliseconds (optional)
          verticalPosition: "top",
        });

        isLoading.set(false);

        me.activeTabIndex = 0;
        me.reloadPage();
      },
      error(error) {
        console.warn(error);

        me.snackBar.open("An error occurred while trying to create a book loan!", "Error", {
          duration: 2000,  // Duration in milliseconds (optional)
          verticalPosition: "top",
        });

        isLoading.set(false);
      }
    })
  }

  handleOnClickAddBookLoanFooterButtonNext() {
    let me = this;

    switch(me.addBookLoan_currStep) {
      case 0:
        if (null == me.addBookLoan_step1SelectedRow) {
          me.snackBar.open("Select a book to continue!", "Info", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });

          return;
        }

        me.addBookLoan_currStep++;
        break;
      case 1:
        // me.addBookLoan_currStep++;

        const loanedOn = me.addBookLoan_step2LoanInfoForm.get("loanedOn")?.getRawValue();
        const loanExpireOn = me.addBookLoan_step2LoanInfoForm.get("loanExpireOn")?.getRawValue();

        if (null == loanedOn || null == loanExpireOn) {
          me.snackBar.open("Select the date interval for book loan!", "Info", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });

          return;
        }

        const timeDifference = Math.abs(loanExpireOn.getTime() - loanedOn.getTime());

        const dayDifference = timeDifference / (1000 * 3600 * 24);

        if (dayDifference < 7) {
          me.snackBar.open("Interval is too small. A book should be loaned for at least 7 days!", "Info", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });

          return;
        }

        if (dayDifference > 30) {
          me.snackBar.open("Interval is too big. A book should be loaned for maximum 30 days!", "Info", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });

          return;
        }

        const _loanedOn = me.convertDateToDateTimeFormat(loanedOn);
        const _loanExpireOn = me.convertDateToDateTimeFormat(loanExpireOn);

        console.log({
          _loanedOn,
          _loanExpireOn,
        })

        me.addBookLoan_currStep++;
        break;
      default:
        console.warn('Unknown step number: ' + me.addBookLoan_currStep);
    }
  }

  handleOnClickAddBookLoanFooterButtonPrev() {
    let me = this;

    switch(me.addBookLoan_currStep) {
      case 1:
        me.addBookLoan_currStep--;
        break;
      case 2:
        me.addBookLoan_currStep--;
        break;
      default:
        console.warn('Unknown step number: ' + me.addBookLoan_currStep);
    }
  }

  convertDateToDateTimeFormat(date: Date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

    return formattedDateTime;
  }

  handleOnClickUserInfoEditUsername() {
    let me = this;

    const dialogRef = me.dialog.open(UsernameDialogComponent, {
      width: '400px',
      data: { username: me.userInfo?.username }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        if (me.userInfo) {
          let url = `http://localhost:9922/users/${me.userInfo.id}/update-username`

          isLoading.set(true);
          me.http.patch(url, { username: result }).subscribe({
            next(value: any) {

              me.snackBar.open("User updated successfully", "Info", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

              me.refreshUserInfoData();
            },
            error(error: any) {

              me.snackBar.open("An error occurred while trying to update the user!", "Error", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

            }
          })
        }

      }
    });
  }

  handleOnClickUserInfoEditEmail() {
    let me = this;

    const dialogRef = me.dialog.open(EmailDialogComponent, {
      width: '400px',
      data: { email: me.userInfo?.email }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        if (me.userInfo) {
          let url = `http://localhost:9922/users/${me.userInfo.id}/update-email`

          isLoading.set(true);
          me.http.patch(url, { email: result }).subscribe({
            next(value: any) {

              me.snackBar.open("User updated successfully", "Info", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

              me.refreshUserInfoData();
            },
            error(error: any) {

              me.snackBar.open("An error occurred while trying to update the user!", "Error", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

            }
          })
        }
      }
    });
  }

  handleOnClickUserInfoEditPhoneNumber() {
    let me = this;

    const dialogRef = me.dialog.open(PhoneNumberDialogComponent, {
      width: '400px',
      data: { phoneNumber: me.userInfo?.phoneNumber }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        if (me.userInfo) {
          let url = `http://localhost:9922/users/${me.userInfo.id}/update-phone-number`

          isLoading.set(true);
          me.http.patch(url, { phoneNumber: result }).subscribe({
            next(value: any) {

              me.snackBar.open("User updated successfully", "Info", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

              me.refreshUserInfoData();
            },
            error(error: any) {

              me.snackBar.open("An error occurred while trying to update the user!", "Error", {
                duration: 2000,  // Duration in milliseconds (optional)
                verticalPosition: "top",
              });

              isLoading.set(false);

            }
          })
        }

      }
    });
  }
}
