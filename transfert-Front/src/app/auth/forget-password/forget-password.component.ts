import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';
import { NotificationService } from '../../_helpers/services/notification.service';


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

  private authService = inject(AuthService);
  private notifService = inject(NotificationService);


  constructor()
  {
    this.passwordForm = this.fb.group({
      phoneOrEmail: ['99999']
    })

  }

  send() {
    const data = this.passwordForm.value;
    console.log('📩 Données envoyées:', data);

    this.authService.forgotPswd(data).subscribe({
      next: (res) => {
        console.log('✅ Réponse forgot password:', res);
        // tu peux rediriger vers la page de code
        this.router.navigateByUrl('/code2');
        this.notifService.success('Code envoyé avec succès ! Vérifiez votre email.');
        localStorage.setItem('phoneOrEmail', data.phoneOrEmail);
      },
      error: (err) => {
        console.error('❌ Erreur API forgot password:', err);
      },
    });
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }


}
