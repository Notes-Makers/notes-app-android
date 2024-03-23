package com.notesmakers.gradleplugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureNavigation(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    dependencies {
        add("implementation", libs.findLibrary("raamcosta.destinations.core").get())
        add("ksp", libs.findLibrary("raamcosta.destinations.ksp").get())
    }
}