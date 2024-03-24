plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}


gradlePlugin {
    plugins {
        register("androidLibraryComposeConventionPlugin") {
            id = "gradlePlugins.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplicationComposeConventionPlugin")  {
            id = "gradlePlugins.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplicationConventionPlugin") {
            id = "gradlePlugins.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryConventionPlugin") {
            id = "gradlePlugins.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryNavigationConventionPlugin") {
            id = "gradlePlugins.android.library.navigation"
            implementationClass = "AndroidLibraryNavigationConventionPlugin"
        }
        register("androidApplicationNavigationConventionPlugin") {
            id = "gradlePlugins.android.application.navigation"
            implementationClass = "AndroidApplicationNavigationConventionPlugin"
        }
        register("androidLibraryKoinConventionPlugin") {
            id = "gradlePlugins.android.library.koin"
            implementationClass = "AndroidLibraryKoinConventionPlugin"
        }
        register("androidApplicationKoinConventionPlugin") {
            id = "gradlePlugins.android.application.koin"
            implementationClass = "AndroidApplicationKoinConventionPlugin"
        }
    }
}