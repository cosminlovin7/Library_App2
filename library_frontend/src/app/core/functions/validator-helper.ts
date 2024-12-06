import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function loginUsernameSpecialCharacterValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (null == value) {
      return null;
    }

    if ('' == value) {
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

    if (null == value) {
      return null;
    }

    if ('' == value) {
      return null;
    }

    const fileName = (function() {
      let out = null;

      try {
        let splittedName = value.name.split('.');

        if (splittedName.length > 1) {
          out = '';
          //@NOTE: Skip the last index, it should be the extension...
          for (var i = 0; i < splittedName.length - 1; i++) {
            out = out + splittedName[i];
          }
        } else if (splittedName.length === 1) {
          out = splittedName[0];
        } else {
          //noop
        }

      } catch (e) {
        console.warn('Cannot parse filename');
      }

      return out;
    })();

    if (null == fileName) {
      return {
        invalidName: true
      }
    }

    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]+/.test(fileName);
    if (true === hasSpecialChar) {
      return {
        specialCharacter: true
      }
    }

    const fileSize = value.size;
    if (fileSize >= 5 * 1024 * 1024) {
      return {
        sizeTooBig: true
      }
    }

    const fileType = value.type;
    let isFileTypeValid = false;

    switch (fileType) {
      case 'image/png':
      case 'image/jpg':
      case 'image/jpeg':
      case 'application/pdf':
        isFileTypeValid = true;
        break;
      default:
        console.warn('Unknown file type: ' + fileType);
    }

    if (false === isFileTypeValid) {
      return {
        invalidFileType: true
      }
    }

    return null;
  }
}
