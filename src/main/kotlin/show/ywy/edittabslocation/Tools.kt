package show.ywy.edittabslocation

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.ui.tabs.JBTabsPosition

class Tools {

    companion object {
        @JvmStatic
        fun changeTabsLocation(project: Project?, position: JBTabsPosition) {
            ApplicationManager.getApplication().invokeLater {
                ApplicationManager.getApplication().runWriteAction {
                    val uiSettings = UISettings.getInstance()
                    println("Changing tabs location to: $position")
                    // 将 JBTabsPosition 映射为 UISettings 的枚举值
                    val newPlacement = when (position) {
                        JBTabsPosition.top -> 1
                        JBTabsPosition.left -> 2
                        JBTabsPosition.right -> 3
                        JBTabsPosition.bottom -> 4
                    }
                    // 设置主编辑器 tab 的位置
                    uiSettings.editorTabPlacement = newPlacement
                    uiSettings.fireUISettingsChanged()
                }
            }
        }
    }
}
