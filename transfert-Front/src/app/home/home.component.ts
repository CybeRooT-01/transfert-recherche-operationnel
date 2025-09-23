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

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    console.log("👤 Utilisateur:", user);
    
    this.nomComplet = user.username
    this.telephone = user.phoneNumber;

    const token = localStorage.getItem('token');
    console.log('Token:', token);
  }

  logout()
  {
    this.router.navigateByUrl('')
  }

}
