plugins {
    alias(libs.plugins.common.android.library)
    alias(libs.plugins.googleDevtoolsKsp)
    alias(libs.plugins.common.android.library.koin)
    id("com.apollographql.apollo3").version("3.7.3")
}
apollo {
    service("ai") {
        packageName.set("com.notesmakers.ai")
    }
}

android {
    namespace = "com.notesmakers.ai"
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
    implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
    implementation("com.apollographql.apollo3:apollo-api:3.7.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":core:network"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}