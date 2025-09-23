import { Component, inject } from '@angular/core';
import { AccountService } from '../../_helpers/services/account.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-historique',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './historique.component.html',
  styleUrl: './historique.component.css'
})
export class HistoriqueComponent {

  userId: number | null = null;
  data: any

  private accountService = inject(AccountService);


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
        console.log('Transactions:', res.history);
        this.data = res.history;
        console.log(this.data.length);
      },
      error: (err) => {
        console.error('Erreur de récupération des transactions', err);
      }
    });
  }

}
