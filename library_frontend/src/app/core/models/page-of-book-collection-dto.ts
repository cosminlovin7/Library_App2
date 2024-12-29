import {BookCollectionDto} from './book-collection-dto';

export interface PageOfBookCollectionDto {
  total: number;
  ls: BookCollectionDto[];
}
