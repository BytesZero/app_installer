import 'package:flutter/material.dart';
import 'package:app_installer/app_installer.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  /// 应用市场信息
  String androidAppId = 'com.tengyue360.student';
  String iOSAppId = '1440249706';

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
              SizedBox(height: 80),
              TextButton.icon(
                onPressed: () {
                  AppInstaller.goStore(androidAppId, iOSAppId);
                },
                icon: Icon(Icons.store),
                label: Text('Go Store'),
              ),
              SizedBox(height: 40),
              TextButton.icon(
                onPressed: () {
                  AppInstaller.goStore(androidAppId, iOSAppId, review: true);
                },
                icon: Icon(Icons.rate_review),
                label: Text('Go Store Review'),
              ),
              SizedBox(height: 40),
              Text(
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
                icon: Icon(Icons.arrow_downward),
                label: Text('Install Apk'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
