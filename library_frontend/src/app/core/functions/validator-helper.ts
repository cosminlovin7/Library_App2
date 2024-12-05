import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function loginUsernameSpecialCharacterValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (null == value) {
      return null;
    }

    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]+/.test(value);

    console.log(hasSpecialChar);
    return hasSpecialChar ? { specialCharacter: true } : null;
  }
}

export function registerIdentityCardPhotoValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    console.log(control);
    return null;
  }
}
