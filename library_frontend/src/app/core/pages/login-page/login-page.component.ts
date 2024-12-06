import { Component } from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  loginUsernameSpecialCharacterValidator,
  registerIdentityCardPhotoValidator
} from '../../functions/validator-helper';

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
    username: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), loginUsernameSpecialCharacterValidator()]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
  });
  registerForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20), loginUsernameSpecialCharacterValidator()]),
    password: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]),
    phoneNumber: new FormControl('', [Validators.required, Validators.pattern('\\d{10}')]),
    identityCardPhoto: new FormControl('', [Validators.required, registerIdentityCardPhotoValidator()])
  })

  constructor() {

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
    console.log(this.loginForm.value)
  }

  handleRegisterFormSubmit() {
    console.log(this.registerForm.value)
  }

  triggerFileInput() {
    const fileInput = document.getElementById('register_identityCardPhoto') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this._uploadedFileName = file.name;
      this.registerForm.get('identityCardPhoto')?.setValue(file);
    }
  }
}
