package com.zero.app_installer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * AppInstallerPlugin
 */
public class AppInstallerPlugin implements FlutterPlugin, ActivityAware, MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener {

    private Context applicationContext;
    private Activity mActivity;
    private MethodChannel methodChannel;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        AppInstallerPlugin instance = new AppInstallerPlugin();
        instance.onAttachedToEngine(registrar.context(), registrar.messenger());
        instance.onAttachedToActivity(registrar.activity());
        registrar.addActivityResultListener(instance.getActivityResultListener());
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
    }

    private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
        this.applicationContext = applicationContext;
        methodChannel = new MethodChannel(messenger, "app_installer");
        methodChannel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        this.applicationContext = null;
        methodChannel.setMethodCallHandler(null);
        methodChannel = null;
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        onAttachedToActivity(binding.getActivity());
        binding.addActivityResultListener(getActivityResultListener());
    }

    private void onAttachedToActivity(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
        binding.removeActivityResultListener(getActivityResultListener());
        binding.addActivityResultListener(getActivityResultListener());

    }

    @Override
    public void onDetachedFromActivity() {
        this.mActivity = null;
    }

    /**
     * 创建 ActivityResult 监听
     *
     * @return ActivityResult 监听
     */
    private PluginRegistry.ActivityResultListener getActivityResultListener() {
        return this;
    }

    @Override
    public void onMethodCall(MethodCall call,@NonNull Result result) {
        String method = call.method;
        if (method.equals("goStore")) {
            String appId = (String) call.argument("androidAppId");
            goAppStore(mActivity, appId);
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

    /**
     * 去应用商店
     *
     * @param appId appId
     */
    private void goAppStore(Activity activity, String appId) {
        String appPackageName;
        // 获取包名
        if (appId != null) {
            appPackageName = appId;
        } else {
            appPackageName = activity.getPackageName();
        }
        // 去应用商店
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        marketIntent.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        activity.startActivity(marketIntent);
    }

    // 安装apkFile
    private File apkFile;
    // 回调处理
    private Result result;

    /**
     * 安装应用的流程
     *
     * @param apkFile apk 文件
     * @param result  返回结果
     */
    private void installProcess(File apkFile, Result result) {
        this.apkFile = apkFile;
        this.result = result;
        installApk(apkFile, result);
    }

    /**
     * 设置安装未知来源App权限
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + applicationContext.getPackageName());
        // 注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        this.mActivity.startActivityForResult(intent, 10086);
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
                Uri contentUri = FileProvider.getUriForFile(applicationContext,
                        applicationContext.getPackageName() + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            mActivity.startActivity(intent);
            if (result != null) {
                result.success(true);
            }
        } else {
            if (result != null) {
                result.success(false);
            }
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
