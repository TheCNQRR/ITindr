// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        autoCorrect = true
        config.setFrom("$rootDir/config/detekt/detekt.yml")
    }

    dependencies {
        val libs = rootProject.extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

        add("detektPlugins", libs.detekt.formatting)
        add("detekt", libs.detekt.cli)
        add("detekt", libs.detekt.rules.compose)
    }
}