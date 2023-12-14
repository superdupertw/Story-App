package com.dicoding.storyapplication.view.account

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapplication.R
import com.dicoding.storyapplication.data.remote.response.LoginResult
import com.dicoding.storyapplication.databinding.ActivityAccountBinding
import com.dicoding.storyapplication.utils.AlarmReceiver
import com.dicoding.storyapplication.view.ViewModelFactory
import com.dicoding.storyapplication.view.login.LoginActivity
import com.dicoding.storyapplication.view.login.LoginPreferences
import com.dicoding.storyapplication.view.setting.AccountViewModel

class AccountActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var loginPreferences: LoginPreferences
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var accountBinding: ActivityAccountBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var loginResults: LoginResult




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginPreferences = LoginPreferences(this)
        accountBinding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(accountBinding.root)

        loginResults = loginPreferences.getUser()
        accountBinding.name.text = loginResults.name
        accountViewModel = obtainViewModel(this as AppCompatActivity)

        showLoading(false)
        darkMode()
        setupView()
        setupAction()
        accountBinding.btnSetRepeatingAlarm.setOnClickListener(this)
        accountBinding.btnCancelRepeatingAlarm.setOnClickListener(this)
        accountBinding.btnSetLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        alarmReceiver = AlarmReceiver()
    }

    private fun setupAction() {
        accountBinding.btnLogout.setOnClickListener {
            showLoading(true)
            loginPreferences.removeUser()
            val intent = Intent(this@AccountActivity, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun setupView() {
        val myactionbar = supportActionBar
        myactionbar?.title = getString(R.string.account_page)
        myactionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View) {
        val repeatMessage = getString(R.string.alarm_message)
        when (v.id) {
            R.id.btn_set_repeating_alarm -> alarmReceiver.setRepeatingAlarm(this, repeatMessage)
            R.id.btn_cancel_repeating_alarm -> alarmReceiver.cancelAlarm(this)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): AccountViewModel {
        val loginPreferences = LoginPreferences(activity.application)
        val factory = ViewModelFactory.getInstance(activity.application, loginPreferences)
        return ViewModelProvider(activity, factory)[AccountViewModel::class.java]
    }

    private fun darkMode() {
        accountBinding.switchTheme.apply {
            accountViewModel.getThemeSettings().observe(this@AccountActivity) { isDark ->
                val mode =
                    if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(mode)
                isChecked = isDark
            }
            setOnCheckedChangeListener { _, isChecked ->
                accountViewModel.saveThemeSettings(isChecked)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        accountBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}