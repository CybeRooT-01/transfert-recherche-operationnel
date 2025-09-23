import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'],
})
export class ProfilComponent {
  profileForm!: FormGroup;
  isEditModalOpen = false; // état du modal

  private fb = inject(FormBuilder)

  constructor() {
    let userData: any = {};
    if (typeof window !== 'undefined' && window.localStorage) {
      const storedUser = localStorage.getItem('user');
      if (storedUser) {
        try {
          userData = JSON.parse(storedUser);
        } catch (e) {
          console.error('❌ Erreur parsing user:', e);
          userData = {};
        }
      }
    }

    this.profileForm = this.fb.group({
      prenom: [userData.firstName || '', [Validators.required, Validators.minLength(2)]],
      nom: [userData.lastName || '', [Validators.required, Validators.minLength(2)]],
      telephone: [userData.phoneNumber || '', [Validators.required, Validators.pattern(/^\d{8,9}$/)]],
      email: [userData.email || '', [Validators.email]],
      pays: [userData.country || '', [Validators.required]],
      photo: [userData.photo || null],
      numero_piece: [userData.idNumber || '', [Validators.required]],
      photo_piece: [userData.rectoCni || null],
    });
  }

  openEditModal(): void {
    this.isEditModalOpen = true;
  }

  closeModal(): void {
    this.isEditModalOpen = false;
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      const formData = this.profileForm.value;
      console.log('Mise à jour du profil réussie :', formData);

      this.closeModal();
    } else {
      console.log('Formulaire invalide');
    }
  }
}
