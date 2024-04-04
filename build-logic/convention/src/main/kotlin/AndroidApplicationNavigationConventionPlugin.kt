import com.android.build.api.dsl.ApplicationExtension
import com.notesmakers.gradleplugins.configureNavigation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidApplicationNavigationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.configure<ApplicationExtension> {
                configureNavigation(this)
//                buildTypes.configureEach {
//                    val variant = this
//                    tasks.withType<KotlinCompile>().configureEach {
//                        sourceSets {
//                            getByName(variant.name) {
//                                kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}