package main.java.org.helpToTest.intellij.plugin.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestFilesHelper {

    public static PsiFile[] findTestCandidates(@NotNull PsiFile psiFile) {
        String expectedTestFileName = getExpectedTestName(psiFile);
        Project project = psiFile.getProject();
        return FilenameIndex.getFilesByName(project, expectedTestFileName, GlobalSearchScope.allScope(project));
    }

    @NotNull
    public static String getExpectedTestName(@NotNull PsiFile psiFile) {
        String currentFileName = psiFile.getName();

        String ext = getExtension(currentFileName);
        return currentFileName.replace(ext, "") + "spec." + ext;
    }

    public static String getExtension(String fileName) {
        String ext = null;
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            ext = fileName.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static boolean isTestFile(PsiFile psiFile) {
        String fileName = psiFile.getName();
        for (String part : testConventions) {
            if (fileName.contains(part)) {
                return true;
            }
        }

        return false;
    }

    private static final List<String> testConventions = new SmartList<>();
    static {
        testConventions.add(".spec.");
        testConventions.add(".specs.");
        testConventions.add(".test.");
    }
}
