import {Component, inject} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  loginUsernameSpecialCharacterValidator,
  registerIdentityCardPhotoValidator
} from '../../functions/validator-helper';
import {AuthenticationService} from '../../services/authentication.service';
import {isLoading} from '../../../app.component';
import {Router} from '@angular/router';
import {LoginInfoDto} from '../../models/login-info-dto';
import {RegisterInfoDto} from '../../models/register-info-dto';
import {RegisterService} from '../../services/register.service';

@Component({
  selector: 'app-login-page',
  imports: [
    NgClass,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {
  _activeSectionId: string | null = 'section-register';
  _uploadedFileName: string | null = null;

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(30), loginUsernameSpecialCharacterValidator()]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(30)]),
  });
  registerForm = new FormGroup({
    username: new FormControl('Cosminel2000', [Validators.required, Validators.minLength(8), Validators.maxLength(30), loginUsernameSpecialCharacterValidator()]),
    password: new FormControl('Cosminel2000', [Validators.required, Validators.minLength(8), Validators.maxLength(30)]),
    phoneNumber: new FormControl('0753690515', [Validators.required, Validators.pattern('\\d{10}')]),
    identityCardPhoto: new FormControl('', [Validators.required, registerIdentityCardPhotoValidator()])
  })

  constructor(
    private authService: AuthenticationService,
    private registerService: RegisterService,
    private router: Router
  ) {

  }

  setPageSectionActive(sectionId: string)  {
    switch(sectionId) {
      case 'section-login':
      case 'section-register':
        this._activeSectionId = sectionId;
        break;
      default:
        this._activeSectionId = null;
        console.warn('Unknown sectionId: ' + sectionId);
    }
  }

  handleLoginFormSubmit() {
    var me = this;

    if (me.loginForm.invalid) {

      console.warn('Invalid login form');

      return;

    }

    let loginInfoDto: LoginInfoDto = {
      username: me.loginForm.value.username,
      password: me.loginForm.value.password,
    }

    isLoading.set(true);
    me.authService.login(loginInfoDto).subscribe(
      {
        next(value) {

          console.log(value);
          isLoading.set(false);

          me.router.navigate(["/"]);

        },
        error(err)  {

          console.error(err);

          isLoading.set(false);

        },
      }
    );

  }

  handleRegisterFormSubmit() {
    var me = this;

    if (me.registerForm.invalid) {

      console.warn('Invalid register form');

      return;
    }

    const reader = new FileReader();

    reader.onload = function(e) {
      if (null != e && null != e.target) {
        const fileBase64 = e.target.result as string;
        readerOnLoadCallback(fileBase64);
      } else {
        console.warn('An error occurred while reading the file...');
      }
    }

    const identityCardPhoto = me.registerForm.get("identityCardPhoto");
    let fileContent: File | null = null;
    if (null != identityCardPhoto && null != identityCardPhoto.value) {
      fileContent = identityCardPhoto.getRawValue();

      if (null != fileContent) {
        isLoading.set(true);
        reader.readAsDataURL(fileContent);
      } else {
        console.warn('An error occurred while getting the file data...');
      }
    } else {
      console.warn('No file selected or file content is empty.');
    }

    let readerOnLoadCallback = function(result: string | null) {

      if (
        ! me.registerForm.invalid
        && null != fileContent
        && null != result
      ) {

        let registerInfoDto: RegisterInfoDto = {
          username: me.registerForm.value.username,
          password: me.registerForm.value.password,
          phoneNumber: me.registerForm.value.phoneNumber,

          fileName: fileContent.name,
          fileSize: fileContent.size,
          fileType: fileContent.type,
          fileData: result
        }

        me.registerService.register(registerInfoDto).subscribe(
          {
            next(value) {
              console.log('account registered successfully');

              isLoading.set(false);
            },
            error(err) {

              console.error(err);

              isLoading.set(false);

            },
          }
        )

      } else {

        isLoading.set(false);
        console.warn('An error occurred while trying to register the user...');

      }

    }
  }

  triggerFileInput() {
    const fileInput = document.getElementById('register_identityCardPhoto') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  onFileChange(event: any) {
    var me = this;
    const file = event.target.files[0];

    console.log(file);

    if (file) {
      me._uploadedFileName = file.name;
      me.registerForm.get('identityCardPhoto')?.setValue(file);
    }
  }
}
