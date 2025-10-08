plugins {
    id("com.android.application")
}

android {
    namespace = "net.idscan.example.android.mrzscannerexample"

    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        applicationId = "net.idscan.example.android.mrzscannerexample"
        versionCode = 100500
        versionName = "1.5.0"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("net.idscan.components.android:scanmrz:1.5.0")
}