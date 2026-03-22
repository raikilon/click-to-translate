# Web Apps Workspace

Nx workspace containing:

- `apps/vocabulary`: Angular web app
- `apps/extension`: WXT browser extension
- `libs/auth`: shared auth contracts/constants
- `libs/language`: shared language contracts/utils

## Prerequisites

- Node.js 20+
- npm

## Install

Run from `web-app`:

```bash
npm install
```

## Run

From `web-app`:

```bash
# Angular app (dev)
npm run serve

# Extension (Chrome)
npm run serve:extension

# Extension (Firefox)
npm run serve:extension:firefox
```

## Build

From `web-app`:

```bash
# Angular app
npm run build

# Extension (Chrome)
npm run build:extension

# Extension (Firefox)
npm run build:extension:firefox
```

## Test / Type Check

From `web-app`:

```bash
# Angular
npm run test

# Extension type-check target
npm run test:extension
```

## Zip Extension

From `web-app`:

```bash
npm run zip:extension
npm run zip:extension:firefox
```

## Project Notes

- Angular proxy config: `apps/vocabulary/proxy.conf.json`
- Extension runtime env files: `apps/extension/.env.development` and `apps/extension/.env.production`
- If gateway auth or language endpoints change, update `libs/auth` and `libs/language`

## App Guides

- Vocabulary app details: `apps/vocabulary/README.md`
- Extension details: `apps/extension/README.md`
