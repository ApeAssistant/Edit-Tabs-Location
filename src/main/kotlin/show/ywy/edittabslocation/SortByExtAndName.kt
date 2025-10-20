package show.ywy.edittabslocation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl
import com.intellij.openapi.vfs.VirtualFile

internal class SortByExtAndName : AnAction() {

    @Suppress("UnstableApiUsage")
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project) as? FileEditorManagerImpl ?: return
        // 获取当前 editor window
        val currentWindow: EditorWindow = fileEditorManager.currentWindow ?: return
        val allTabs = currentWindow.fileList.toList()
        // 找出未固定的文件（通过 currentWindow.isFilePinned）
        val unpinnedTabs = allTabs.filter { !currentWindow.isFilePinned(it) }
            .filter {
                it.isValid && it.isInLocalFileSystem &&
                        it.name.isNotBlank() &&
                        !it.name.contains("loading")
            }
        // 按扩展名和名称排序
        val sortedTabs = unpinnedTabs.sortedWith(
            compareBy<VirtualFile> { it.extension ?: "" }
                .thenBy { it.name }
        )
        // 关闭未固定的文件
        for (file in unpinnedTabs) {
            try {
                fileEditorManager.closeFile(file, currentWindow)
            } catch (e: Exception) {
                println("Error closing file: ${file.name}, error: ${e.message}")
            }
        }
        // 重新打开排序后的文件
        for (file in sortedTabs) {
            try {
                fileEditorManager.openFile(file)
            } catch (e: Exception) {
                println("Error opening file: ${file.name}, error: ${e.message}")
            }
        }
    }

}
