import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class AppInstaller {
  static const MethodChannel _channel = const MethodChannel('app_installer');

  ///去应用商店
  ///[androidAppId] Android package name
  ///[iOSAppId] iOS Bundle Id
  ///[review] iOS App Store evaluation
  static Future<void> goStore(String androidAppId, String iOSAppId,
      {bool review = false}) async {
    _channel.invokeMethod('goStore', {
      'androidAppId': androidAppId,
      'iOSAppId': iOSAppId,
      'review': review,
    });
  }

  ///安装 Apk
  ///[apkPath] Apk file path
  static Future<void> installApk(String apkPath) async {
    //判断 Android 平台
    if (Platform.isAndroid) {
      _channel.invokeMethod('installApk', {'apkPath': apkPath});
    }
  }
}
