import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {


  loginForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);


  constructor()
  {
    this.loginForm = this.fb.group({
      phoneNumber: ['11111'],
      password: ['password']
    })
  }

  login() {
    console.log((this.loginForm.value));

    this.authService.login(this.loginForm.value).subscribe({
      next: (res:any) => {
        console.log('✅ Login réussi', res);
        localStorage.setItem('loginInProgress', 'true');
        localStorage.setItem('token', res.token);
        localStorage.setItem('user', JSON.stringify(res));
        this.router.navigateByUrl("/code")
      },
      error: (err) => {
        console.error('❌ Erreur de login', err);
      }
    });
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
