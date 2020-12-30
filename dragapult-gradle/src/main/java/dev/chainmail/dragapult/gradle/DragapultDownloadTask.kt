package dev.chainmail.dragapult.gradle

import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.Project

object DragapultDownloadTask {

    fun name(project: Project) = "downloadDragapultFor${project.name.capitalize()}"

    fun registerIn(project: Project)= project.tasks.maybeRegister<Download>(name(project)) {
        src(Constants.latestBinaryUrl)
        dest(Constants.tmpDir(project))
    }

}