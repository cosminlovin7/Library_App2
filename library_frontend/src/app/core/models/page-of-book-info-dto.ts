import {BookInfoDto} from './book-info-dto';

export interface PageOfBookInfoDto {
  total: number;
  ls: BookInfoDto[];
}
