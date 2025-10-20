package show.ywy.edittabslocation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile

internal class SortByExtAndName : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project) ?: return
        val allTabs = fileEditorManager.openFiles.toList()

        // 过滤有效文件（移除 pinned 检查，因为无公共 API）
        val tabsToSort = allTabs.filter {
            it.isValid && it.isInLocalFileSystem &&
                    it.name.isNotBlank() &&
                    !it.name.contains("loading")
        }

        // 按扩展名和名称排序
        val sortedTabs = tabsToSort.sortedWith(
            compareBy<VirtualFile> { it.extension ?: "" }
                .thenBy { it.name }
        )

        // 关闭要排序的文件
        for (file in tabsToSort) {
            try {
                fileEditorManager.closeFile(file)  // 使用公共 closeFile（不指定窗口）
            } catch (ex: Exception) {
                println("Error closing file: ${file.name}, error: ${ex.message}")
            }
        }

        // 重新打开排序后的文件
        for (file in sortedTabs) {
            try {
                fileEditorManager.openFile(file)  // 使用公共 openFile（默认当前窗口）
            } catch (ex: Exception) {
                println("Error opening file: ${file.name}, error: ${ex.message}")
            }
        }
    }
}
