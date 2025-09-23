import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet, RouterLinkActive, RouterLink, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  profileImage: string = "/assets/images/raffa.jpg";
  nomComplet: string = "Mugiwara Ndao";
  telephone: string = "771234567";

  private router = inject(Router);
  private fb = inject(FormBuilder);

  constructor(){
  }

  ngOnInit(): void {
    if (typeof window !== 'undefined' && window.localStorage) {
      const userData = localStorage.getItem('user');
      if (userData) {
        try {
          const user = JSON.parse(userData);
          console.log("👤 Utilisateur:", user);

          this.nomComplet = user.username ?? '';
          this.telephone = user.phoneNumber ?? '';
        } catch (e) {
          console.error("❌ Erreur parsing userData:", e);
        }
      } else {
        console.warn("⚠️ Aucun utilisateur trouvé dans localStorage");
      }

      const token = localStorage.getItem('token');
      console.log('Token:', token);
    } else {
      console.warn("⚠️ localStorage non disponible (SSR ou hors navigateur)");
    }
  }

  logout()
  {
    this.router.navigateByUrl('')
  }

}
