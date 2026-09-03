import { APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { InteractionType, PublicClientApplication } from '@azure/msal-browser';
import {
  MSAL_GUARD_CONFIG,
  MSAL_INSTANCE,
  MSAL_INTERCEPTOR_CONFIG,
  MsalBroadcastService,
  MsalGuard,
  MsalGuardConfiguration,
  MsalInterceptor,
  MsalInterceptorConfiguration,
  MsalService
} from '@azure/msal-angular';

import { routes } from './app.routes';
import { environment } from '../environments/environment';

export const apiScope = environment.apiScope;

export function msalInstanceFactory(): PublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: environment.clientId,
      authority: `https://login.microsoftonline.com/${environment.tenantId}`,
      redirectUri: environment.redirectUri,
      postLogoutRedirectUri: `${environment.redirectUri}/login`
    },
    cache: {
      cacheLocation: 'sessionStorage'
    }
  });
}

export function msalGuardConfigFactory(): MsalGuardConfiguration {
  return { interactionType: InteractionType.Redirect, authRequest: { scopes: [apiScope] } };
}

export function msalInterceptorConfigFactory(): MsalInterceptorConfiguration {
  const protectedResourceMap = new Map<string, Array<string>>();
  protectedResourceMap.set(`${environment.apiBaseUrl}/**`, [apiScope]);
  return { interactionType: InteractionType.Redirect, protectedResourceMap };
}

export function msalInitializerFactory(msal: MsalService): () => Promise<void> {
  return () => msal.instance.initialize();
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    { provide: MSAL_INSTANCE, useFactory: msalInstanceFactory },
    { provide: MSAL_GUARD_CONFIG, useFactory: msalGuardConfigFactory },
    { provide: MSAL_INTERCEPTOR_CONFIG, useFactory: msalInterceptorConfigFactory },
    { provide: HTTP_INTERCEPTORS, useClass: MsalInterceptor, multi: true },
    MsalService,
    MsalGuard,
    MsalBroadcastService,
    { provide: APP_INITIALIZER, useFactory: msalInitializerFactory, deps: [MsalService], multi: true }
  ]
};
