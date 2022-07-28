package by.aadito123.cordova.firebase;

import android.net.Uri;
import android.util.Log;
import android.content.Context;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.appcheck.AppCheckToken;
import com.google.firebase.appcheck.FirebaseAppCheck;
// import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.FirebaseApp;

public class FirebaseAppCheckPlugin extends CordovaPlugin {
    protected static final String TAG = "FirebaseAppCheck";
    protected static Context applicationContext = null;
    private FirebaseAppCheck mAppCheck;

    @Override
    protected void pluginInitialize() {
        applicationContext = this.cordova.getActivity().getApplicationContext();
        Log.d(TAG, "Application context: " + applicationContext.toString());
        FirebaseApp.initializeApp(applicationContext);
        Log.d(TAG, "Firebase initialised");
        mAppCheck = FirebaseAppCheck.getInstance();
        Log.d(TAG, "FirebaseAppCheck initialised");
        mAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        Log.d(TAG, "PlayIntegrityAppCheckProviderFactory installed3");
    }

    protected static void handleExceptionWithContext(Exception e, CallbackContext context) {
        String msg = e.toString();
        Log.e(TAG, msg);
        context.error(msg);
    }

    private void getToken(CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    mAppCheck.getAppCheckToken(false)
                            .addOnCompleteListener(new OnCompleteListener<AppCheckToken>() {
                                @Override
                                public void onComplete(Task<AppCheckToken> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            AppCheckToken result = task.getResult();
                                            Log.d(TAG, "AppCheckToken: " + result.getToken());
                                            JSONObject returnResults = new JSONObject();
                                            returnResults.put("token", result.getToken());
                                            returnResults.put("expireTimeMillis",
                                                    Long.toString(result.getExpireTimeMillis()));
                                            callbackContext.success(returnResults);
                                        } else {
                                            Log.d(TAG, "Token task unsuccesful");
                                            callbackContext.error(task.getException().getMessage());
                                        }
                                    } catch (JSONException e) {
                                        Log.d(TAG, "Token task exception");
                                        handleExceptionWithContext(e, callbackContext);
                                    }
                                }
                            });
                } catch (Exception e) {
                    handleExceptionWithContext(e, callbackContext);
                }
            }
        });
    }

    // Todo: add enable debug mode
    private void enableDebug(CallbackContext callbackContext) {

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        try {
            if (action.equals("getToken")) {
                Log.d(TAG, "executing getToken");
                this.getToken(callbackContext);
            } 
            else if (action.equals("enableDebug")) {
                Log.d(TAG, "executing enableDebug");
                this.enableDebug(callbackContext);
            } else {
                callbackContext.error("Invalid action: " + action);
                return false;
            }
        } catch (Exception e) {
            handleExceptionWithContext(e, callbackContext);
            return false;
        }
        return true;
    }
}