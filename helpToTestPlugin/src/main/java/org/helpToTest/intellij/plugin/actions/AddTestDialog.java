package main.java.org.helpToTest.intellij.plugin.actions;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTestDialog extends DialogWrapper {
    private JPanel contentPane;
    private TextFieldWithBrowseButton testDirectory;

    public AddTestDialog(Project project) {
        super(project, true);
        setTitle("Select a directory for a test");

        testDirectory.addBrowseFolderListener("Select target directory", null, project,
                FileChooserDescriptorFactory.createSingleFolderDescriptor());
        testDirectory.setEditable(false);


        testDirectory.addActionListener(actionEvent -> getOKAction().setEnabled(isValid()));

        init();
    }

    public String getDirectory() {
        return this.testDirectory.getText().trim();
    }

    private boolean isValid() {
        return !StringUtil.isEmptyOrSpaces(getDirectory());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
