import {FileInfoMetaDto} from './file-info-meta-dto';

export interface FileInfoDto {
  meta: FileInfoMetaDto,
  base64FileContent: string;
}
