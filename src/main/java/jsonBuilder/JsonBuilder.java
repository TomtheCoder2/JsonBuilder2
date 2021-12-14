package main.java.jsonBuilder;

import arc.files.Fi;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.core.ContentLoader;
import mindustry.core.Version;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawWeave;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static main.java.jsonBuilder.Editor.*;
import static mindustry.type.ItemStack.with;

public class JsonBuilder {
    public static int[] windowSize = {3 * 375, 3 * 250};
    public static Preview preview;
    public static JEditorPane editorPane;
    public static Block currentBlock;
    public static String currentCategory = "StaticWall";
    public static String description = "";
    public static JEditorPane errorLog;
    public static Editor editor;
    public static HashMap<Item, Integer> consumes = new HashMap<>();


    public static void main(String[] args) throws IllegalAccessException {
        System.out.println("Start Application...");
        //clear cache
        new Fi("cache").deleteDirectory();

        // initiation
        Version.enabled = false;
        Vars.content = new ContentLoader();
        Vars.content.createBaseContent();

        JFrame frame = new JFrame("Json Builder");// new window
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close with X button
        frame.setSize(3 * 350, 3 * 250);


//        final Gson gson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation()
////                .excludeFieldsWithModifiers(TRANSIENT) // STATIC|TRANSIENT in the default configuration
//                .create();
        currentBlock = new GenericCrafter("multi-press2") {{
            requirements(Category.crafting, with(Items.silicon, 130, Items.lead, 120, Items.thorium, 75));
            craftEffect = Fx.smeltsmoke;
            outputItem = new ItemStack(Items.phaseFabric, 1);
            craftTime = 120f;
            size = 2;
            hasPower = true;
            drawer = new DrawWeave();

            ambientSound = Sounds.techloop;
            craftEffect = Fx.pulverize;
            ambientSoundVolume = 0.02f;

            consumes.items(with(Items.thorium, 4, Items.sand, 10));
            consumes.power(5f);
            itemCapacity = 30;
        }};


// ========================== GUI ===================================
        System.out.println("create GUI...");
        // set icon
        Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\janwi\\java\\jsonBuilder2\\assets\\icons\\icon_coreNucleus.png");
        frame.setIconImage(icon);

        // Create a split pane with the two scroll panes in it.
        // left side
        JPanel editorScrollPane = new JPanel();
        BoxLayout boxlayout = new BoxLayout(editorScrollPane, BoxLayout.Y_AXIS);
        editorScrollPane.setLayout(boxlayout);
        editorScrollPane.add(new Editor().build());
        FieldList reqList = new FieldList(new JPanel(), "Requirements", Types.requirements);
        FieldList consumeList = new FieldList(new JPanel(), "Consumes", Types.consume);
        editorScrollPane.add(reqList.build());
        editorScrollPane.add(consumeList.build());
        Dimension minimumSize = new Dimension(windowSize[0] / 2, windowSize[1]);
        editorScrollPane.setMinimumSize(minimumSize);
        editorPane = new JEditorPane();
        JScrollPane previewScrollPane = new JScrollPane(editorPane);
        JPanel ep = new JPanel();
        ep.setLayout(new BorderLayout());
        JScrollPane mainEditorPane = new JScrollPane(editorScrollPane);


        // update button (bottom right in mainEditorPane)
        JButton updateButton = new JButton();
        updateButton.setText("Update");
//        updateButton.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(updateButton);
        updateButton.addActionListener((arg) -> {
            // reset error message
            errorLog.setText(" ");
            try {
                // update all variables
                description = descriptionField.getText();
                currentBlock.size = Integer.parseInt(size.getText());
                currentBlock.itemCapacity = Integer.parseInt(itemCapacity.getText());
                currentBlock.hasPower = Boolean.parseBoolean(hasPower.getText());
                currentBlock.hasItems = Boolean.parseBoolean(hasItems.getText());
                currentBlock.category = Category.valueOf(categoryField.getText());
                currentBlock.health = Integer.parseInt(health.getText());
                reqList.update();
                consumeList.update();

                // update graphics
                update();
            } catch (Exception e) {
                printError(e);
            }
        });
        ep.add(buttonPanel, BorderLayout.SOUTH);
        ep.add(mainEditorPane);

        // main split pane (editor and error log)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                ep, previewScrollPane);

        editor = new Editor(); // create a new editor
        preview = new Preview(currentBlock); // preview the default block (multi-press2)
        preview.update(currentBlock, editorPane); // update preview

        MainLoop loop = new MainLoop(frame, editor, Thread.currentThread()); // thread (doesnt work, cause main thread closes first)
//        loop.setDaemon(false);
        loop.start();
        System.out.println("MainLoop...");

        splitPane.setMinimumSize(new Dimension(windowSize[0], windowSize[1] * 3 / 4)); // set size main pane to fill 1/4 with error log pnale
        errorLog = new JEditorPane(); // pane for error logs
        JScrollPane errorLogPane = new JScrollPane(errorLog);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                splitPane, errorLogPane);

        frame.add(mainSplitPane);

        frame.setSize(windowSize[0], windowSize[1]); // set windows size to fixed var
//        frame.pack();
        frame.setVisible(true);
        // main thread
        schedule(editor);
    }

    public static void printError(Exception e) {
        e.printStackTrace();
        errorLog.setText("Error: " + e.getLocalizedMessage());
    }

    public static void update() throws IllegalAccessException, NumberFormatException { // idk why seperate function
        editor.updateData();
        preview.update(currentBlock, editorPane);
    }

    public static void schedule(Editor editor) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                update();
            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//                errorLog.setText(e.getMessage());
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
}
