export const appRoutePaths = {
  home: '',
  login: 'login',
  settings: 'settings',
  entryDetails: 'entries/:entryId',
  wildcard: '**',
} as const;

const entriesBaseRoute = '/entries';

export const appRouteUrls = {
  home: '/',
  login: '/login',
  settings: '/settings',
} as const;

export const appRouteCommands = {
  home(): [string] {
    return [appRouteUrls.home];
  },
  login(): [string] {
    return [appRouteUrls.login];
  },
  settings(): [string] {
    return [appRouteUrls.settings];
  },
  entryDetails(entryId: number): [string, number] {
    return [entriesBaseRoute, entryId];
  },
};
