package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import org.jetbrains.annotations.NotNull;

public class GoToTestAction extends BaseCodeInsightAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final Project project = e.getProject();

        if (editor == null || project == null) {
            return;
        }

        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }

        this.actionPerformedImpl(project, editor);
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new GoToTestHandler();
    }

    @Override
    public void update(AnActionEvent event) {
        Presentation p = event.getPresentation();
        p.setEnabled(true);
        // todo: implement disabling logic if it is impossible to test current file.
    }
}