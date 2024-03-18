import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {
//    const val navigationCore =
//        "io.github.raamcosta.compose-destinations:core:${Versions.navigation}"
//    const val navigationKsp = "io.github.raamcosta.compose-destinations:ksp:${Versions.navigation}"
}

fun DependencyHandler.notes() {
    implementation(project(":notes:notes-ui"))
}
