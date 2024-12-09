import {FileInfoDto} from './file-info-dto';

export interface UserInfoDto {
  username: string | null;
  enabled: boolean | null;
  authorities: string[] | null;
  identityPhotoFile: FileInfoDto | null;
}
