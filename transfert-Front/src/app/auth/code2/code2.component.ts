import { CommonModule } from '@angular/common';
import { Component, inject, Signal, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-code2',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './code2.component.html',
  styleUrl: './code2.component.css'
})
export class Code2Component {


  codeForm!: FormGroup;

  private router = inject(Router);
  private fb = inject(FormBuilder);

  constructor()
  {
    this.codeForm = this.fb.group({
      code: [''],
    })

  }

  valider()
  {
    this.router.navigateByUrl("/confirm-password")
  }

  register()
  {
    this.router.navigateByUrl("/sign-up")
  }

}
