# Click to Translate Extension

## Prerequisites
- Node.js 20+ (recommended)
- npm

## Install
```bash
npm install
```

## Environment Variables
Runtime config is read in the extension infrastructure and requires these keys:

- `WXT_API_BASE_URL`
- `WXT_SEGMENT_PATH`
- `WXT_TRANSLATE_LANGUAGES_PATH`
- `WXT_AUTH_LOGIN_PATH`
- `WXT_AUTH_ME_PATH`
- `WXT_AUTH_LOGOUT_PATH`

`host_permissions` in the manifest is derived from `WXT_API_BASE_URL`.

Where to put them:

- Development: `extension/.env.development`
- Production build/zip: `extension/.env.production`

Example:
```env
WXT_API_BASE_URL=http://localhost:8082
WXT_SEGMENT_PATH=/segment
WXT_TRANSLATE_LANGUAGES_PATH=/translate/languages
WXT_AUTH_LOGIN_PATH=/auth/login
WXT_AUTH_ME_PATH=/auth/me
WXT_AUTH_LOGOUT_PATH=/auth/logout
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

## Compile with Development Env
Use this when you want a build that reads `.env.development` (without running watch mode):

```bash
npx wxt build --mode development
```
