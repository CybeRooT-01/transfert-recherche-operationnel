import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../_helpers/services/account.service';
import { NotificationService } from '../../_helpers/services/notification.service';
import { jsPDF } from 'jspdf';

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
  private notifService = inject(NotificationService);

  userId: number | null = null;
  data: any = {
    balance: 0,
    depot: [],
    transfer: [],
    retrait: []
  };

  currentModal: 'depot' | 'retrait' | 'transfert' | null = null;

  amount: number | null = null;
  depotMode = 'mobile-money';

  retraitMontant: number | null = null;
  retraitMode = 'mobile-money';

  transMontantSend: number | null = null;
  transMontantRecept: number | null = null;
  transNumero = '';

  destinataire: any;

  private isUpdating = false;

  constructor() {}

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
      },
      error: (err) => {
        console.error('Erreur de récupération des transactions', err);
      }
    });
  }

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
      this.notifService.error('⚠️ Montant invalide');
      return;
    }

    this.accountService.deposit(this.amount, this.userId).subscribe({
      next: (res) => {
        this.notifService.success('✅ Dépôt réussi !');
        this.fetchTransactions();
        this.closeModal();
        this.generateReceipt(res, 'depot');
      },
      error: (err) => {
        this.notifService.error('❌ Erreur lors du dépôt');
      }
    });
  }

  submitRetrait(): void {
    if (!this.retraitMontant || this.retraitMontant <= 0) {
      this.notifService.error('❌ Montant invalide pour le retrait');
      return;
    }

    console.log('💰 Retrait demandé', { montant: this.retraitMontant, userId: this.userId });

    this.accountService.withdraw(this.retraitMontant, this.userId).subscribe({
      next: (res) => {
        console.log('✅ Retrait réussi', res);
        this.notifService.success('✅ Retrait effectué avec succès');
        this.fetchTransactions();
        this.closeModal();
        this.generateReceipt(res, 'retrait');
      },
      error: (err) => {
        console.error('❌ Erreur retrait', err);
        this.notifService.error('❌ Échec du retrait');
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
      this.notifService.error('❌ Infos invalides pour le transfert');
      return;
    }

    console.log('📤 Transfert demandé', {
      montant: this.transMontantSend,
      userId: this.userId,
      destinataire: this.transNumero
    });
    this.destinataire = this.transNumero;

    this.accountService.transfer(this.transMontantSend, this.userId, this.transNumero).subscribe({
      next: (res) => {
        console.log('✅ Transfert réussi', res);
        this.notifService.success('✅ Transfert effectué avec succès');
        this.fetchTransactions();
        this.closeModal();
        this.generateReceipt(res, 'transfert');
      },
      error: (err) => {
        console.error('❌ Erreur transfert', err);
        this.notifService.error('❌ Échec du transfert');
      }
    });
  }

  // generateReceipt(transaction: any, type: 'depot' | 'retrait' | 'transfert') {
  //   const doc = new jsPDF();

  //   doc.setFontSize(18);
  //   doc.text('Reçu de transaction', 105, 20, { align: 'center' });

  //   doc.setFontSize(12);
  //   doc.text(`Type : ${type}`, 20, 40);
  //   doc.text(`Montant : ${transaction.amountDebited || transaction.amountCredited} FCFA`, 20, 50);
  //   if (type === "transfert") {
  //     doc.text(`Numéro destinataire : ${this.destinataire}`, 20, 60);
  //   }
  //   doc.text(`N° de reçu : ${transaction.receiptNumber}`, 20, 80);
  //   doc.text(`Solde actuel : ${transaction.newBalance || transaction.newSenderBalance} FCFA`, 20, 70);
  //   doc.text(`Date : ${new Date().toLocaleString()}`, 20, 100);

  //   doc.save(`reçu_${type}.pdf`);
  // }

  generateReceipt(type: 'depot' | 'retrait' | 'transfert', data: any) {
    const doc = new jsPDF();

    // Titre
    doc.setFontSize(20);
    doc.setTextColor('#1e3a8a'); // bleu foncé
    doc.text('💳 Reçu de transaction', 105, 20, { align: 'center' });

    // Ligne séparatrice
    doc.setDrawColor('#1e3a8a');
    doc.setLineWidth(0.5);
    doc.line(20, 25, 190, 25);

    // Infos transaction
    doc.setFontSize(12);
    doc.setTextColor('#000');
    doc.setFont('helvetica', 'bold');

    let y = 35;
    doc.text(`Date :`, 20, y);
    doc.setFont('helvetica', 'normal');
    doc.text(`${new Date().toLocaleString()}`, 70, y);

    y += 10;
    doc.setFont('helvetica', 'bold');
    doc.text(`Utilisateur ID :`, 20, y);
    doc.setFont('helvetica', 'normal');
    doc.text(`${data.userId}`, 70, y);

    if (type === 'transfert') {
      y += 10;
      doc.setFont('helvetica', 'bold');
      doc.text(`Destinataire :`, 20, y);
      doc.setFont('helvetica', 'normal');
      doc.text(`${data.transNumero}`, 70, y);

      y += 10;
      doc.setFont('helvetica', 'bold');
      doc.text(`Montant envoyé :`, 20, y);
      doc.setFont('helvetica', 'normal');
      doc.text(`${data.transMontantSend} F CFA`, 70, y);

      y += 10;
      doc.setFont('helvetica', 'bold');
      doc.text(`Montant reçu :`, 20, y);
      doc.setFont('helvetica', 'normal');
      doc.text(`${data.transMontantRecept} F CFA`, 70, y);
    } else if (type === 'retrait' || type === 'depot') {
      y += 10;
      doc.setFont('helvetica', 'bold');
      doc.text(`Montant :`, 20, y);
      doc.setFont('helvetica', 'normal');
      doc.text(`${data.amount} F CFA`, 70, y);
    }

    // Ligne de fin
    doc.setDrawColor('#1e3a8a');
    doc.setLineWidth(0.5);
    doc.line(20, y + 10, 190, y + 10);

    // Footer
    doc.setFontSize(10);
    doc.setFont('helvetica', 'italic');
    doc.setTextColor('#6b7280'); // gris clair
    doc.text('Merci pour votre confiance !', 105, y + 20, { align: 'center' });

    // Téléchargement
    doc.save(`reçu-${type}-${new Date().getTime()}.pdf`);
  }

}
