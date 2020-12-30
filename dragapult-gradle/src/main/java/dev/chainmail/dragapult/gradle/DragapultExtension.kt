package dev.chainmail.dragapult.gradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

open class DragapultExtension {

    var inputFiles: Array<out String> = arrayOf()
    var inputFormat: String = "csv"
    var inputSeparator: String = ","

    var outputDir: String = ""
    var outputFormat: String = "android"

    fun inputFiles(vararg files: String) {
        inputFiles = files
    }

    companion object {

        fun registerIn(project: Project) =
            project.extensions.create<DragapultExtension>("dragapult").also {
                it.outputDir = "${project.buildDir}/generated/res/dragapult"
            }

    }

}