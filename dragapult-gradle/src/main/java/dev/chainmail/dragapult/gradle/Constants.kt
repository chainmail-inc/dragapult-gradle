package dev.chainmail.dragapult.gradle

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project
import java.io.File

internal object Constants {

    const val latestBinaryName = "dragapult.zip"
    const val latestBinaryUrl =
        "https://github.com/chainmail-inc/dragapult/releases/latest/download/${latestBinaryName}"

    fun bundle(project: Project): File {
        return File(tmpDir(project), latestBinaryName)
    }

    fun binary(project: Project): File {
        val binaryName = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> "dragapult.bat"
            Os.isFamily(Os.FAMILY_UNIX) || Os.isFamily(Os.FAMILY_MAC) -> "dragapult"
            else -> throw UnsupportedOperationException("Your OS is not supported by dragapult.")
        }
        return File(project.buildDir, "dragapult/bin/${binaryName}")
    }

    fun tmpDir(project: Project): File {
        val buildDir = project.buildDir
        return File(buildDir, "tmp").also {
            it.mkdirs()
        }
    }

}