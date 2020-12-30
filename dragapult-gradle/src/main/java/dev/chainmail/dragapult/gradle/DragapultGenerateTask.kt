package dev.chainmail.dragapult.gradle

import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import java.io.File
import kotlin.reflect.KClass

abstract class DragapultGenerateTask : DefaultTask() {

    @get:Input
    abstract var inputFiles: FileCollection

    @get:Input
    abstract val inputSeparator: Property<String>

    @get:Input
    abstract val inputFormat: Property<String>

    @get:Input
    abstract val outputFormat: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun onAction() {
        inputFiles.forEach { generateFilesFrom(it) }
    }

    private fun generateFilesFrom(from: File) {
        val result = project.dragapult(
            "-i", from.absolutePath,
            "-iF", inputFormat.get(),
            "-iS", inputSeparator.get(),
            "-o", outputDir.get().asFile.absolutePath,
            "-oF", outputFormat.get()
        )
        if (result.exitValue != 0) {
            logger.error("Did not generate any files in module ${project.name}")
        }
        result.rethrowFailure()
    }

    companion object {

        fun registerIn(project: Project, variant: BaseVariant) {
            val extension = project.extensions.getByType<DragapultExtension>()
            val output = project.file(extension.outputDir)
            val task = project.tasks.maybeRegister<DragapultGenerateTask>(
                name = "useDragapultOn${project.name.capitalize()}"
            ) {
                dependsOn(DragapultTask.name(project))
                inputFiles = project.files(*extension.inputFiles)
                inputSeparator.set(extension.inputSeparator)
                inputFormat.set(extension.inputFormat)
                outputFormat.set(extension.outputFormat)
                outputDir.set(output)
            }

            variant.registerGeneratedResFolders(project.files(output).builtBy(task))
        }

    }

}

inline fun <reified T : Task> TaskContainer.maybeRegister(name: String, noinline init: T.() -> Unit): TaskProvider<T> {
    val task = findNamed(name, T::class)
    if (task?.isPresent == true) {
        return task
    }

    return register(name, init)
}

inline fun <reified T : Task> TaskContainer.findNamed(name: String, klass: KClass<T>) = try {
    named(name, klass)
} catch (e: UnknownTaskException) {
    null
}