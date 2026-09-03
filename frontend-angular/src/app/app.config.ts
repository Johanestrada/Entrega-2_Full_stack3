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

export const apiScope = 'api://e0d39aa2-d7b9-4ef5-9bef-84e418dcae72/api.access';

export function msalInstanceFactory(): PublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: 'a43b07f8-2ca4-4985-b355-c62623fd8bc9',
      authority: 'https://login.microsoftonline.com/39428fa5-d349-476e-8a21-6570cfd7fa42',
      redirectUri: 'http://localhost:4173',
      postLogoutRedirectUri: 'http://localhost:4173/login'
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
  protectedResourceMap.set('http://localhost:8084/**', [apiScope]);
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
