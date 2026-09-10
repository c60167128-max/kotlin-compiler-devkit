package org.jetbrains.kotlin.test.helper.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.findDocument
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import org.jetbrains.kotlin.test.helper.TestDataType
import org.jetbrains.kotlin.test.helper.findProject
import org.jetbrains.kotlin.test.helper.getTestDataType

private const val DIAGNOSTICS_TEST_DATA_PATH = "/compiler/testData/diagnostics/tests"

private const val RUN_PIPELINE_TILL_FRONTEND = "// RUN_PIPELINE_TILL: FRONTEND"

internal fun String.isInDiagnosticsTestData(): Boolean {
    return DIAGNOSTICS_TEST_DATA_PATH in "$this/"
}

class NewDiagnosticsTestDataFileListener : BulkFileListener {
    override fun after(events: List<VFileEvent>) {
        val toUpdate = events.asSequence()
            .filterIsInstance<VFileCreateEvent>()
            .filter { !it.isFromRefresh && !it.isDirectory }
            .mapNotNull { event ->
                val file = event.file ?: return@mapNotNull null
                if (file.extension != "kt") return@mapNotNull null
                if (!file.path.isInDiagnosticsTestData()) return@mapNotNull null
                val project = file.findProject() ?: return@mapNotNull null
                if (file.getTestDataType(project) != TestDataType.File) return@mapNotNull null
                Pair(file, project)
            }
            .toList()

        if (toUpdate.isEmpty()) return

        ApplicationManager.getApplication().invokeLater {
            for ((file, project) in toUpdate) {
                if (project.isDisposed || !file.isValid) continue

                WriteCommandAction.runWriteCommandAction(project, "Insert RUN_PIPELINE_TILL Directive", null, {
                    val document = file.findDocument() ?: return@runWriteCommandAction
                    // Never touch a file which already arrived with some content.
                    if (document.text.isNotBlank()) return@runWriteCommandAction

                    document.insertString(0, "$RUN_PIPELINE_TILL_FRONTEND\n")
                })
            }
        }
    }
}
