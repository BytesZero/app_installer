import 'package:flutter/material.dart';
import 'package:app_installer/app_installer.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  /// 应用市场信息
  String androidAppId = 'com.tengyue360.student';
  String iOSAppId = '1440249706';
  String macOSAppId = '836500024';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              const SizedBox(height: 80),
              TextButton.icon(
                onPressed: () {
                  AppInstaller.goStore(
                    androidAppId,
                    iOSAppId,
                    macOSAppId: macOSAppId,
                  );
                },
                icon: const Icon(Icons.store),
                label: const Text('Go Store'),
              ),
              const SizedBox(height: 40),
              TextButton.icon(
                onPressed: () {
                  AppInstaller.goStore(
                    androidAppId,
                    iOSAppId,
                    macOSAppId: macOSAppId,
                    review: true,
                  );
                },
                icon: const Icon(Icons.rate_review),
                label: const Text('Go Store Review'),
              ),
              const SizedBox(height: 40),
              const Text(
                '⚠️需要先允许读取存储权限才可以⚠️',
                style: TextStyle(color: Colors.red),
              ),
              TextButton.icon(
                onPressed: () {
                  // 需要先允许读取存储权限才可以
                  // 需要先允许读取存储权限才可以
                  // 需要先允许读取存储权限才可以
                  AppInstaller.installApk('/sdcard/app/app-debug.apk');
                },
                icon: const Icon(Icons.arrow_downward),
                label: const Text('Install Apk'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
