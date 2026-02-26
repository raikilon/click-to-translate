# Browser Extension Local Guide

## Prerequisites

- Node.js 20+ (with npm)
- Running backend API (`http://localhost:8080`)
- Running Keycloak (`http://localhost:8081`) with realm `click-to-translate`

## Install and Build

From `browser-extension/`:

```powershell
npm ci
npm run build:chrome
npm run build:firefox
```

`build:*` uses `prod` profile by default.
Use `dev` profile builds to get localhost defaults:

```powershell
npm run build:chrome:dev
npm run build:firefox:dev
```

Build output:

- Chrome: `dist/chrome`
- Firefox: `dist/firefox`

## Load in Chrome

1. Open `chrome://extensions`
2. Enable `Developer mode`
3. Click `Load unpacked`
4. Select folder `browser-extension/dist/chrome`

## Load in Firefox

1. Open `about:debugging#/runtime/this-firefox`
2. Click `Load Temporary Add-on...`
3. Select file `browser-extension/dist/firefox/manifest.json`

## Configure the Extension

Open extension Options page and set or verify:

- API Base URL: `http://localhost:8080`
- Languages Path: `/api/translate/languages`
- Segments Path: `/api/translate`
- Authorize URL: `http://localhost:8081/realms/click-to-translate/protocol/openid-connect/auth`
- Token URL: `http://localhost:8081/realms/click-to-translate/protocol/openid-connect/token`
- OAuth Client ID: `click-to-translate-extension`
- Scopes: `openid segment translate`

Then click `Login to Load Languages`.

## Keycloak Redirect URI Requirement

The imported realm config expects these redirect URIs:

- `https://opjgbppjdccgdgidkpabmnmfohpjcjnk.chromiumapp.org/oauth2`
- `https://click-to-translate@local.extensions.allizom.org/oauth2`

If your Chrome extension ID is different, update Keycloak client `click-to-translate-extension`:

- Add redirect URI: `https://<your_chrome_extension_id>.chromiumapp.org/oauth2`
- Add web origin: `https://<your_chrome_extension_id>.chromiumapp.org`

Without this, OAuth login from the extension will fail.
