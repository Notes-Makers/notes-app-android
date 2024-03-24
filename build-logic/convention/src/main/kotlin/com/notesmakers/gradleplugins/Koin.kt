package com.notesmakers.gradleplugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKoin(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    dependencies {
        add("implementation", libs.findLibrary("koin.core").get())
        add("implementation", libs.findLibrary("koin.android").get())
        add("implementation", libs.findLibrary("koin.annotations").get())
        add("ksp", libs.findLibrary("koin.ksp.compiler").get())
    }
}