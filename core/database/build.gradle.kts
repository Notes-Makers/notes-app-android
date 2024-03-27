plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.library.koin)
    alias(libs.plugins.io.realm.kotlin)
}

android {
    namespace = "com.notesmakers.database"
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // realm db
    implementation(libs.realm.base)
    implementation(libs.realm.sync)
}