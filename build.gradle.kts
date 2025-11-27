// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // ⭐ 1. Google Services 플러그인이 여기에 정의되어야 합니다.
    id("com.google.gms.google-services") version "4.4.0" apply false // 버전은 다를 수 있습니다.
}