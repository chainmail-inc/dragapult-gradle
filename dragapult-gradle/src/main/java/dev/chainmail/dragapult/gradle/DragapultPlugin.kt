package dev.chainmail.dragapult.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

class DragapultPlugin : Plugin<Project> {

    private lateinit var extension: DragapultExtension

    override fun apply(target: Project) {
        DragapultDownloadTask.registerIn(target)
        extension = DragapultExtension.registerIn(target)
        DragapultTask.registerIn(target)

        target.plugins.whenPluginAdded {
            target.afterEvaluate {
                target.configure()
            }
        }
    }

    private fun Project.configure() {
        val app = extensions.findByType<AppExtension>()

        if (app != null) {
            return configureUpon(app)
        }

        val library = extensions.findByType<LibraryExtension>()

        if (library != null) {
            return configureUpon(library)
        }

        logger.error("Cannot configure dragapult for project ${project.name}")
    }

    private fun Project.configureUpon(extension: AppExtension) {
        extension.applicationVariants.forEach { variant ->
            DragapultGenerateTask.registerIn(project, variant)
        }
    }

    private fun Project.configureUpon(extension: LibraryExtension) {
        extension.libraryVariants.forEach { variant ->
            DragapultGenerateTask.registerIn(project, variant)
        }
    }

}