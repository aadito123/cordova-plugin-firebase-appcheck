const PLUGIN_NAME = 'FirebaseAppCheckPlugin';

exports.getToken = () => {
	return new Promise((resolve, reject) => {
		cordova.exec(resolve, reject, PLUGIN_NAME, 'getToken', []);
	});
};
