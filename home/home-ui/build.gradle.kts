plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.android.library.compose)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.library.navigation)
    alias(libs.plugins.common.android.library.koin)
}
android {
    namespace = "com.notesmakers.home_ui"
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
        arg("compose-destinations.moduleName", "home")
    }
}

dependencies {
    implementation(project(":auth:auth-ui"))
    implementation(project(":core:ui"))
    implementation(project(":core:database"))
    implementation(project(":home:home-domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(project(":notes:notes-ui"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("io.insert-koin:koin-androidx-compose:3.4.1")
}