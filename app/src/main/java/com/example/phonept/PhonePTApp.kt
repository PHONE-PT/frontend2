package com.example.phonept

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * 앱이 시작될 때 Firebase 서비스를 한 번만 초기화하기 위한 사용자 정의 Application 클래스입니다.
 * 이 클래스를 AndroidManifest.xml에 등록해야 합니다.
 */
class PhonePTApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 🚨 모든 Activity나 Service보다 먼저 Firebase를 초기화합니다.
        FirebaseApp.initializeApp(this)
    }
}