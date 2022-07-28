export const getToken: () => Promise<{
	token: string;
	expireTimeMillis: string;
}>;
