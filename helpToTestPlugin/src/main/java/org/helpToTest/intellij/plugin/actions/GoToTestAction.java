package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.DocCommandGroupId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by eugene on 15.11.2015.
 */
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
//        final PsiFile psiFile = file;
//        CommandProcessor.getInstance().executeCommand(project, () -> {
//            final CodeInsightActionHandler handler = GoToTestAction.this.getHandler();
//            Runnable action = () -> {
//                if(ApplicationManager.getApplication().isUnitTestMode() || editor.getContentComponent().isShowing()) {
//                    handler.invoke(project, editor, psiFile);
//                }
//            };
//            if(handler.startInWriteAction()) {
//                ApplicationManager.getApplication().runWriteAction(action);
//            } else {
//                action.run();
//            }
//
//        }, this.getCommandName(), DocCommandGroupId.noneGroupId(editor.getDocument()));
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new GoToTestHandler();
    }



}
