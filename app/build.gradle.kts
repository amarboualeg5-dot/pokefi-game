plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.pokefi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokefi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("org.osmdroid:osmdroid-android:6.1.18")
}
