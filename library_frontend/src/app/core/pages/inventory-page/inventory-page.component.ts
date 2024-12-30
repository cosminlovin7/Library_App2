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
import {BookWrapperInfoDto} from '../../models/book-wrapper-info-dto';
import {PageOfBookInfoDto} from '../../models/page-of-book-info-dto';

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
    MatButton,
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

  book_stock_page = this.DEFAULT_BOOK_STOCK_PAGE;
  book_stock_pageSize = this.DEFAULT_BOOK_STOCK_PAGE_SIZE;
  book_stock_total: number = 0;
  book_stock_ls: BookWrapperInfoDto[] = [];
  book_stock_displayedColumns: string[] = ['id', 'quantity', 'availableQuantity', 'title', 'author', 'collection', 'editure', 'isbn', 'actions'];
  book_stock_dataSource: MatTableDataSource<BookWrapperInfoDto> = new MatTableDataSource(this.book_stock_ls);
  @ViewChild(MatPaginator) book_stock_paginator!: MatPaginator;
  bookStockFiltersForm = new FormGroup({
    title: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    author: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    editure: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    collection: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    yearOfPublication: new FormControl('', [Validators.pattern('\\d{4}')])
  })

  // book_categories_page = this.DEFAULT_BOOK_CATEGORIES_PAGE;
  // book_categories_pageSize = this.DEFAULT_BOOK_CATEGORIES_PAGE_SIZE;
  // book_categories_total: number = 0;
  book_categories_ls: BookCollectionDto[] = [];
  book_categories_displayedColumns: string[] = ['id', 'name', 'actions'];
  book_categories_dataSource: MatTableDataSource<BookCollectionDto> = new MatTableDataSource(this.book_categories_ls);
  // @ViewChild(MatPaginator) book_categories_paginator!: MatPaginator;

  books_page = this.DEFAULT_BOOKS_PAGE;
  books_pageSize = this.DEFAULT_BOOKS_PAGE_SIZE;
  books_total: number = 0;
  books_ls: BookInfoDto[] = [];
  books_displayedColumns: string[] = ['id', 'title', 'author', 'collection', 'coverType', 'bookFormat', 'edition', 'editure', 'isbn', 'pageNr', 'translator', 'yearOfPublication', 'actions'];
  books_dataSource: MatTableDataSource<BookInfoDto> = new MatTableDataSource(this.books_ls);
  @ViewChild(MatPaginator) books_paginator!: MatPaginator;
  booksFiltersForm = new FormGroup({
    title: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    author: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    editure: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    collection: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    yearOfPublication: new FormControl('', [Validators.pattern('\\d{4}')])
  })

  bookCollectionCreateForm = new FormGroup({
    name: new FormControl('', [Validators.minLength(0), Validators.maxLength(50), Validators.pattern('^[^!@#$%^&*{}|<>]+$')])
  })

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private snackBar: MatSnackBar) {

  }

  handleBookCollectionCreateFormSubmit() {
    let me = this;

    if (me.bookCollectionCreateForm.invalid) {

      console.warn('Invalid book collection create form');

      return;
    }

    const name = me.bookCollectionCreateForm.get('name')?.getRawValue();

    let url = `http://localhost:9922/admin/book-collections/create-collection`;
    isLoading.set(true);
    me.loading = true;
    me.http.post(url, { name: name }).subscribe(
      {
        next() {
          isLoading.set(false);
          me.loading = false;

          me.snackBar.open("Book collection created successfully!", "", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });

          me.refreshBookCollectionData();
        },
        error(error) {
          console.log(error);

          isLoading.set(false);
          me.loading = false;

          me.snackBar.open("An error occurred while trying to delete the book collection!", "Error", {
            duration: 2000,  // Duration in milliseconds (optional)
            verticalPosition: "top",
          });
        }
      }
    )
  }

  handleBookStockFiltersFormSubmit() {
    var me = this;

    if (me.bookStockFiltersForm.invalid) {

      console.warn('Invalid filters form');

      return;
    }

    me.reloadPage();
  }

  handleBooksFiltersFormSubmit() {
    var me = this;

    if (me.booksFiltersForm.invalid) {

      console.warn('Invalid filters form');

      return;
    }

    me.reloadPage();
  }

  setActiveTab(index: number): void {
    let me = this;

    console.log('activeTabIndex', index);
    me.activeTabIndex = index;

    me.reloadPage();
  }

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {

      me.loading = true;
      isLoading.set(true);

      let _tabIndex = null != params['tabIndex'] ? parseInt(params['tabIndex']) : me.DEFAULT_TAB_INDEX;

      let _page = null;
      let _pageSize = null;

      let _book_stock_queryParams = {
        title: null,
        author: null,
        editure: null,
        collection: null,
        yearOfPublication: null,
      };
      let _books_queryParams = {
        title: null,
        author: null,
        editure: null,
        collection: null,
        yearOfPublication: null,
      };

      switch (_tabIndex) {
        case 0:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOK_STOCK_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOK_STOCK_PAGE_SIZE;

          _book_stock_queryParams = {
            title: null != params['title'] && '' != params['title'] ? params['title'] : null,
            author: null != params['author'] && '' != params['author'] ? params['author'] : null,
            editure: null != params['editure'] ? params['editure'] : null,
            collection: null != params['collection'] ? params['collection'] : null,
            yearOfPublication: null != params['yearOfPublication'] ? params['yearOfPublication'] : null,
          }
          break;
        case 1:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOK_CATEGORIES_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOK_CATEGORIES_PAGE_SIZE;
          break;
        case 2:
          _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOKS_PAGE;
          _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOKS_PAGE_SIZE;

          _books_queryParams = {
            title: null != params['title'] && '' != params['title'] ? params['title'] : null,
            author: null != params['author'] && '' != params['author'] ? params['author'] : null,
            editure: null != params['editure'] ? params['editure'] : null,
            collection: null != params['collection'] ? params['collection'] : null,
            yearOfPublication: null != params['yearOfPublication'] ? params['yearOfPublication'] : null,
          }
          break;
        default:
          console.warn('Unknown tabIndex:', _tabIndex);
          return;
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
            me.book_stock_page = _page;
            me.book_stock_pageSize = _pageSize;

            me.bookStockFiltersForm.get('title')?.setValue(_book_stock_queryParams.title);
            me.bookStockFiltersForm.get('author')?.setValue(_book_stock_queryParams.author);
            me.bookStockFiltersForm.get('editure')?.setValue(_book_stock_queryParams.editure);
            me.bookStockFiltersForm.get('collection')?.setValue(_book_stock_queryParams.collection);
            me.bookStockFiltersForm.get('yearOfPublication')?.setValue(_book_stock_queryParams.yearOfPublication);

            break;
          case 1:
            // me.book_categories_page = _page;
            // me.book_categories_pageSize = _pageSize;

            break;
          case 2:
            me.books_page = _page;
            me.books_pageSize = _pageSize;

            me.booksFiltersForm.get('title')?.setValue(_books_queryParams.title);
            me.booksFiltersForm.get('author')?.setValue(_books_queryParams.author);
            me.booksFiltersForm.get('editure')?.setValue(_books_queryParams.editure);
            me.booksFiltersForm.get('collection')?.setValue(_books_queryParams.collection);
            me.booksFiltersForm.get('yearOfPublication')?.setValue(_books_queryParams.yearOfPublication);

            break;
          default:
            console.warn('Unknown tabIndex: ', _tabIndex);
            return;
            break;
        }
      } else {
        return;
      }

      let url = null;
      let __title = null, __author = null, __editure = null, __collection = null, __yearOfPublication = null;
      switch (_tabIndex) {
        case 0:
          url = `http://localhost:9922/admin/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

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

          break;
        case 1:
          url = `http://localhost:9922/book-collections`;
          break;
        case 2:
          url = `http://localhost:9922/admin/books?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

          __title = me.booksFiltersForm.get("title")?.getRawValue();
          __author = me.booksFiltersForm.get("author")?.getRawValue();
          __editure = me.booksFiltersForm.get("editure")?.getRawValue();
          __collection = me.booksFiltersForm.get("collection")?.getRawValue();
          __yearOfPublication = me.booksFiltersForm.get("yearOfPublication")?.getRawValue();

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

          break;
        default:
          console.warn('Unknown tabIndex:', _tabIndex);
          return;
          break;
      }

      if (null != url) {
        switch (_tabIndex) {
          case 0:
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
            me.http.get<BookCollectionDto[]>(url).subscribe(
              {
                next(value: BookCollectionDto[]) {

                  me.book_categories_ls = value;
                  // me.book_categories_total = value.total;

                  me.book_categories_dataSource.data = me.book_categories_ls;
                  // setTimeout(function() {
                  //   me.book_categories_paginator.pageIndex = me.book_categories_page;
                  //   me.book_categories_paginator.length = me.book_categories_total;
                  // }, 0);

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
            me.http.get<PageOfBookInfoDto>(url).subscribe(
              {
                next(value: PageOfBookInfoDto) {

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
          default:
            console.warn('Unknown tabIndex:', _tabIndex);
            return;
            break;
        }
      } else {
        return;
      }
    })
  }

  ngAfterViewInit() {
    let me = this;

    me.book_stock_dataSource.paginator = me.book_stock_paginator;
    // me.book_categories_dataSource.paginator = me.book_categories_paginator;
    me.books_dataSource.paginator = me.books_paginator;

    // me.book_stock_paginator.page.subscribe(event => {
    //   if (
    //     null != event
    //     && null != event.pageIndex
    //     && null != event.pageSize
    //   ) {
    //     me.book_stock_page = event.pageIndex;
    //     me.book_stock_pageSize = event.pageSize;
    //
    //     console.log(event);
    //
    //     me.reloadPage();
    //   }
    // })
    //
    // me.book_categories_paginator.page.subscribe(event => {
    //   if (
    //     null != event
    //     && null != event.pageIndex
    //     && null != event.pageSize
    //   ) {
    //     me.book_categories_page = event.pageIndex; // Current page index
    //     me.book_categories_pageSize = event.pageSize; // Selected page size
    //
    //     console.log(event);
    //
    //     me.reloadPage();
    //   }
    // })
    //
    // me.books_paginator.page.subscribe(event => {
    //   if (
    //     null != event
    //     && null != event.pageIndex
    //     && null != event.pageSize
    //   ) {
    //     me.books_page = event.pageIndex; // Current page index
    //     me.books_pageSize = event.pageSize; // Selected page size
    //
    //     console.log(event);
    //
    //     me.reloadPage();
    //   }
    // });
  }

  handleBookStockPaginatorChange(event: any) {
    var me = this;

    if (null != event) {
      me.book_stock_page = event.pageIndex;
      me.book_stock_pageSize = event.pageSize;

      me.reloadPage();
    }
  }

  handleBooksPaginatorChange(event: any) {
    var me = this;

    if (null != event) {
      me.books_page = event.pageIndex;
      me.books_pageSize = event.pageSize;

      me.reloadPage();
    }
  }

  handleOnClickButtonDeleteBookCollection(bookCollection: any) {
    var me = this;

    console.log(bookCollection);

    let url = `http://localhost:9922/admin/book-collections/delete-collection/${bookCollection.id}`;

    isLoading.set(true);
    me.loading = true;
    me.http.delete<void>(url).subscribe({
      next() {
        isLoading.set(false);
        me.loading = false;

        me.snackBar.open("Book collection deleted successfully!", "", {
          duration: 2000,  // Duration in milliseconds (optional)
          verticalPosition: "top",
        });

        me.refreshBookCollectionData();
      },
      error(error) {
        console.log(error);

        isLoading.set(false);
        me.loading = false;

        me.snackBar.open("An error occurred while trying to delete the book collection!", "Error", {
          duration: 2000,  // Duration in milliseconds (optional)
          verticalPosition: "top",
        });
      },
    })

  }

  reloadPage() {
    let me = this;

    let _queryParams = null;
    let __title = null, __author = null, __editure = null, __collection = null, __yearOfPublication = null;

    console.log('reloading...');
    switch (me.activeTabIndex) {
      case 0:

        __title = me.bookStockFiltersForm.get("title")?.getRawValue();
        __author = me.bookStockFiltersForm.get("author")?.getRawValue();
        __editure = me.bookStockFiltersForm.get("editure")?.getRawValue();
        __collection = me.bookStockFiltersForm.get("collection")?.getRawValue();
        __yearOfPublication = me.bookStockFiltersForm.get("yearOfPublication")?.getRawValue();

        _queryParams = {
          tabIndex: me.activeTabIndex,
          page: me.book_stock_page,
          pageSize: me.book_stock_pageSize,

          ...(__title != null && __title != '' && { title: __title }),
          ...(__author != null && __author != '' && { author: __author }),
          ...(__editure != null && __editure != '' && { editure: __editure }),
          ...(__collection != null && __collection != '' && { collection: __collection }),
          ...(__yearOfPublication != null && __yearOfPublication != '' && { yearOfPublication: __yearOfPublication }),
        };

        console.log(_queryParams);

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      case 1:
        _queryParams = {
          tabIndex: me.activeTabIndex,
          // page: me.book_categories_page,
          // pageSize: me.book_categories_pageSize,
        };

        console.log('ajung aici');

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      case 2:

        __title = me.booksFiltersForm.get("title")?.getRawValue();
        __author = me.booksFiltersForm.get("author")?.getRawValue();
        __editure = me.booksFiltersForm.get("editure")?.getRawValue();
        __collection = me.booksFiltersForm.get("collection")?.getRawValue();
        __yearOfPublication = me.booksFiltersForm.get("yearOfPublication")?.getRawValue();

        _queryParams = {
          tabIndex: me.activeTabIndex,
          page: me.books_page,
          pageSize: me.books_pageSize,

          ...(__title != null && __title != '' && { title: __title }),
          ...(__author != null && __author != '' && { author: __author }),
          ...(__editure != null && __editure != '' && { editure: __editure }),
          ...(__collection != null && __collection != '' && { collection: __collection }),
          ...(__yearOfPublication != null && __yearOfPublication != '' && { yearOfPublication: __yearOfPublication }),
        };

        me.router.navigate(['dashboard', 'inventory'], { queryParams: _queryParams });
        break;
      default:
        console.warn('Unknown tab index', me.activeTabIndex);
        return;
        break;
    }
  }

  refreshBookStockData() {
    let me = this;

    let url = null;
    let __title = null, __author = null, __editure = null, __collection = null, __yearOfPublication = null;

    url = `http://localhost:9922/admin/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

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
    me.loading = true;
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
          me.loading = false;
        },
        error(error) {
          console.log(error);

          isLoading.set(false);
          me.loading = false;
        },
      }
    )
  }

  refreshBookCollectionData() {
    let me = this;

    let url = `http://localhost:9922/book-collections`;

    isLoading.set(true);
    me.loading = true;
    me.http.get<BookCollectionDto[]>(url).subscribe(
      {
        next(value: BookCollectionDto[]) {

          me.book_categories_ls = value;
          // me.book_categories_total = value.total;

          me.book_categories_dataSource.data = me.book_categories_ls;
          // setTimeout(function() {
          //   me.book_categories_paginator.pageIndex = me.book_categories_page;
          //   me.book_categories_paginator.length = me.book_categories_total;
          // }, 0);

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
  }

  refreshBooksData() {
    let me = this;

    let url = null;
    let __title = null, __author = null, __editure = null, __collection = null, __yearOfPublication = null;

    url = `http://localhost:9922/admin/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

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
    me.loading = true;
    me.http.get<PageOfBookInfoDto>(url).subscribe(
      {
        next(value: PageOfBookInfoDto) {

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
  }
}
