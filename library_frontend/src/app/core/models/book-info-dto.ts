import {BookCollectionDto} from './book-collection-dto';

export interface BookInfoDto {
  id: number,
  title: string,
  author: string,
  editure: string,
  collection: BookCollectionDto,
  edition: number,
  isbn: string,
  translator: string,
  pageNr: number,
  bookFormat: string,
  coverType: string,
  yearOfPublication: string
}
