function bytesToBase64Url(bytes: Uint8Array): string {
  let binary = "";
  for (const value of bytes) {
    binary += String.fromCharCode(value);
  }

  return btoa(binary).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/g, "");
}

function randomBytes(length: number): Uint8Array {
  const bytes = new Uint8Array(length);
  crypto.getRandomValues(bytes);
  return bytes;
}

export function generateCodeVerifier(): string {
  return bytesToBase64Url(randomBytes(64));
}

export async function codeChallengeS256(verifier: string): Promise<string> {
  const encodedVerifier = new TextEncoder().encode(verifier);
  const digest = await crypto.subtle.digest("SHA-256", encodedVerifier);
  return bytesToBase64Url(new Uint8Array(digest));
}

export function generateState(): string {
  return bytesToBase64Url(randomBytes(24));
}
