import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';
import { NotificationService } from '../../_helpers/services/notification.service';


@Component({
  selector: 'app-code2',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './code2.component.html',
  styleUrl: './code2.component.css'
})
export class Code2Component {


  codeForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);

  private authService = inject(AuthService);
  private notifService = inject(NotificationService);


  constructor()
  {
    this.codeForm = this.fb.group({
      code: [''],
    })

  }

  valider() {
    const codeData = this.codeForm.value;
    const phoneOrEmail = localStorage.getItem('phoneOrEmail');
    const data = {
      code: codeData.code,
      phoneOrEmail: phoneOrEmail
    };

    console.log('📩 Données envoyées:', data);

    this.authService.validateCode(data).subscribe({
      next: (res) => {
        console.log('✅ Code vérifié:', res);
        this.router.navigateByUrl('/confirm-password');
        this.notifService.success('Code vérifié avec succès !');
      },
      error: (err) => {
        console.error('❌ Erreur API validate code:', err);
      },
    });
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }

}
