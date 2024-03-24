import com.android.build.api.dsl.ApplicationExtension
import com.notesmakers.gradleplugins.configureKoin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationKoinConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.configure<ApplicationExtension> {
                configureKoin(this)
            }
        }
    }
}