plugins {
    alias(libs.plugins.common.android.application)
    alias(libs.plugins.common.android.application.compose)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.application.navigation)
    alias(libs.plugins.common.android.application.koin)
}

android {
    namespace = "com.notesmakers.noteapp"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "app")
    }
}

dependencies {
    implementation(project(":home:home-ui"))
    implementation(project(":auth:auth-ui"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.raamcosta.destinations.animations)
}