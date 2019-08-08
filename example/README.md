# app_installer_example

Demonstrates how to use the app_installer plugin.

## Getting Started

- Open Store

```
/// App Info
String androidAppId = 'com.tengyue360.student';
String iOSAppId = '1440249706';

AppInstaller.goStore(androidAppId, iOSAppId);
```

- Open Review

```
AppInstaller.goStore(androidAppId, iOSAppId, review: true);
```

- Install Apk

```
AppInstaller.installApk('/sdcard/apk/app-debug.apk');
```
