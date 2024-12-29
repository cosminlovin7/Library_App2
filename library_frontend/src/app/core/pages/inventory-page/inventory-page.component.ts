import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {NgIf} from '@angular/common';
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
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {isLoading} from '../../../app.component';
import {BookInfoDto} from '../../models/book-info-dto';
import {BookCollectionDto} from '../../models/book-collection-dto';
import {MatRadioButton, MatRadioGroup} from '@angular/material/radio';
import {MatFormField} from '@angular/material/form-field';
import {FormControl, FormGroup, FormGroupDirective, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatInput} from '@angular/material/input';
import {PageOfBookCollectionDto} from '../../models/page-of-book-collection-dto';
import {PageOfBookWrapperInfoDto} from '../../models/page-of-book-wrapper-info-dto';

@Component({
  selector: 'app-inventory-page',
  imports: [
    NgIf,
    MatCell,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatPaginator,
    MatRow,
    MatTable,
    ReactiveFormsModule,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRowDef,
    MatRowDef,
  ],
  templateUrl: './inventory-page.component.html',
  styleUrl: './inventory-page.component.css',
})
export class InventoryPageComponent implements OnInit, AfterViewInit {

  DEFAULT_TAB_INDEX = 0;

  DEFAULT_BOOKS_PAGE = 0;
  DEFAULT_BOOKS_PAGE_SIZE = 5;

  DEFAULT_BOOK_CATEGORIES_PAGE = 0;
  DEFAULT_BOOK_CATEGORIES_PAGE_SIZE = 5;

  DEFAULT_BOOK_STOCK_PAGE = 0;
  DEFAULT_BOOK_STOCK_PAGE_SIZE = 5;

  activeTabIndex: number = 0;
  loading: boolean = true;

  books_page = this.DEFAULT_BOOKS_PAGE;
  books_pageSize = this.DEFAULT_BOOKS_PAGE_SIZE;
  books_total: number = 0;
  books_ls: BookInfoDto[] = [];
  books_displayedColumns: string[] = ['id', 'title', 'author', 'collection', 'coverType', 'bookFormat', 'edition', 'editure', 'isbn', 'pageNr', 'translator', 'yearOfPublication', 'actions'];
  books_dataSource: MatTableDataSource<BookInfoDto> = new MatTableDataSource(this.books_ls);
  @ViewChild(MatPaginator) books_paginator!: MatPaginator;
  books_filters = {
    title: null,
    author: null,
    editure: null,
    collection: null,
    yearOfPublication: null,
  }

  book_categories_page = this.DEFAULT_BOOK_CATEGORIES_PAGE;
  book_categories_pageSize = this.DEFAULT_BOOK_CATEGORIES_PAGE_SIZE;
  book_categories_total: number = 0;
  book_categories_ls: BookCollectionDto[] = [];
  book_categories_displayedColumns: string[] = ['id', 'name', 'actions'];
  book_categories_dataSource: MatTableDataSource<BookCollectionDto> = new MatTableDataSource(this.book_categories_ls);
  @ViewChild(MatPaginator) book_categories_paginator!: MatPaginator;

  book_stock_page = this.DEFAULT_BOOK_STOCK_PAGE;
  book_stock_pageSize = this.DEFAULT_BOOK_STOCK_PAGE_SIZE;
  book_stock_total: number = 0;
  book_stock_ls: BookCollectionDto[] = [];
  book_stock_displayedColumns: string[] = ['id'];
  book_stock_dataSource: MatTableDataSource<BookCollectionDto> = new MatTableDataSource(this.book_stock_ls);
  @ViewChild(MatPaginator) book_stock_paginator!: MatPaginator;

  booksFiltersForm = new FormGroup({
    title: new FormControl('', [Validators.minLength(8), Validators.maxLength(30)]),
    author: new FormControl('', [Validators.minLength(8), Validators.maxLength(30)]),
    editure: new FormControl('', [Validators.minLength(8), Validators.maxLength(30)]),
    collection: new FormControl('', [Validators.minLength(8), Validators.maxLength(30)]),
    yearOfPublication: new FormControl('', [Validators.pattern('\\d{4}')])
  })

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private snackBar: MatSnackBar) {

  }

  handleBooksFiltersFormSubmit() {
    var me = this;

    if (me.booksFiltersForm.invalid) {

      console.warn('Invalid filters form');

      return;
    }

    const title = me.booksFiltersForm.get("title")?.getRawValue();
    const author = me.booksFiltersForm.get("author")?.getRawValue();
    const editure = me.booksFiltersForm.get("editure")?.getRawValue();
    const collection = me.booksFiltersForm.get("collection")?.getRawValue();
    const yearOfPublication = me.booksFiltersForm.get("yearOfPublication")?.getRawValue();

    me.books_filters = {
      title: title,
      author: author,
      editure: editure,
      collection: collection,
      yearOfPublication: yearOfPublication,
    }

    me.reloadPage();
  }

  setActiveTab(index: number): void {
    let me = this;

    console.log('activeTabIndex', index);
    me.activeTabIndex = index;

    let queryParams = null;

    me.reloadPage();
  }

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {

      me.loading = true;
      isLoading.set(true);

      let _tabIndex = null != params['page'] ? parseInt(params['tabIndex']) : me.DEFAULT_TAB_INDEX;

      let _page = null;
      let _pageSize = null;

      switch (_tabIndex) {
        case 0:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOKS_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOKS_PAGE_SIZE;
          break;
        case 1:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOK_CATEGORIES_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOK_CATEGORIES_PAGE_SIZE;
          break;
        case 2:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOK_STOCK_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOK_STOCK_PAGE_SIZE;
          break;
        default:
          console.warn('Unknown tabIndex:', _tabIndex);
          break;
      }

      if (
        null != _tabIndex
        && null != _page
        && null != _pageSize
      ) {
        me.activeTabIndex = _tabIndex;

        switch (_tabIndex) {
          case 0:
            me.books_page = _page;
            me.books_pageSize = _pageSize;
            break;
          case 1:
            me.book_categories_page = _page;
            me.book_categories_pageSize = _pageSize;
            break;
          case 2:
            me.book_stock_page = _page;
            me.book_stock_pageSize = _pageSize;
            break;
          default:
            console.warn('Unknown tabIndex: ', _tabIndex);
            break;
        }
      }

      let url = null;
      switch (_tabIndex) {
        case 0:
          url = `http://localhost:9922/admin/book-wrappers`;
          break;
        case 1:
          url = `http://localhost:9922/book-collections`;
          break;
        case 2:
          url = `http://localhost:9922/books`;
          break;
        default:
          console.warn('Unknown tabIndex:', _tabIndex);
          break;
      }

      if (null != url) {
        switch (_tabIndex) {
          case 0:
            me.http.get<PageOfBookWrapperInfoDto>(url).subscribe(
              {
                next(value: PageOfBookWrapperInfoDto) {

                  console.log(value);

                  me.books_ls = value.ls;
                  me.books_total = value.total;

                  me.books_dataSource.data = me.books_ls;
                  setTimeout(function() {
                    me.books_paginator.pageIndex = me.books_page;
                    me.books_paginator.length = me.books_total;
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
            break;
          case 1:
            me.http.get<PageOfBookCollectionDto>(url).subscribe(
              {
                next(value: PageOfBookCollectionDto) {

                  me.book_categories_ls = value.ls;
                  me.book_categories_total = value.total;

                  me.book_categories_dataSource.data = me.book_categories_ls;
                  setTimeout(function() {
                    me.book_categories_paginator.pageIndex = me.book_categories_page;
                    me.book_categories_paginator.length = me.book_categories_total;
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
            break;
          case 2:
            //todo
            break;
          default:
            break;
        }
      }
    })
  }

  ngAfterViewInit() {
    let me = this;

    me.books_dataSource.paginator = me.books_paginator;
    me.book_categories_dataSource.paginator = this.book_categories_paginator;
    me.book_stock_dataSource.paginator = this.book_stock_paginator;

    me.books_paginator.page.subscribe(event => {
      if (
        null != event
        && null != event.pageIndex
        && null != event.pageSize
      ) {
        me.books_page = event.pageIndex; // Current page index
        me.books_pageSize = event.pageSize; // Selected page size

        me.reloadPage();
      }
    });

    me.book_categories_paginator.page.subscribe(event => {
      if (
        null != event
        && null != event.pageIndex
        && null != event.pageSize
      ) {
        me.book_categories_page = event.pageIndex; // Current page index
        me.book_categories_pageSize = event.pageSize; // Selected page size

        me.reloadPage();
      }
    })
  }

  reloadPage() {
    let me = this;

    let _queryParams = null;

    switch (me.activeTabIndex) {
      case 0:
        _queryParams = {
          tabIndex: me.activeTabIndex,
          page: me.books_page,
          pageSize: me.books_pageSize,

          ...(me.books_filters.title != null && { title: me.books_filters.title }),
          ...(me.books_filters.author != null && { author: me.books_filters.author }),
          ...(me.books_filters.editure != null && { editure: me.books_filters.editure }),
          ...(me.books_filters.collection != null && { collection: me.books_filters.collection }),
          ...(me.books_filters.yearOfPublication != null && { yearOfPublication: me.books_filters.yearOfPublication }),
        };

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      case 1:
        _queryParams = {
          tabIndex: me.activeTabIndex,
          page: me.book_categories_page,
          pageSize: me.book_categories_pageSize,
        };

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      case 2:
        _queryParams = {
          tabIndex: me.activeTabIndex,
          page: me.book_stock_page,
          pageSize: me.book_stock_pageSize,
        };

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      default:
        console.warn('Unknown tab index', me.activeTabIndex);
        break;
    }
  }
}
