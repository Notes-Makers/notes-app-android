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
    implementation(project(":core:database"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(project(":core:auth"))
    implementation(project(":core:network"))
    implementation(libs.androidx.work.runtime.ktx)
    implementation(project(":core:ai"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.raamcosta.destinations.animations)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.insert-koin:koin-androidx-compose:3.4.1")
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc04")
}