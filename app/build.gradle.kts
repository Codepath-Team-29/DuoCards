plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.duocards"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.duocards"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // import spotify api
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    //implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.spotify.android:auth:1.2.3") // Spotify Authentication library
    // implementation("com.github.kaaes:spotify-web-api-android:0.4.1") // Spotify Web API interaction
    implementation("com.google.code.gson:gson:2.8.5") // For JSON parsing



}