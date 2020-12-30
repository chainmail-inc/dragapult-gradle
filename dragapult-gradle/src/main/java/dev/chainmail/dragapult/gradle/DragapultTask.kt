package dev.chainmail.dragapult.gradle

import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import java.io.File

abstract class DragapultTask : DefaultTask() {

    @TaskAction
    fun onAction() {
        if (project.hasDragapult) {
            logger.quiet("Dragapult is already present in project. No need to do anything.")
            return
        }

        project.tasks.create<Download>("downloadDragapultFor${project.name.capitalize()}") {
            src(Constants.latestBinaryUrl)
            dest(Constants.tmpDir(project))
        }.download()

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