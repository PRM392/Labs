plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.demozalopay"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.demozalopay"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ZaloPay SDK (local AAR in libs folder)
    implementation(files("libs/zpdk-release-v3.1.aar"))

    // OkHttp for HTTP requests to ZaloPay sandbox
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // ZXing Android core for generating QR code Bitmaps
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}