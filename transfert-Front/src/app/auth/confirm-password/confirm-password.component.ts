import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-confirm-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './confirm-password.component.html',
  styleUrl: './confirm-password.component.css'
})
export class ConfirmPasswordComponent {


  changePasswordForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);

  constructor()
  {
    this.changePasswordForm = this.fb.group({
      newPassword: [null],
      confirmPassword: [null]
    })

  }

  onChangePassword()
  {
    this.router.navigateByUrl("/login")
  }


}
