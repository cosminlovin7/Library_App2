import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {loginUsernameSpecialCharacterValidator} from '../../../core/functions/validator-helper';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-username-dialog',
  imports: [
    MatButton,
    MatDialogActions,
    MatLabel,
    MatFormField,
    ReactiveFormsModule,
    MatDialogContent,
    MatDialogTitle,
    MatInput,
    NgIf
  ],
  templateUrl: './username-dialog.component.html',
  styleUrl: './username-dialog.component.css'
})
export class UsernameDialogComponent {
  usernameForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<UsernameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { username: string },
    private fb: FormBuilder
  ) {
    this.usernameForm = this.fb.group({
      username: [data.username || '', [Validators.required, Validators.minLength(8), Validators.maxLength(30), loginUsernameSpecialCharacterValidator()]]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close(this.usernameForm.value.username);
  }
}
