package com.example.githubuserbfaa

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserbfaa.databinding.ActivityReminderSettingBinding

class ReminderSetting : AppCompatActivity() {
    companion object {
       private const val PREF_NAME = "SettingPref"
        private const val DAILY = "daily"
    }
    private var binding: ActivityReminderSettingBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        binding = ActivityReminderSettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setBtnAlarm()
        binding?.btnSwitchAlarm?.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                alarmReceiver.setRepeatingAlarm(applicationContext, AlarmReceiver.TYPE_REPEAT, "Daily Github Reminder!")
            } else {
                alarmReceiver.cancelAlarm(applicationContext, AlarmReceiver.TYPE_REPEAT)
            }
            saveChek(isChecked)
        }
    }

    private fun saveChek(checked: Boolean) {
        val edit = mSharedPreferences.edit()
        edit.putBoolean(DAILY, checked)
        edit.apply()
    }

    private fun setBtnAlarm() {
        binding?.btnSwitchAlarm?.isChecked = mSharedPreferences.getBoolean(DAILY, false)
    }

}