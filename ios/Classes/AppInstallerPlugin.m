#import "AppInstallerPlugin.h"

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
        NSString* iTunesLink;
        //根据系统判断打开方式
        if([[[UIDevice currentDevice] systemVersion] floatValue] >= 11) {
            iTunesLink = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/xy/app/foo/id%@", appId];
        } else {
            iTunesLink = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?type=Purple+Software&id=%@", appId];
        }
        //如果是评价直接跳转到App Store 的评价页面
        if([call.arguments[@"review"] boolValue]){
            iTunesLink=[iTunesLink stringByAppendingString:@"?action=write-review"];
        }
        //打开 App Store
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:iTunesLink]];

        result(nil);
    }
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
