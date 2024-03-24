plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.android.library.compose)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.library.navigation)
}
android {
    namespace = "com.notesmakers.auth_ui"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "auth")
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(project(":core-ui"))
}