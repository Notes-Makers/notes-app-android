plugins {
    alias(libs.plugins.common.android.application)
    alias(libs.plugins.common.android.application.compose)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.application.navigation)
    alias(libs.plugins.common.android.application.koin)
    alias(libs.plugins.io.realm.kotlin)
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
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    applicationVariants.all {
        val variant = this
        kotlin.sourceSets {
            getByName(variant.name) {
                kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
            }
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
    // realm db
    implementation(libs.realm.base)
    implementation(libs.realm.sync)

    implementation(project(":core:database"))
    implementation(project(":home:home-ui"))
    implementation(project(":auth:auth-ui"))
    implementation(project(":notes:notes-ui"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.raamcosta.destinations.animations)
    debugImplementation(libs.androidx.ui.test.manifest)
}