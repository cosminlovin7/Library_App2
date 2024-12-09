import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RegisterInfoDto} from '../models/register-info-dto';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(registerInfoDto: RegisterInfoDto) {
    let me = this;

    return me.http.post("http://localhost:9922/register", registerInfoDto);
  }
}
