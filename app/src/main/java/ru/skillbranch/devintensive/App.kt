package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class App : Application() {
    companion object{
        private var instance:App? = null

        fun applicationContext() : Context{
            return instance!!.applicationContext
        }
    }

    init{
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        PreferencesRepository.getAppTheme().also {
           //setDefaultNightMode(MODE_NIGHT_YES)
            setDefaultNightMode(it)
        }
    }
}