import { Injectable } from '@angular/core';
import { AccountInfo, AuthenticationResult } from '@azure/msal-browser';
import { MsalService } from '@azure/msal-angular';
import { Observable } from 'rxjs';
import { apiScope } from '../app.config';

@Injectable({ providedIn: 'root' })
export class AuthService {
	constructor(private msal: MsalService) {}

	loginWithMicrosoft(): Observable<void> {
		return this.msal.loginRedirect({ scopes: [apiScope] });
	}

	handleMicrosoftRedirect(): Observable<AuthenticationResult | null> {
		return this.msal.handleRedirectObservable();
	}

	getMicrosoftAccount(): AccountInfo | null {
		return this.msal.instance.getAllAccounts()[0] ?? null;
	}

	acquireApiToken(): Observable<AuthenticationResult> {
		const account = this.getMicrosoftAccount();
		if (!account) {
			throw new Error('No hay una cuenta Microsoft autenticada');
		}
		return this.msal.acquireTokenSilent({ scopes: [apiScope], account });
	}

	isAuthenticated(): boolean {
		return !!this.getMicrosoftAccount();
	}

	logout(): void {
		if (this.getMicrosoftAccount()) {
			void this.msal.logoutRedirect({ postLogoutRedirectUri: 'http://localhost:4173/login' });
		}
	}
}
