#import <Cordova/CDV.h>

@import Firebase;

@interface YourAppCheckProviderFactory : NSObject <FIRAppCheckProviderFactory>
@end

@interface FirebaseAuthenticationPlugin : CDVPlugin

- (void)getToken:(CDVInvokedUrlCommand *)command;
- (void)enableDebug:(CDVInvokedUrlCommand *)command;

@end