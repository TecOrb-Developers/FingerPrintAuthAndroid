package com.fingerprint_auth

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.CancellationSignal
import android.widget.Toast
import androidx.core.app.ActivityCompat


class BiometricAuthSupport(private val mContext: Context) {

    private var cancellationSignal: CancellationSignal? = null

    fun notifyUser(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    fun checkBiometricSupport(): Boolean {
        val keyguardManager: KeyguardManager = mContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                mContext, android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint hs not been enabled in settings.")
            return false
        }
        return if (mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }
}