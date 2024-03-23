import com.android.build.api.dsl.CommonExtension
import com.notesmakers.gradleplugins.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("kotlinCompilerExtension").get().toString()
        }
    }

    dependencies {
        val composeBom = libs.findLibrary("androidx.compose.bom").get()
        add("implementation", platform(composeBom))
        add("implementation", libs.findLibrary("androidx.ui").get())
        add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
        add("implementation", libs.findLibrary("androidx.material3").get())
        add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
    }

}