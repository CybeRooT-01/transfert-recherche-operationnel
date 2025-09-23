import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'],
})
export class ProfilComponent implements OnInit {
  profileForm!: FormGroup;
  isEditModalOpen = false; // état du modal

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const userData = JSON.parse(localStorage.getItem('user') || '{}');

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

      // ⚡ Appel API backend (AWS / Laravel)
      // this.userService.updateProfile(formData).subscribe(...);

      this.closeModal(); // fermer le modal après sauvegarde
    } else {
      console.log('Formulaire invalide');
    }
  }
}
