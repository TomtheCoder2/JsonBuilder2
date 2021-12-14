package main.java.jsonBuilder;

import javax.swing.*;

import static main.java.jsonBuilder.JsonBuilder.update;

public class MainLoop extends Thread {
    private Thread mt;
    private JFrame frame;
    private Editor editor;

    public MainLoop(JFrame frame, Editor editor, Thread thread) {
        this.frame = frame;
        this.editor = editor;
        this.mt = thread;
    }

    public void run() {
        while (this.mt.isAlive()) {
            try {
                Thread.sleep(1);
//                currentBlock.name = editor.nameTextField.getText(); // TODO: add action listener to nameTextField
//                System.out.println("update");
                update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("stop thread");
    }
}
