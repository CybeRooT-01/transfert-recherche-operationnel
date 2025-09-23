import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-forget-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './forget-password.component.html',
  styleUrl: './forget-password.component.css'
})
export class ForgetPasswordComponent {


  passwordForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);

  constructor()
  {
    this.passwordForm = this.fb.group({
      telephone: ['783845870'],
      password: ['elzondao']
    })

  }

  login()
  {
    this.router.navigateByUrl("/code2")
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }

  onForgotPassword()
  {
    this.router.navigateByUrl("/forget-password")
  }

}
