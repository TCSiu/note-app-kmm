package org.example.new_kmm_note_app

import android.app.Application
import core.di.initKoin
import core.di.initSharedScreen

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()

        initSharedScreen()

//        startKoin {
//            androidContext(this@MainApplication)
//            androidLogger()
//
//
//        }
    }
}