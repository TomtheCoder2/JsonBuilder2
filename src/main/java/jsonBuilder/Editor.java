package main.java.jsonBuilder;

import mindustry.Vars;
import mindustry.core.ContentLoader;
import mindustry.type.Category;
import mindustry.world.blocks.defense.Wall;

import javax.swing.*;
import java.awt.*;

import static main.java.jsonBuilder.JsonBlock.referenceDefault;
import static main.java.jsonBuilder.JsonBuilder.*;

public class Editor {
    private static final int N = 9;
    public static JComboBox categoriesBox;
    public static JTextField textField; // name field
    public static JTextField descriptionField;
    public static JTextField size;
    public static JTextField itemCapacity;
    public static JTextField hasPower;
    public static JTextField hasItems;
    public static JTextField categoryField;
    public static JTextField health;
    private final String[] categoriesList = {"Attribute Crafter", "StaticWall", "Floor", "Generic Crafter"}; // currently all available categories


    public Editor() {
    }

    public JPanel build() {
        // initialization
        textField = new JFormattedTextField();
        descriptionField = new JTextField();
        size = new JTextField();
        itemCapacity = new JTextField();
        hasPower = new JTextField();
        hasItems = new JTextField();
        categoryField = new JTextField();
        health = new JTextField();


        // main editor pane
        JPanel panel = new JPanel();
//        panel.setSize(500, 500);
        panel.setPreferredSize(new Dimension(500, 50 * N));


        panel.setLayout(new GridLayout(N, 1));
//        panel.setBounds(80, 50, 140, 20);


        JPanel namePanel = createField("Name", "Wall1", (evt) -> {
            String category = (String) categoriesBox.getSelectedItem();
            assert category != null;
            reload(category);
        }, textField, "String");


        JPanel categoriesPanel = new JPanel(); // panel for the category select box
        categoriesPanel.setLayout(new GridLayout(1, 4));
        categoriesBox = new JComboBox(categoriesList);
        categoriesBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 5)); // padding
        categoriesBox.setSelectedIndex(0); // default category
        categoriesBox.addActionListener((evt) -> { // select categories
            JComboBox cb = (JComboBox) evt.getSource();
            String category = (String) cb.getSelectedItem();
            assert category != null;
            System.out.println(category);
            reload(category);
        });
        JLabel categoryLabel = new JLabel(); // description text
        categoryLabel.setText("Type:");
        // add both to the grid
        categoriesPanel.add(categoryLabel);
        categoriesPanel.add(categoriesBox);
        categoriesPanel.add(new JLabel()); // for aesthetics
        panel.add(categoriesPanel); // add the category to the main panel
        panel.add(namePanel); // same thing

        panel.add(createField("Description", "", (evt) -> {
            JTextField descField = (JTextField) evt.getSource();
            description = descField.getText();
        }, descriptionField, "String"));

        panel.add(createField("Size", "0", (evt) -> {
            try {
                currentBlock.size = Integer.parseInt(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, size, "Int"));

        panel.add(createField("Item Capacity", "0", (evt) -> {
            try {
                currentBlock.itemCapacity = Integer.parseInt(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, itemCapacity, "Int"));

        panel.add(createField("Has Power", "false", (evt) -> {
            try {
                currentBlock.hasPower = Boolean.parseBoolean(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, hasPower, "Boolean"));

        panel.add(createField("Has Items", "false", (evt) -> {
            try {
                currentBlock.hasItems = Boolean.parseBoolean(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, hasItems, "Boolean"));

        panel.add(createField("category", String.valueOf(referenceDefault.category), (evt) -> {
            try {
                currentBlock.category = Category.valueOf(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, categoryField, "String"));

        panel.add(createField("Health", String.valueOf(referenceDefault.health), (evt) -> {
            try {
                currentBlock.health = Integer.parseInt(((JTextField) evt.getSource()).getText());
                updateData();
            } catch (Exception e) {
                printError(e);
            }
        }, health, "int"));

        return panel;
    }

    private void reload(String category) {
        Vars.content.clear();
        Vars.content = new ContentLoader();
        currentBlock = DefaultBlock.getDefaultBlock();
        JsonBuilder.currentBlock = new Wall(textField.getText()) {
        };
//        switch (category) {
//            case "StaticWall" -> {
//                JsonBuilder.currentBlock = new Wall(textField.getText()) {
//                };
//            }
//            case "Floor" -> {
//                JsonBuilder.currentBlock = new Floor(textField.getText()) {
//                };
//            }
//            case "Generic Crafter" -> {
//                JsonBuilder.currentBlock = new GenericCrafter(textField.getText()) {
//                };
//            }
//            case "Attribute Crafter" -> {
//                currentBlock = new Floor(textField.getText());
//            }
//        }
        currentCategory = category;
        Vars.content.createBaseContent();
        currentBlock.init();
        System.out.println(currentBlock);
    }

    /**
     * Create an input field with description in front of it.
     *
     * @param name           Name of the Field
     * @param defaultText    default text of the input field
     * @param actionListener lambda function for the inputTextField
     * @param textField      textField to append to the panel
     * @param typeDescText   Description to put in front of the inputField
     */
    private JPanel createField(String name, String defaultText, java.awt.event.ActionListener
            actionListener, JTextField textField, String typeDescText) {
        JPanel panel = new JPanel(); // panel for the field
        panel.setLayout(new GridLayout(1, 4));
//        textField.setSize(500, 50); // doesnt work, idk
        textField.setText(defaultText); // default value
        textField.addActionListener(actionListener);
        textField.setToolTipText(name);
        JLabel label = new JLabel(); // info for the field
        label.setText(name);
        panel.add(label); // add the label to the grid
        panel.add(textField); // add the text field to the grid
        JLabel typeDesc = new JLabel();
        typeDesc.setText("(" + typeDescText + ")");
        panel.add(new JPanel().add(typeDesc)); // acts like a border
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // padding
        return panel;
    }

    public void updateData() throws NumberFormatException {
        currentBlock.description = descriptionField.getText();
//        currentBlock.size = Integer.parseInt(size.getText());
//        currentBlock.itemCapacity = Integer.parseInt(itemCapacity.getText());
//        currentBlock.hasPower = Boolean.parseBoolean(hasPower.getText());
//        currentBlock.hasItems = Boolean.parseBoolean(hasItems.getText());
//        currentBlock.category = Category.valueOf(categoryField.getText());
//        currentBlock.health = Integer.parseInt(health.getText());
    }
}
