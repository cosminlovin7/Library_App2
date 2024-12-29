export interface RegisterInfoDto {
  username: string | undefined | null;
  password: string | undefined | null;
  email: string | undefined | null;
  phoneNumber: string | undefined | null;

  fileName: string | undefined | null;
  fileSize: number | undefined | null;
  fileType: string | undefined | null;
  fileData: string | undefined | null;
}
