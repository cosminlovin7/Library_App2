import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
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
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-phone-number-dialog',
  imports: [
    FormsModule,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './phone-number-dialog.component.html',
  styleUrl: './phone-number-dialog.component.css'
})
export class PhoneNumberDialogComponent {
  phoneNumberForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<PhoneNumberDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { phoneNumber: string },
    private fb: FormBuilder
  ) {
    this.phoneNumberForm = this.fb.group({
      phoneNumber: [data.phoneNumber || '', [Validators.required, Validators.pattern('\\d{10}')]]
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    this.dialogRef.close(this.phoneNumberForm.value.phoneNumber);
  }
}
