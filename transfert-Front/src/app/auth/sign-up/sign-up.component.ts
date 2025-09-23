import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../_helpers/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  registerForm!: FormGroup;
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  constructor() {
    this.registerForm = this.fb.group({
      firstName: ['Jean Malick'],
      lastName: ['Mendy'],
      username: ['jemmy'],
      pinHash: ['0000'],
      phoneNumber: ['888'],
      email: ['jemmy@gmail.com'],
      Country: ['Senegal'],
      idNumber: ['777'],
      password: ['999'],
      photo: [null],
      rectoCni: [null],
      versoCni: [null],
    });
  }

  // 🔹 Convertir fichier en Base64
  private convertFileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }

  async onFileChange(event: any, controlName: string) {
    const file: File = event.target.files[0];
    if (file) {
      const base64 = await this.convertFileToBase64(file);
      this.registerForm.patchValue({ [controlName]: base64 });
    }
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formData = { ...this.registerForm.value };

      console.log('📤 Body envoyé :', JSON.stringify(formData, null, 2));

      this.authService.register(formData).subscribe({
        next: (res) => {
          console.log('✅ Utilisateur créé :', res);
          this.router.navigateByUrl('/login');
        },
        error: (err) => {
          console.error('❌ Erreur lors de l\'inscription :', err);
        }
      });
    } else {
      console.log('⚠️ Formulaire invalide !');
    }
  }

  login() {
    this.router.navigateByUrl('/login');
  }

}
