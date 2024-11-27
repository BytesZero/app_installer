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
    NSString *appId = call.arguments[@"macOSAppId"];
    if (!appId.length) {
        result([FlutterError errorWithCode:@"ERROR" message:@"Invalid app id" details:nil]);
    } else {
        if ([call.arguments[@"review"] boolValue]) {
            [SKStoreReviewController requestReview];
        } else {
            NSString *urlString = [NSString stringWithFormat:@"macappstore://itunes.apple.com/app/id%@", appId];
            NSURL *url = [NSURL URLWithString:urlString];
            if ([[NSWorkspace sharedWorkspace] openURL:url]) {
                result(nil);
            } else {
                result([FlutterError errorWithCode:@"UNAVAILABLE"
                                           message:@"Cannot open App Store"
                                           details:nil]);
            }
        }
    }
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end