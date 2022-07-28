export const getToken: () => Promise<{
	token: string;
	expireTimeMillis: string;
}>;

export const enableDebug: () => Promise<void>;
