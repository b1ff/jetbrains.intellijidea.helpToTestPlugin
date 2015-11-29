package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import main.java.org.helpToTest.intellij.plugin.utils.TestFilesHelper;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class CreateTestAction extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        AddTestDialog d = new AddTestDialog(project);
        if (d.showAndGet()){
            CommandProcessor.getInstance().executeCommand(project, () -> {
                PsiFile file = ApplicationManager.getApplication().runWriteAction((Computable<PsiFile>) () -> {
                    return generateTest(project, psiElement.getContainingFile(), d);
                });
                file.navigate(true);
                final PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
                documentManager.commitAllDocuments();
            }, CodeInsightBundle.message("intention.create.test"), this);
        }
        else {
            Messages.showInfoMessage("Test is not created", "Result");
        }
    }

    @Nullable
    private PsiFile generateTest(Project project, PsiFile psiFile, AddTestDialog dialog) {
        String testFileName = TestFilesHelper.getExpectedTestName(psiFile);
        String pathToTest = dialog.getDirectory() + "/" + testFileName;
        final File file = new File(pathToTest);

        try {
            VirtualFile baseDir = project.getBaseDir();
            String dirPath = StringUtil.notNullize(
                    file.getParent(),
                    baseDir != null ? baseDir.getPath() : "."
            );
            PsiDirectory dir = createDirectories(project, dirPath);
            if (dir != null) {
                PsiFile existingFile = dir.findFile(pathToTest);
                if (existingFile == null) {
                    return dir.createFile(file.getName());
                }
            }
            else {
                throw new IncorrectOperationException(String.format("Cannot create file '%s'", pathToTest));
            }

        } catch (IOException e) {
            throw new IncorrectOperationException(String.format("Cannot create file '%s'", pathToTest));
        }
        return null;
    }

    @Nullable
    private static PsiDirectory createDirectories(Project project, String target) throws IOException {
        String the_rest = null;
        VirtualFile the_root = null;
        PsiDirectory ret = null;

        // NOTE: we don't canonicalize target; must be ok in reasonable cases, and is far easier in unit test mode
        target = FileUtil.toSystemIndependentName(target);
        for (VirtualFile file : ProjectRootManager.getInstance(project).getContentRoots()) {
            final String root_path = file.getPath();
            if (target.startsWith(root_path)) {
                the_rest = target.substring(root_path.length());
                the_root = file;
                break;
            }
        }
        if (the_root == null) {
            throw new IOException("Can't find '" + target + "' among roots");
        }

        final LocalFileSystem lfs = LocalFileSystem.getInstance();
        final PsiManager psi_mgr = PsiManager.getInstance(project);
        String[] dirs = the_rest.split("/");
        int i = 0;
        if ("".equals(dirs[0])) i = 1;
        while (i < dirs.length) {
            VirtualFile subdir = the_root.findChild(dirs[i]);
            if (subdir != null) {
                if (!subdir.isDirectory()) {
                    throw new IOException("Expected dir, but got non-dir: " + subdir.getPath());
                }
            }
            else {
                subdir = the_root.createChildDirectory(lfs, dirs[i]);
            }

            the_root = subdir;
            i += 1;
        }
        ret = psi_mgr.findDirectory(the_root);
        return ret;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        PsiFile file = psiElement.getContainingFile();
        return TestFilesHelper.isTestFile(file);
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return getText();
    }

    @NotNull
    @Override
    public String getText() {
        return CodeInsightBundle.message("intention.create.test");
    }
}
