import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccountService } from '../../_helpers/services/account.service';
import { AuthService } from '../../_helpers/services/auth.service';
import { NotificationService } from '../../_helpers/services/notification.service';


@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'],
})
export class ProfilComponent {
  profileForm: FormGroup;
  isEditModalOpen = false;

  userId: number | null = null;
  data: any

  private fb = inject(FormBuilder);
  private accountService = inject(AccountService);
  private authService = inject(AuthService);
  private notifService = inject(NotificationService);


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
      firstName: [userData.firstName],
      lastName: [userData.lastName],
      username: [userData.username],
      phoneNumber: [userData.phoneNumber],
      email: [userData.email],
      country: [userData.country],
      idNumber: [null]
    });
  }

  ngOnInit(): void {
    let userData: any = {};
    if (typeof window !== 'undefined' && localStorage.getItem('user')) {
      userData = JSON.parse(localStorage.getItem('user') || '{}');
    }

    console.log('userData', userData.id);
    this.userId = userData.id || null;

    if (this.userId) {
      this.fetchTransactions();
    }
  }

  fetchTransactions(): void {
    this.accountService.getTransactions(this.userId).subscribe({
      next: (res) => {
        console.log('Transactions:', res);
        this.data = res;

        if (this.data && this.data.idNumber) {
          this.profileForm.patchValue({
            idNumber: this.data.idNumber
          });
        }

        console.log(this.data.length);
      },
      error: (err) => {
        console.error('Erreur de récupération des transactions', err);
      }
    });
  }

  openEditModal(): void {
    this.isEditModalOpen = true;
  }

  closeModal(): void {
    this.isEditModalOpen = false;
  }

  onSubmit(): void {
    console.log(this.userId);

    if (this.profileForm.valid && this.userId) {
      const formData = this.profileForm.value;
      console.log('Mise à jour profil :', formData);

      this.authService.updateUser(this.userId, formData).subscribe({
        next: (res) => {
          console.log('Profil mis à jour avec succès', res.username);
          this.closeModal();
          this.notifService.success("Profil mis à jour avec succès.");
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour du profil', err);
          this.notifService.error("Échec de la mise à jour du profil. Veuillez réessayer.");
        }
      });
    } else {
      console.log('Formulaire invalide ou userId manquant');
      this.notifService.error("Formulaire invalide. Veuillez vérifier les informations saisies.");
    }
  }

}
