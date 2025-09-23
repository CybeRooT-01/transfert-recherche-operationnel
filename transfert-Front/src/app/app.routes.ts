import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { HomeComponent } from './home/home.component';
import { SignUpComponent } from './auth/sign-up/sign-up.component';
import { ForgetPasswordComponent } from './auth/forget-password/forget-password.component';
import { ProfilComponent } from './home/profil/profil.component';
import { AccueilComponent } from './home/accueil/accueil.component';
import { CodeComponent } from './auth/code/code.component';
import { HistoriqueComponent } from './home/historique/historique.component';
import { ParamsComponent } from './home/params/params.component';
import { ConfirmPasswordComponent } from './auth/confirm-password/confirm-password.component';
import { Code2Component } from './auth/code2/code2.component';
import { LandingComponent } from './auth/landing/landing.component';


export const routes: Routes = [
  { path: "", component: LandingComponent },
  { path: "login", component: LoginComponent },
  { path: "code", component: CodeComponent },
  { path: "code2", component: Code2Component },
  { path: "confirm-password", component: ConfirmPasswordComponent },
  { path: "sign-up", component: SignUpComponent },
  { path: "forget-password", component: ForgetPasswordComponent },
  { path: "home", component: HomeComponent,
    children: [
      { path: '', redirectTo: 'accueil', pathMatch: 'full' },
      { path: 'accueil', component: AccueilComponent },
      { path: 'historique', component: HistoriqueComponent },
      { path: 'profil', component: ProfilComponent },
      { path: 'params', component: ParamsComponent }
    ]

  },



];
