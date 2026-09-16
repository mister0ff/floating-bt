package com.chess.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.webkit.JavascriptInterface
import com.getcapacitor.BridgeActivity

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Injetando ponte JavaScript na WebView do Capacitor
        bridge.webView.addJavascriptInterface(WebAppInterface(this), "AndroidBridge")
    }

    class WebAppInterface(private val activity: MainActivity) {
        @JavascriptInterface
        fun requestOverlayPermission() {
            if (!Settings.canDrawOverlays(activity)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${activity.packageName}")
                )
                activity.startActivity(intent)
            }
        }

        @JavascriptInterface
        fun startService() {
            if (Settings.canDrawOverlays(activity)) {
                val intent = Intent(activity, FloatingService::class.java)
                activity.startService(intent)
                activity.finishAffinity() // Sai do app e mantém o flutuante ativo
            }
        }
    }
}
