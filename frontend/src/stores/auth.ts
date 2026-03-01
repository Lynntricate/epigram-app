import { writable } from "svelte/store";

// token store
export const auth_token = writable<string | null>(null);

export const auth_username = writable<string | null>(null);