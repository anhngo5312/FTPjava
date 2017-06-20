package Model;

import java.io.File;
import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;

public class Tree implements Serializable {

    public File getFileRoot() {
        return FileRoot;
    }

    public void setFileRoot(File FileRoot) {
        this.FileRoot = FileRoot;
    }

    public DefaultMutableTreeNode getRoot() {
        return Root;
    }

    public void setRoot(DefaultMutableTreeNode Root) {
        this.Root = Root;
    }

    public Tree(File FileRoot, DefaultMutableTreeNode Root) {
        this.FileRoot = FileRoot;
        this.Root = Root;
        createChildren(FileRoot, Root);
    }

    public void createChildren(File fileRoot, DefaultMutableTreeNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory() && (file.getName().charAt(0) != '$') && (file.getName().charAt(0) != '.')) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                node.add(childNode);
                createChildren(file, childNode);
            }
        }
    }
    private File FileRoot;
    private DefaultMutableTreeNode Root;
}
