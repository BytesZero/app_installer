# App 安装器示例

如何使用 App 安装器插件

[English](https://github.com/yy1300326388/app_installer/tree/master/example)

## 入门

- 打开应用商店

```
/// App Info
String androidAppId = 'com.tengyue360.student';
String iOSAppId = '1440249706';

AppInstaller.goStore(androidAppId, iOSAppId);
```

- 打开 iOS 评价应用页面

```
AppInstaller.goStore(androidAppId, iOSAppId, review: true);
```

- 安装 Apk

```
AppInstaller.installApk('/sdcard/apk/app-debug.apk');
```
