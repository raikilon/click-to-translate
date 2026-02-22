import fs from "node:fs/promises";
import fsSync from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import * as esbuild from "esbuild";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const projectRoot = path.resolve(__dirname, "..");
const distRoot = path.join(projectRoot, "dist");
const extensionEntryPoints = [
  "background/background.ts",
  "content/contentScript.ts",
  "pages/options/options.ts",
  "pages/popup/popup.ts",
];

const targetArg = process.argv[2] ?? "all";

if (targetArg === "clean") {
  await fs.rm(distRoot, { recursive: true, force: true });
  process.exit(0);
}

const targets =
  targetArg === "all" ? ["chrome", "firefox"] : [targetArg];

for (const target of targets) {
  if (!["chrome", "firefox"].includes(target)) {
    throw new Error(
      `Unsupported target "${target}". Use: chrome | firefox | all | clean`,
    );
  }
}

for (const target of targets) {
  await buildTarget(target);
}

async function buildTarget(target) {
  const infrastructureRoot = path.join(projectRoot, "infrastructure", target);
  const sourceRoot = path.join(infrastructureRoot, "src");
  const outputRoot = path.join(distRoot, target);
  const outputSourceRoot = path.join(outputRoot, "src");

  await fs.rm(outputRoot, { recursive: true, force: true });
  await fs.mkdir(outputRoot, { recursive: true });

  await copyInfrastructureAssets(infrastructureRoot, outputRoot);
  await copyStaticSourceAssets(sourceRoot, outputSourceRoot);

  const entryPoints = extensionEntryPoints.map((relativePath) =>
    path.join(sourceRoot, relativePath),
  );

  await esbuild.build({
    entryPoints,
    outdir: outputSourceRoot,
    outbase: sourceRoot,
    bundle: true,
    format: "iife",
    platform: "browser",
    target: "es2020",
    logLevel: "info",
    plugins: [tsPathAliasPlugin()],
  });

  console.log(`Built ${target} extension in ${path.relative(projectRoot, outputRoot)}`);
}

async function copyInfrastructureAssets(infrastructureRoot, outputRoot) {
  const entries = await fs.readdir(infrastructureRoot, { withFileTypes: true });

  for (const entry of entries) {
    if (entry.name === "src" || entry.name === "tsconfig.json") {
      continue;
    }

    const sourcePath = path.join(infrastructureRoot, entry.name);
    const destinationPath = path.join(outputRoot, entry.name);

    if (entry.isDirectory()) {
      await fs.cp(sourcePath, destinationPath, { recursive: true });
      continue;
    }

    if (entry.isFile()) {
      await fs.copyFile(sourcePath, destinationPath);
    }
  }
}

async function copyStaticSourceAssets(sourceRoot, outputSourceRoot) {
  await fs.mkdir(outputSourceRoot, { recursive: true });
  await copyStaticFilesRecursive(sourceRoot, outputSourceRoot);
}

async function copyStaticFilesRecursive(sourceDir, destinationDir) {
  const entries = await fs.readdir(sourceDir, { withFileTypes: true });

  for (const entry of entries) {
    const sourcePath = path.join(sourceDir, entry.name);
    const destinationPath = path.join(destinationDir, entry.name);

    if (entry.isDirectory()) {
      await fs.mkdir(destinationPath, { recursive: true });
      await copyStaticFilesRecursive(sourcePath, destinationPath);
      continue;
    }

    if (!entry.isFile()) {
      continue;
    }

    if (path.extname(entry.name).toLowerCase() === ".ts") {
      continue;
    }

    await fs.copyFile(sourcePath, destinationPath);
  }
}

function tsPathAliasPlugin() {
  const aliasMap = new Map([
    ["@application", path.join(projectRoot, "application", "src")],
    ["@domain", path.join(projectRoot, "domain", "src")],
  ]);

  return {
    name: "ts-path-alias",
    setup(build) {
      for (const [aliasPrefix, aliasBasePath] of aliasMap.entries()) {
        build.onResolve(
          { filter: new RegExp(`^${escapeRegex(aliasPrefix)}(?:\\/.*)?$`) },
          (args) => {
            const suffix = args.path.slice(aliasPrefix.length).replace(/^\//, "");
            const withoutExtension = suffix
              ? path.join(aliasBasePath, suffix)
              : path.join(aliasBasePath, "index");

            const resolvedPath = resolveTsModulePath(withoutExtension);
            return { path: resolvedPath };
          },
        );
      }
    },
  };
}

function resolveTsModulePath(withoutExtension) {
  const candidates = [
    `${withoutExtension}.ts`,
    path.join(withoutExtension, "index.ts"),
  ];

  for (const candidate of candidates) {
    try {
      const stats = fsSync.statSync(candidate);
      if (stats.isFile()) {
        return candidate;
      }
    } catch {
      // Try next candidate.
    }
  }

  throw new Error(`Cannot resolve TS module path for "${withoutExtension}".`);
}

function escapeRegex(value) {
  return value.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}
