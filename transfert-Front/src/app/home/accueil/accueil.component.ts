import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../_helpers/services/account.service';


@Component({
  selector: 'app-accueil',
   standalone: true,
  imports: [ CommonModule, FormsModule ],
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.css']
})
export class AccueilComponent {

  private router = inject(Router);
  private accountService = inject(AccountService);

  currentModal: 'depot' | 'retrait' | 'transfert' | null = null;

  amount: number | null = null;
  depotMode = 'mobile-money';

  retraitMontant: number | null = null;
  retraitMode = 'mobile-money';

  transMontantSend: number | null = null;
  transMontantRecept: number | null = null;
  transNumero = '';

  private isUpdating = false;

  constructor() {}


  openModal(modal: 'depot' | 'retrait' | 'transfert'): void {
    this.currentModal = modal;
  }

  closeModal(): void {
    this.currentModal = null;
    this.amount = null;
    this.retraitMontant = null;
    this.transMontantSend = null;
    this.transMontantRecept = null;
    this.transNumero = '';
  }

  submitDepot(): void {
    if (!this.amount || this.amount <= 0) {
      console.log('Montant ou utilisateur invalide');
      return;
    }

    console.log('💰 Dépôt demandé', { montant: this.amount, userId: 9 });

    this.accountService.deposit(this.amount, 9).subscribe({
      next: (res) => {
        console.log('✅ Dépôt réussi', res);
        this.closeModal();
      },
      error: (err) => {
        console.error('❌ Erreur dépôt', err);
      }
    });
  }

  submitRetrait(): void {
    if (!this.retraitMontant || this.retraitMontant <= 0) {
      console.log('Montant invalide');
      return;
    }

    console.log('💰 Retrait demandé', { montant: this.retraitMontant, userId: 9 });

    this.accountService.withdraw(this.retraitMontant, 9).subscribe({
      next: (res) => {
        console.log('✅ Retrait réussi', res);
        this.closeModal();
      },
      error: (err) => {
        console.error('❌ Erreur retrait', err);
      }
    });
  }

  updateFromSend(): void {
    if (this.isUpdating) return;
    this.isUpdating = true;

    if (this.transMontantSend && this.transMontantSend > 0) {
      this.transMontantRecept = this.transMontantSend * 0.9; // -10%
    } else {
      this.transMontantRecept = null;
    }

    this.isUpdating = false;
  }

  updateFromRecept(): void {
    if (this.isUpdating) return;
    this.isUpdating = true;

    if (this.transMontantRecept && this.transMontantRecept > 0) {
      this.transMontantSend = this.transMontantRecept * 1.1; // +10%
    } else {
      this.transMontantSend = null;
    }

    this.isUpdating = false;
  }

  submitTransfert(): void {
    if (!this.transMontantSend || this.transMontantSend <= 0 || !this.transNumero) {
      console.log('❌ Infos invalides pour le transfert');
      return;
    }

    console.log('📤 Transfert demandé', {
      montant: this.transMontantSend,
      userId: 9,
      destinataire: this.transNumero
    });

    this.accountService.transfer(this.transMontantSend, 9, this.transNumero).subscribe({
      next: (res) => {
        console.log('✅ Transfert réussi', res);
        this.closeModal();
      },
      error: (err) => {
        console.error('❌ Erreur transfert', err);
      }
    });
  }

}
