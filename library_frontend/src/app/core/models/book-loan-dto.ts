import {BookWrapperInfoDto} from './book-wrapper-info-dto';

export interface BookLoanDto {
  id: number,
  loanedOn: string,
  loanExpireOn: string,
  returnedOn: string,
  loanFineAmount: number,
  loanStatus: string,
  bookWrapper: BookWrapperInfoDto,
  user: string
}
