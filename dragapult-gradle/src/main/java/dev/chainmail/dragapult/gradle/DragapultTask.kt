package dev.chainmail.dragapult.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import java.io.File

abstract class DragapultTask : DefaultTask() {

    @TaskAction
    fun onAction() {
        if (project.hasDragapult) {
            return
        }

        val download = project.tasks.findByName(DragapultDownloadTask.name(project))

        download?.actions?.forEach { it.execute(download) }
            ?: logger.error("Download task couldn't be performed")

        project.copy {
            from(project.zipTree(Constants.bundle(project)))
            into(project.buildDir)
        }

        File(project.buildDir, "dragapult-release")
            .renameTo(File(project.buildDir, "dragapult"))

        Constants.bundle(project).delete()

        if (!project.hasDragapult) {
            logger.error("Dragapult cannot be installed in this project. Try installing it manually.")
            logger.error(Constants.latestBinaryUrl)
        }
    }

    companion object {

        fun name(project: Project) = "requireDragapultBinaryIn${project.name.capitalize()}"

        fun registerIn(project: Project) {
            project.tasks.register<DragapultTask>(name(project))
        }

    }

}