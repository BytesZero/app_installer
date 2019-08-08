package com.zero.app_installer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** AppInstallerPlugin */
public class AppInstallerPlugin implements MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener {
  private final Registrar mRegistrar;

  public AppInstallerPlugin(Registrar mRegistrar) {
    this.mRegistrar = mRegistrar;
    this.mRegistrar.addActivityResultListener(this);
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "app_installer");
    channel.setMethodCallHandler(new AppInstallerPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    String method = call.method;
    if (method.equals("goStore")) {
      String appId = (String) call.argument("androidAppId");
      String appPackageName;
      // 获取包名
      if (appId != null) {
        appPackageName = appId;
      } else {
        appPackageName = mRegistrar.activity().getPackageName();
      }
      // 去应用商店
      Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
      marketIntent.addFlags(
          Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
      mRegistrar.activity().startActivity(marketIntent);

      result.success(true);
    } else if (method.equals("installApk")) {
      String filePath = call.argument("apkPath");
      if (!TextUtils.isEmpty(filePath)) {
        installProcess(new File(filePath), result);
      } else {
        result.error("installApk", "apkPath is null", null);
      }
    } else {
      result.notImplemented();
    }
  }

  // 安装apkFile
  private File apkFile;
  // 回调处理
  private Result result;

  // 安装应用的流程
  private void installProcess(File apkFile, Result result) {
    this.apkFile = apkFile;
    this.result = result;
    boolean haveInstallPermission;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      haveInstallPermission = mRegistrar.context().getPackageManager().canRequestPackageInstalls();
      if (!haveInstallPermission) {
        // Toast.makeText(mRegistrar.activeContext(), "安装应用需要打开未知来源权限，请去设置中开启权限",
        // Toast.LENGTH_LONG).show();
        startInstallPermissionSettingActivity();
      }
    }
    // 有权限，开始安装
    installApk(apkFile, result);
  }

  /**
   * 设置安装未知来源App权限
   */
  @TargetApi(Build.VERSION_CODES.O)
  private void startInstallPermissionSettingActivity() {
    Uri packageURI = Uri.parse("package:" + mRegistrar.context().getPackageName());
    // 注意这个是8.0新API
    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
    mRegistrar.activity().startActivityForResult(intent, 10086);
  }

  /**
   * 安装Apk
   *
   * @param apkFile 安装文件
   */
  private void installApk(File apkFile, Result result) {
    if (apkFile != null && apkFile.exists() && apkFile.length() > 0) {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri contentUri = FileProvider.getUriForFile(mRegistrar.activeContext(),
            mRegistrar.context().getPackageName() + ".fileProvider", apkFile);
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
      } else {
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
      }
      mRegistrar.activity().startActivity(intent);
      result.success(true);
    } else {
      result.success(false);
    }
    this.apkFile = null;
    this.result = null;
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == 10086 && resultCode == Activity.RESULT_OK) {
      installProcess(apkFile, result);
      return true;
    }
    return false;
  }
}
