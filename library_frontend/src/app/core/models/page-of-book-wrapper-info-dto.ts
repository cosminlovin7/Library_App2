import {BookInfoDto} from './book-info-dto';

export interface PageOfBookWrapperInfoDto {
  total: number;
  ls: BookInfoDto[];
}
