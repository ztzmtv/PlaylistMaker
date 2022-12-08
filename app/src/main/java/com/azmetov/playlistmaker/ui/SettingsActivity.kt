package com.azmetov.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azmetov.playlistmaker.App
import com.azmetov.playlistmaker.R
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_KEY
import com.azmetov.playlistmaker.other.Constants.THEME_SWITCHER_PREFERENCES
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(THEME_SWITCHER_PREFERENCES, MODE_PRIVATE)

        val shareTextView = findViewById<TextView>(R.id.tv_share)
        val supportTextView = findViewById<TextView>(R.id.tv_support)
        val agreementTextView = findViewById<TextView>(R.id.tv_user_agreement)
        val themeSwitcherView = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val darkMode = sharedPrefs.getBoolean(THEME_SWITCHER_KEY, false)
        themeSwitcherView.isChecked = darkMode

        themeSwitcherView.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putBoolean(THEME_SWITCHER_KEY, checked)
                .apply()
        }

        shareTextView.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_link))
                type = "text/plain"
                if (this.resolveActivity(packageManager) != null) {
                    startActivity(this)
                }
            }
        }

        supportTextView.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                val emailArray = arrayOf(getString(R.string.email_yapr_support))
                putExtra(Intent.EXTRA_EMAIL, emailArray)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_support))
                if (this.resolveActivity(packageManager) != null) {
                    startActivity(this)
                }
            }
        }

        agreementTextView.setOnClickListener {
            val intent: Intent =
                Uri.parse(getString(R.string.url_user_agreement_yand_prac)).let { webpage ->
                    Intent(Intent.ACTION_VIEW, webpage)
                }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}