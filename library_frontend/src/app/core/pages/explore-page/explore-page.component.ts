import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
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
import {NgForOf, NgIf} from '@angular/common';
import {BookWrapperInfoDto} from '../../models/book-wrapper-info-dto';
import {BookCollectionDto} from '../../models/book-collection-dto';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';
import {PageOfBookWrapperInfoDto} from '../../models/page-of-book-wrapper-info-dto';
import {isLoading} from '../../../app.component';

@Component({
  selector: 'app-explore-page',
  imports: [
    FormsModule,
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
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    MatHeaderCellDef
  ],
  templateUrl: './explore-page.component.html',
  styleUrl: './explore-page.component.css'
})
export class ExplorePageComponent implements OnInit, AfterViewInit {

  DEFAULT_BOOK_STOCK_PAGE = 0;
  DEFAULT_BOOK_STOCK_PAGE_SIZE = 5;

  book_stock_page = this.DEFAULT_BOOK_STOCK_PAGE;
  book_stock_pageSize = this.DEFAULT_BOOK_STOCK_PAGE_SIZE;
  book_stock_total: number = 0;
  book_stock_ls: BookWrapperInfoDto[] = [];
  book_stock_displayedColumns: string[] = ['id', 'availableQuantity', 'title', 'author', 'collection', 'editure', 'isbn', 'yearOfPublication', 'actions'];
  book_stock_dataSource: MatTableDataSource<BookWrapperInfoDto> = new MatTableDataSource(this.book_stock_ls);
  book_stock_select_book_categories_ls: BookCollectionDto[] = [];
  @ViewChild(MatPaginator) book_stock_paginator!: MatPaginator;
  bookStockFiltersForm = new FormGroup({
    title: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    author: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    editure: new FormControl('', [Validators.minLength(2), Validators.maxLength(30)]),
    collection: new FormControl('', [Validators.minLength(0), Validators.maxLength(30)]),
    yearOfPublication: new FormControl('', [Validators.pattern('\\d{4}')])
  })
  book_stock_selectedBook: BookWrapperInfoDto | null = null;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private snackBar: MatSnackBar) {

  }

  ngOnInit() {
    let me = this;

    me.route.queryParams.subscribe(params => {

      let _page = null;
      let _pageSize = null;

      let _book_stock_queryParams = {
        title: null,
        author: null,
        editure: null,
        collection: null,
        yearOfPublication: null,
      };

      _page = null != params['page'] ? params['page'] : me.DEFAULT_BOOK_STOCK_PAGE;
      _pageSize = null != params['pageSize'] ? params['pageSize'] : me.DEFAULT_BOOK_STOCK_PAGE_SIZE;

      _book_stock_queryParams = {
        title: null != params['title'] && '' != params['title'] ? params['title'] : null,
        author: null != params['author'] && '' != params['author'] ? params['author'] : null,
        editure: null != params['editure'] ? params['editure'] : null,
        collection: null != params['collection'] ? params['collection'] : null,
        yearOfPublication: null != params['yearOfPublication'] ? params['yearOfPublication'] : null,
      }

      if (
        null != _page
        && null != _pageSize
      ) {
        me.book_stock_page = _page;
        me.book_stock_pageSize = _pageSize;

        me.bookStockFiltersForm.get('title')?.setValue(_book_stock_queryParams.title);
        me.bookStockFiltersForm.get('author')?.setValue(_book_stock_queryParams.author);
        me.bookStockFiltersForm.get('editure')?.setValue(_book_stock_queryParams.editure);
        me.bookStockFiltersForm.get('collection')?.setValue(_book_stock_queryParams.collection);
        me.bookStockFiltersForm.get('yearOfPublication')?.setValue(_book_stock_queryParams.yearOfPublication);
      }

      let url = `http://localhost:9922/book-wrappers?page=${me.book_stock_page}&pageSize=${me.book_stock_pageSize}`;

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

      let promiseLs = [];
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

              // isLoading.set(false);
              // me.loading = false;

              resolve();
            },
            error(error) {
              console.log(error);

              // isLoading.set(false);
              // me.loading = false;

              reject();
            },
          }
        )
      }));

      promiseLs.push(new Promise<void>((resolve, reject) => {
        me.http.get<BookCollectionDto[]>(`http://localhost:9922/book-collections`).subscribe(
          {
            next(value: BookCollectionDto[]) {

              me.book_stock_select_book_categories_ls = value;

              // isLoading.set(false);
              // me.loading = false;

              resolve();
            },
            error(error) {
              console.log(error);

              // isLoading.set(false);
              // me.loading = false;

              reject();
            },
          }
        )
      }));

      isLoading.set(true);
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

    });
  }

  ngAfterViewInit() {
    let me = this;

    me.book_stock_dataSource.paginator = me.book_stock_paginator;
  }

  handleBookStockFiltersFormSubmit() {
    var me = this;

    if (me.bookStockFiltersForm.invalid) {

      console.warn('Invalid filters form');

      return;
    }

    me.reloadPage();
  }

  handleBookStockFiltersFormReset() {
    var me = this;

    me.bookStockFiltersForm.get('title')?.setValue('');
    me.bookStockFiltersForm.get('author')?.setValue('');
    me.bookStockFiltersForm.get('editure')?.setValue('');
    me.bookStockFiltersForm.get('collection')?.setValue('');
    me.bookStockFiltersForm.get('yearOfPublication')?.setValue('');

    me.reloadPage();
  }

  handleBookStockPaginatorChange(event: any) {
    let me = this;

    if (null != event) {
      me.book_stock_page = event.pageIndex;
      me.book_stock_pageSize = event.pageSize;

      me.reloadPage();
    }
  }

  handleOnBookStockRowClick(bookRow: any) {
    let me = this;

    if (null != bookRow) {
      me.book_stock_selectedBook = bookRow;
    }
  }

  reloadPage() {
    let me = this;

    let __title = me.bookStockFiltersForm.get("title")?.getRawValue();
    let __author = me.bookStockFiltersForm.get("author")?.getRawValue();
    let __editure = me.bookStockFiltersForm.get("editure")?.getRawValue();
    let __collection = me.bookStockFiltersForm.get("collection")?.getRawValue();
    let __yearOfPublication = me.bookStockFiltersForm.get("yearOfPublication")?.getRawValue();

    let _queryParams = {
      page: me.book_stock_page,
      pageSize: me.book_stock_pageSize,

      ...(__title != null && __title != '' && { title: __title }),
      ...(__author != null && __author != '' && { author: __author }),
      ...(__editure != null && __editure != '' && { editure: __editure }),
      ...(__collection != null && __collection != '' && { collection: __collection }),
      ...(__yearOfPublication != null && __yearOfPublication != '' && { yearOfPublication: __yearOfPublication }),
    };

    console.log(_queryParams);

    me.router.navigate(['dashboard', 'explore'], { queryParams: _queryParams });
  }
}
