package dev.chainmail.dragapult.gradle

import org.gradle.api.Project

open class DragapultExtension {

    var inputFile: String = ""
    var inputFormat: String = "csv"
    var inputSeparator: String = ","

    var outputDir: String = ""
    var outputFormat: String = "android"

    companion object {

        fun registerIn(project: Project) =
            project.extensions.create("dragapult", DragapultExtension::class.java).also {
                it.outputDir = "${project.buildDir}/generated/res/dragapult"
            }

    }

}