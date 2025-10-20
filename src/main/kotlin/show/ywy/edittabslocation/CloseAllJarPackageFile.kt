package show.ywy.edittabslocation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile

internal class CloseAllJarPackageFile : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)

        val openFiles = fileEditorManager.openFiles
        val toClose = openFiles.filter { file ->
            isFromJarOrLibrary(file) && isJavaOrClassFile(file)
        }

        toClose.forEach { file ->
            try {
                fileEditorManager.closeFile(file)
            } catch (ex: Exception) {
                println("Error closing file: ${file.name}, error: ${ex.message}")
            }
        }
    }

    private fun isFromJarOrLibrary(file: VirtualFile): Boolean {
        return !file.isInLocalFileSystem
    }

    private fun isJavaOrClassFile(file: VirtualFile): Boolean {
        val ext = file.extension?.lowercase()
        return ext == "java" || ext == "class"
    }
}
