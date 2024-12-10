import {FileInfoMetaDto} from './file-info-meta-dto';

export interface FileInfoDto {
  id: number;
  meta: FileInfoMetaDto,
  base64FileContent: string;
}
