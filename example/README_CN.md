# App 安装器示例

如何使用 App 安装器插件

[English](https://github.com/yy1300326388/app_installer/tree/master/example)

[![Codemagic build status](https://api.codemagic.io/apps/5d5610bc6a6c3600097b4391/5d5610bc6a6c3600097b4390/status_badge.svg)](https://codemagic.io/apps/5d5610bc6a6c3600097b4391/5d5610bc6a6c3600097b4390/latest_build)

## 入门

- 打开应用商店

```dart
/// App Info
String androidAppId = 'com.tengyue360.student';
String iOSAppId = '1440249706';

AppInstaller.goStore(androidAppId, iOSAppId);
```

- 打开 iOS 评价应用页面

```dart
AppInstaller.goStore(androidAppId, iOSAppId, review: true);
```

- 安装 Apk

> ⚠️需要先允许读取存储权限才可以，不然会出现解析包错误⚠️

```dart
AppInstaller.installApk('/sdcard/apk/app-debug.apk');
```

> AndroidManifest.xml

```xml
<!-- Provider -->
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

> android/app/src/main/res/xml/file_paths.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path path="Android/data/packagename/" name="files_root" />
    <external-path path="." name="external_storage_root" />
</paths>

//替换 packagename 为你的包名
```
