# Click to Translate Extension

## Prerequisites
- Node.js 20+ (recommended)
- npm

## Install
```bash
npm install
```

## Environment Variables
Runtime config is read in `src/entrypoints/background/config/runtimeConfig.ts` and requires these keys:

- `WXT_API_BASE_URL`
- `WXT_TRANSLATE_PATH`
- `WXT_AUTH_AUTHORIZE_URL`
- `WXT_AUTH_TOKEN_URL`
- `WXT_OAUTH_CLIENT_ID`
- `WXT_AUTH_SCOPES`

Where to put them:

- Development: `extension/.env.development`
- Production build/zip: `extension/.env.production`

Example:
```env
WXT_API_BASE_URL=http://localhost:8080
WXT_TRANSLATE_PATH=/api/translate
WXT_AUTH_AUTHORIZE_URL=http://localhost:8081/realms/click-to-translate/protocol/openid-connect/auth
WXT_AUTH_TOKEN_URL=http://localhost:8081/realms/click-to-translate/protocol/openid-connect/token
WXT_OAUTH_CLIENT_ID=click-to-translate-extension
WXT_AUTH_SCOPES=openid segment translate
```

## Run (Development)
Chrome:
```bash
npm run dev
```

Firefox:
```bash
npm run dev:firefox
```

## Build
Chrome:
```bash
npm run build
```

Firefox:
```bash
npm run build:firefox
```

Build output:
- Chrome: `.output/chrome-mv3`
- Firefox: `.output/firefox-mv3`

## Package Zip
Chrome:
```bash
npm run zip
```

Firefox:
```bash
npm run zip:firefox
```

## Type Check
```bash
npm run compile
```

