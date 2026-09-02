import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
@Component({selector:'app-root',standalone:true,imports:[CommonModule,RouterOutlet,RouterLink,RouterLinkActive],templateUrl:'./app.component.html',styleUrl:'./app.component.css'})
export class AppComponent { readonly userName='Administrador'; constructor(public auth:AuthService,private router:Router){} logout(){this.auth.logout();this.router.navigateByUrl('/login');} }
