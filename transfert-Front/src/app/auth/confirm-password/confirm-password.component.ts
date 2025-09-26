import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';
import { NotificationService } from '../../_helpers/services/notification.service';


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

  private authService = inject(AuthService);
  private notifService = inject(NotificationService);


  constructor()
  {
    this.changePasswordForm = this.fb.group({
      newPassword: [""],
      confirmPassword: [""]
    })

  }

  onChangePassword()
  {
    const pswdData = this.changePasswordForm.value;
    const phoneOrEmail = localStorage.getItem('phoneOrEmail');
    const data = {
      phoneOrEmail: phoneOrEmail,
      newPassword: pswdData.newPassword
    };

    console.log('📩 Données envoyées:', data);

    this.authService.resetPswd(data).subscribe({
      next: (res) => {
        console.log('✅ Mot de passe modifié avec succès:', res);
        this.router.navigateByUrl('/login');
        this.notifService.success('Mot de passe modifié avec succès ! Veuillez vous reconnecter.');
      },
      error: (err) => {
        console.error('❌ Erreur API validate code:', err);
      },
    });

  }


}
