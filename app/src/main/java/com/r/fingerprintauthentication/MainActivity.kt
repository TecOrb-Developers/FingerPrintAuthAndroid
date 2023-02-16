package com.r.fingerprintauthentication

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fingerprint_auth.BiometricAuthSupport
import java.util.*

class MainActivity : AppCompatActivity() {

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P) object :
            BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                BiometricAuthSupport(applicationContext).notifyUser("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                BiometricAuthSupport(applicationContext).notifyUser("Authentication Success!")
                //  startActivity(Intent(this@MainActivity, Secret::class.java))
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BiometricAuthSupport(this).checkBiometricSupport()
        val button = findViewById<Button>(R.id.biometric_login)

        button.setOnClickListener {
            val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(this).setTitle("Title")
                .setSubtitle("Authentication is required")
                .setDescription("Fingerprint Authentication")
                .setNegativeButton("Cancel", this.mainExecutor) { _, _ ->
                }.build()
            biometricPrompt.authenticate(
                BiometricAuthSupport(this).getCancellationSignal(),
                mainExecutor,
                authenticationCallback)
        }
    }


}
