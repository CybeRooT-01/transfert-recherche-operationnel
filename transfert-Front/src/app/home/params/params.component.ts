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

  private accountService = inject(AccountService);

  constructor() {}

  ngOnInit(): void {
    if (typeof window !== 'undefined' && window.localStorage) {
      const userDataStr = localStorage.getItem('user');

      if (userDataStr) {
        try {
          const userData = JSON.parse(userDataStr);
          console.log('✅ userData', userData);

          this.accountType =
            userData.subscriptionType?.toLowerCase() === 'premium'
              ? 'premium'
              : 'free';

          this.userId = userData.id || null;
        } catch (e) {
          console.error('❌ Erreur parsing userData:', e);
        }
      } else {
        console.warn('⚠️ Aucun user trouvé dans localStorage');
      }
    } else {
      console.warn('⚠️ localStorage indisponible');
    }
  }

  upgradeToPremium(): void {
    if (!this.userId) {
      console.error('❌ userId manquant');
      return;
    }

    this.accountService.upgradeToPremium(this.userId).subscribe({
      next: (res: any) => {
        console.log('✅ Compte passé à Premium', res);
        this.accountType = 'premium';

        const userDataStr = localStorage.getItem('user');
        if (userDataStr) {
          const userData = JSON.parse(userDataStr);
          userData.subscriptionType = 'PREMIUM';
          localStorage.setItem('user', JSON.stringify(userData));
        }
      },
      error: (err: any) => {
        console.error('❌ Erreur upgrade:', err);
      }
    });
  }

  promptDowngrade(): void {
    alert('Impossible de revenir à Free une fois Premium 🚀');
  }
}
