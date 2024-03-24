import com.android.build.gradle.LibraryExtension
import com.notesmakers.gradleplugins.configureKoin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryKoinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.configure<LibraryExtension> {
                configureKoin(this)
            }
        }
    }
}