package show.ywy.edittabslocation

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.tabs.JBTabsPosition

internal class ChangeTabsToTop : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
    override fun actionPerformed(e: AnActionEvent) {
        Tools.changeTabsLocation(e.project, JBTabsPosition.top)
    }

    override fun update(e: AnActionEvent) {
        // 确保动作在有项目上下文时可用
        e.presentation.isEnabled = e.project != null
    }
}
