import {UserInfoDto} from './user-info-dto';

export interface PageOfUserInfoDto{
  total: number;
  ls: UserInfoDto[];
}
