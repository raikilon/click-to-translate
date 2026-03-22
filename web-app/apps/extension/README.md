# Click to Translate Extension

For workspace setup/install/common commands, use the root guide: `web-app/README.md`.

## Extension Runtime Config

Required env vars:

- `WXT_API_BASE_URL`

Files:

- Dev: `apps/extension/.env.development`
- Prod: `apps/extension/.env.production`

Example:

```env
WXT_API_BASE_URL=http://localhost:8082
```

## Extension Commands

Run from workspace root (`web-app`):

```bash
npm run serve:extension
npm run serve:extension:firefox
npm run build:extension
npm run build:extension:firefox
npm run zip:extension
npm run zip:extension:firefox
npm run test:extension
```

## Build Artifacts

- Chrome: `apps/extension/.output/chrome-mv3`
- Firefox: `apps/extension/.output/firefox-mv3`
