import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class AppInstaller {
  static const MethodChannel _channel = const MethodChannel('app_installer');

  ///去应用商店
  ///
  static Future<void> goStore(String androidAppId, String iOSAppId,
      {bool review = false}) async {
    _channel.invokeMethod('goStore', {
      'androidAppId': androidAppId,
      'iOSAppId': iOSAppId,
      'review': review,
    });
  }

  ///安装 Apk
  ///[filePath] Apk file path
  static Future<void> installApk(String filePath) async {
    //判断 Android 平台
    if (Platform.isAndroid) {
      _channel.invokeMethod('installApk', {'filePath': filePath});
    }
  }
}
