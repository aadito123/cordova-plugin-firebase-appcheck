#import "FirebaseAppCheckPlugin.h"

@implementation YourAppCheckProviderFactory

- (nullable id<FIRAppCheckProvider>)createProviderWithApp:
    (nonnull FIRApp *)app {
  if (@available(iOS 14.0, *)) {
    return [[FIRAppAttestProvider alloc] initWithApp:app];
  }
  return [[FIRDeviceCheckProvider alloc] initWithApp:app];
}

@end

@implementation FirebaseAppCheckPlugin

- (void)pluginInitialize {
  NSLog(@"Starting Firebase AppCheck plugin");
  // get GoogleService-Info.plist file path
  NSString *filePath =
      [[NSBundle mainBundle] pathForResource:@"GoogleService-Info"
                                      ofType:@"plist"];

  // if file is successfully found, use it
  if (filePath) {
    [FirebasePlugin.firebasePlugin
        _logMessage:@"GoogleService-Info.plist found, setup: [FIRApp "
                    @"configureWithOptions]"];
    // create firebase configure options passing .plist as content
    FIROptions *options = [[FIROptions alloc] initWithContentsOfFile:filePath];

    // configure FIRApp with options
    [FIRApp configureWithOptions:options];

    isFirebaseInitializedWithPlist = true;
  } else {
    // no .plist found, try default App
    [FirebasePlugin.firebasePlugin
        _logError:
            @"GoogleService-Info.plist NOT FOUND, setup: [FIRApp defaultApp]"];
    [FIRApp configure];
  }

  YourAppCheckProviderFactory *providerFactory =
      [[YourAppCheckProviderFactory alloc] init];
  [FIRAppCheck setAppCheckProviderFactory:providerFactory];
}

- (void)getToken:(CDVInvokedUrlCommand *)command {
  NSLog(@"Getting AppCheck token");

  FIRAppCheck *appCheck = [FIRAppCheck appCheck];
}

@end
