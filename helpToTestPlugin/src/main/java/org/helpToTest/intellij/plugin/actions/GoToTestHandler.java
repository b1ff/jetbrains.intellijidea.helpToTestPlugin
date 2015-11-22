package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SmartList;
import main.java.org.helpToTest.intellij.plugin.utils.TestFilesHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class GoToTestHandler extends GotoTargetHandler {

    private static final Logger LOG = Logger.getInstance("main.java.org.helpToTest.intellij.plugin.actions.GoToTestHandler");

    @Override
    protected String getFeatureUsedKey() {
        return "org.moreunit.actions.goToTest";
    }

    private static GotoData emptyData(PsiElement el) {
        return new GotoData(el, new PsiElement[0], Collections.emptyList());
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(Editor editor, PsiFile psiFile) {
        if (psiFile == null) {
            return null;
        }

        if (TestFilesHelper.isTestFile(psiFile) || isTestSelected(GotoTestOrCodeHandler.getSelectedElement(editor, psiFile))) {
            // todo: go to source
        } else {
            PsiFile[] files = TestFilesHelper.findTestCandidates(psiFile);
            if (files.length > 0) {
                return new GotoTargetHandler.GotoData(psiFile, files, Collections.emptyList());
            } else {
                List<AdditionalAction> actions = new SmartList<>();
                actions.add(new AdditionalAction() {
                    @NotNull
                    @Override
                    public String getText() {
                        return "Create New Test...";
                    }

                    @Override
                    public Icon getIcon() {
                        return AllIcons.Actions.IntentionBulb;
                    }

                    @Override
                    public void execute() {
                        createTest(psiFile.getProject(), editor, psiFile);
                    }
                });

                return new GotoData(psiFile, files, actions);
                // todo: create test dialog
            }
        }

        return emptyData(psiFile);
    }

    private boolean isTestSelected(PsiElement selectedElement) {
        return false;
    }

    private static void createTest(Project project, Editor editor, PsiFile file){
        try {
            CreateTestAction action = new CreateTestAction();
            action.invoke(project, editor, file.getContainingFile());
        }
        catch (IncorrectOperationException e) {
            LOG.warn(e);
        }
    }

    @NotNull
    @Override
    protected String getChooserTitle(PsiElement psiElement, String s, int i) {
        return CodeInsightBundle.message("goto.test.chooserTitle.test", s, i);
    }

    @NotNull
    @Override
    protected String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
        return CodeInsightBundle.message("goto.test.notFound");
    }
}