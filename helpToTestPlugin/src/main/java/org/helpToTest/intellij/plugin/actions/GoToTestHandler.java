package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class GoToTestHandler extends GotoTargetHandler {

    @Override
    protected String getFeatureUsedKey() {
        return "org.moreunit.actions.goToTest";
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(Editor editor, PsiFile psiFile) {
        String currentFileName = psiFile.getName();
        Project project = editor.getProject();
        if (project == null) {
            return null;
        }

        Messages.showInfoMessage(project, currentFileName, "The File");
        String ext = getExtension(currentFileName);
        String withoutExt = currentFileName.replace(ext, "") + "spec." + ext;
        PsiFile[] files = PsiShortNamesCache.getInstance(project).getFilesByName(withoutExt);
        if (files.length > 0) {
            return new GotoTargetHandler.GotoData(psiFile, files, Collections.emptyList());
        }

        return null;
//        PsiDirectory testDir = DirectoryIndex.getInstance(project).getDirectoriesByPackageName()
    }



    public static String getExtension(String fileName) {
        String ext = null;
        String s = fileName;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
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
