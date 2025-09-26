import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';
import { NotificationService } from '../../_helpers/services/notification.service';


export interface VerifyPinResponse {
  message: string;
  account: {
    accountNumber: string;
    balance: number;
    currency: string;
    subscriptionType: string;
    dailyLimit: number;
    monthlyLimit: number;
  };
}

@Component({
  selector: 'app-code',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './code.component.html',
  styleUrl: './code.component.css'
})
export class CodeComponent {

  codeForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private notifService = inject(NotificationService);


  constructor()
  {
    this.codeForm = this.fb.group({
      pin: ['00000'],
    })
  }

  valider() {
    const code = this.codeForm.value.pin;
    console.log(code);

    this.authService.verifyPin(code).subscribe({
      next: (res: VerifyPinResponse) => {
        console.log('Message:', res.message);
        console.log('Compte:', res.account);
        localStorage.setItem('account', JSON.stringify(res.account));
        this.router.navigateByUrl("/home");
        localStorage.removeItem('loginInProgress');
        this.notifService.success("Authentification réussie. Bienvenue !");
      },
      error: (err) => {
        console.error('Erreur PIN:', err);
        console.log(err.error.text)
        this.notifService.error("Échec de l'authentification. Veuillez réessayer.");
      }
    });
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }

}
