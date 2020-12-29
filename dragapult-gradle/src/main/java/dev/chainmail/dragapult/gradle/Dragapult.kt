package dev.chainmail.dragapult.gradle

import org.gradle.api.Project

val Project.hasLinkedExecutable: Boolean
    get() {
        return kotlin.runCatching {
            val result = exec {
                commandLine("dragapult", "-h")
            }

            result.exitValue == 0
        }.fold({it}, {false})
    }

val Project.hasBuildExecutable: Boolean
    get() = Constants.binary(this).exists()

val Project.hasDragapult
    get() = hasBuildExecutable || hasLinkedExecutable

fun Project.dragapult(vararg args: String) = exec {
    val command = when {
        hasBuildExecutable -> Constants.binary(this@dragapult).absolutePath
        hasLinkedExecutable -> "dragapult"
        else -> throw IllegalArgumentException("Dragapult is not linked or present in project.")
    }
    commandLine(command, *args)
}
