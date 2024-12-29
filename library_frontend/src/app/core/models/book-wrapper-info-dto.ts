import {BookInfoDto} from './book-info-dto';

export interface BookWrapperInfoDto {
  id: number;
  book: BookInfoDto;
  quantity: number;
  availableQuantity: number;
}
