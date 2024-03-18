import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.Dependency

fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.implementation(dependency: Dependency) {
    add("implementation", dependency)
}