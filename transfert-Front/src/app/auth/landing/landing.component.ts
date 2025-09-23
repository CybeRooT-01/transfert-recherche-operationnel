import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent {

  private router = inject(Router);

  constructor()
  {}

  login()
  {
    this.router.navigateByUrl("/login")
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }

}
