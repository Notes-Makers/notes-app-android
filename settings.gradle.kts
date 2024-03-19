pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Note App"
include(":app")
include(":common-ui")
include(":notes:notes-data")
include(":notes:notes-ui")
include(":notes:notes-domain")
include(":home:home-ui")
include(":home:home-domain")
include(":home:home-data")