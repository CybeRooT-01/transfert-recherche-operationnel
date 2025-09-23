import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../_helpers/services/account.service';


@Component({
  selector: 'app-params',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './params.component.html',
  styleUrls: ['./params.component.css']
})
export class ParamsComponent implements OnInit {
  accountType: 'free' | 'premium' = 'free';
  confirmArchive = false;
  userId: number | null = null;

  private accountService = inject(AccountService)

  constructor() {}

  ngOnInit(): void {
    const userData = JSON.parse(localStorage.getItem('user') || '{}');
    this.accountType = userData.subscriptionType?.toLowerCase() === 'premium' ? 'premium' : 'free';
    console.log('userData', userData);
    this.userId = userData.id || null;
  }

  upgradeToPremium() {
    // if (!this.userId) return;

    this.accountService.upgradeToPremium(9).subscribe({
      next: (res:any) => {
        console.log('✅ Compte passé à Premium', res);
        this.accountType = 'premium';

        const userData = JSON.parse(localStorage.getItem('user') || '{}');
        userData.subscriptionType = 'PREMIUM';
        localStorage.setItem('user', JSON.stringify(userData));
      },
      error: (err:any) => {
        console.error('Erreur upgrade:', err);
      }
    });
  }

  promptDowngrade() {
    alert('Impossible de revenir à Free une fois Premium 🚀');
  }

 
}
