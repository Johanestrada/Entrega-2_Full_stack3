import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
@Component({selector:'app-root',standalone:true,imports:[CommonModule,RouterOutlet,RouterLink,RouterLinkActive],templateUrl:'./app.component.html',styleUrl:'./app.component.css'})
export class AppComponent {
	readonly userName = 'Administrador';

	constructor(public auth: AuthService, private router: Router) {
		this.auth.handleMicrosoftRedirect().subscribe({
			next: result => {
				if (result?.account) {
					this.auth.acquireApiToken().subscribe({
						next: token => console.info('[MSAL] Access Token obtenido para api.access', { tokenLength: token.accessToken.length }),
						error: error => console.error('[MSAL] No se pudo obtener el Access Token de la API', error)
					});
					void this.router.navigateByUrl('/dashboard');
				}
			},
			error: error => console.error('[MSAL] Error procesando el retorno de Microsoft Entra', error)
		});
	}

	logout() {
		this.auth.logout();
		if (!this.auth.isAuthenticated()) {
			void this.router.navigateByUrl('/login');
		}
	}
}
