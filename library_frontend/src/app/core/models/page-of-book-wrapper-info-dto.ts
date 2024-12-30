import {BookWrapperInfoDto} from './book-wrapper-info-dto';

export interface PageOfBookWrapperInfoDto {
  total: number;
  ls: BookWrapperInfoDto[];
}
