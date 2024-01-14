plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.chapter_blog"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.chapter_blog"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("jp.wasabeef:richeditor-android:2.0.0")
    implementation ("com.afollestad.material-dialogs:core:0.9.6.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.chromium.net:cronet-embedded:113.5672.61")
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.10")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.10")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}