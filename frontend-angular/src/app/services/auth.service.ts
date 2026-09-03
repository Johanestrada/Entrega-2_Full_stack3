import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountInfo, AuthenticationResult } from '@azure/msal-browser';
import { MsalService } from '@azure/msal-angular';
import { Observable, tap } from 'rxjs';
import { apiScope } from '../app.config';

interface AuthResponse { token: string }

@Injectable({ providedIn: 'root' })
export class AuthService {
	private readonly key = 'coligo_session';

	constructor(private http: HttpClient, private msal: MsalService) {}

	login(username: string, password: string): Observable<AuthResponse> {
		return this.http.post<AuthResponse>('http://localhost:8084/auth/login', { username, password }).pipe(
			tap(response => localStorage.setItem(this.key, response.token))
		);
	}

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
		return !!this.getMicrosoftAccount() || !!localStorage.getItem(this.key);
	}

	logout(): void {
		localStorage.removeItem(this.key);
		if (this.getMicrosoftAccount()) {
			void this.msal.logoutRedirect({ postLogoutRedirectUri: 'http://localhost:4173/login' });
		}
	}
}
