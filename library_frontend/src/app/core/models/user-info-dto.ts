import {FileInfoDto} from './file-info-dto';

export interface UserInfoDto {
  id: number;
  username: string | null;
  enabled: boolean | null;
  authorities: string[] | null;
  identityPhotoFile: FileInfoDto | null;
}
