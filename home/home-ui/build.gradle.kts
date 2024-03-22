plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.common.android.library.compose)
    alias(libs.plugins.googleDevtoolsKsp)
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

    libraryVariants.all {
        val variant = this
        kotlin.sourceSets {
            getByName(variant.name) {
                kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
            }
        }
    }
    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "home")
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.coreRaamcostaDestinations)
    ksp(libs.kspRaamcostaDestinations)
}