import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {
}

fun DependencyHandler.uiModules() {
    implementation(project(":notes:notes-ui"))
    implementation(project(":home:home-ui"))
}

fun DependencyHandler.uiCommon() {
    implementation(project(":common-ui"))
}
