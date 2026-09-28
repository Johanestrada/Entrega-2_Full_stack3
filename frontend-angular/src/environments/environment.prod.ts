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
  tenantId: '39428fa5-d349-476e-8a21-6570cfd7fa42',
  clientId: 'a43b07f8-2ca4-4985-b355-c62623fd8bc9',
  apiScope: 'api://e0d39aa2-d7b9-4ef5-9bef-84e418dcae72/api.access'
};
