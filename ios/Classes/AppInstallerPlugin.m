#import "AppInstallerPlugin.h"
#import <StoreKit/StoreKit.h>

@implementation AppInstallerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"app_installer"
            binaryMessenger:[registrar messenger]];
  AppInstallerPlugin* instance = [[AppInstallerPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"goStore" isEqualToString:call.method]) {
    NSString *appId = call.arguments[@"iOSAppId"];
    if (!appId.length) {
        result([FlutterError errorWithCode:@"ERROR" message:@"Invalid app id" details:nil]);
    } else {
        if ([call.arguments[@"review"] boolValue]) {
            [SKStoreReviewController requestReview];
            result(nil);
        } else {
            NSString* iTunesLink = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/xy/app/foo/id%@", appId];
            //打开 App Store
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:iTunesLink] options:@{} completionHandler:nil];
            result(nil);
        }
    }
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
