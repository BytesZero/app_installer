import 'dart:async';

import 'package:flutter/services.dart';

class AppInstaller {
  static const MethodChannel _channel = const MethodChannel('app_installer');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  ///去应用商店
  ///
  static Future<void> goStore(String androidAppId, String iOSAppId,
      {bool review = false}) async {}

  ///安装 Apk
  ///[filePath] Apk file path
  static Future<void> installApk(String filePath) async {}
}
