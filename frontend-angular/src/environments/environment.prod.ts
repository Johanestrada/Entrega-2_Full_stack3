declare global {
  interface Window {
    __env?: { apiBaseUrl?: string };
  }
}

const runtime = window.__env ?? {};

export const environment = {
  production: true,
  apiBaseUrl: runtime.apiBaseUrl ?? window.location.origin,
  redirectUri: window.location.origin,
  tenantId: '2b19a7f0-3812-4fba-b561-52eca3bdd992',
  clientId: '1b5cb56f-4445-4103-a718-3efebbde144b',
  apiScope: 'api://ef2b9500-20d4-4ad8-9c4d-a1ea41344c6b/api.access'
};
