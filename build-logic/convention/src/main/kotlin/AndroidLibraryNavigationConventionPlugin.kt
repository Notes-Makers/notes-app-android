import com.android.build.gradle.LibraryExtension
import com.notesmakers.gradleplugins.configureNavigation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AndroidLibraryNavigationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
//            pluginManager.apply("com.google.devtools.ksp") //TODO NOT WORKING
            extensions.configure<LibraryExtension> {
                configureNavigation(this)
                libraryVariants.all {
                    val variant = this
                    tasks.withType<KotlinCompile>().configureEach {
                        sourceSets {
                            getByName(variant.name) {
                                kotlin.srcDir("build/generated/ksp/${variant.name}/kotlin")
                            }
                        }
                    }
                }
            }
        }
    }
}