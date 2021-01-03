package com.zero.app_installer_example;

import android.os.Bundle;

import com.zero.app_installer.AppInstallerPlugin;

import io.flutter.app.FlutterActivity;

/**
 * EmbeddingV1 兼容测试类
 */
public class EmbeddingV1Activity extends FlutterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInstallerPlugin.registerWith(registrarFor("com.zero.app_installer.AppInstallerPlugin"));
    }
}
