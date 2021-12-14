package main.java.jsonBuilder;

import mindustry.content.Items;
import mindustry.type.Item;
import mindustry.type.ItemStack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.java.jsonBuilder.JsonBuilder.*;

enum Types {
    requirements,
    consume
}

public class FieldList {

    final int maxFields = 20;
    public ArrayList<AddRemoveButton> buttonList = new ArrayList<>();
    JPanel content;
    JPanel listing;
    Types currentType;

    public FieldList(JPanel content, String Name, Types type) {
        content.add(new JLabel(Name, JLabel.LEFT), BorderLayout.CENTER);

        listing = new JPanel();
        listing.setLayout(new BoxLayout(listing, BoxLayout.Y_AXIS));

        content.add(listing, BorderLayout.CENTER);

        addNewField();
        this.content = content;
        this.currentType = type;
    }

    public void update() {
        for (AddRemoveButton button : buttonList) {
            // update currentBlock
            updateReq(button);
        }
    }

    void addNewField() {
        FieldButtonPair field = new FieldButtonPair();
        buttonList.add(field.button);
        field.button.addActionListener((ae) -> {
            AddRemoveButton source = (AddRemoveButton) ae.getSource();
            if (source.state == AddRemoveButton.State.ADD) {
                if (listing.getComponentCount() < maxFields) {
                    // update currentBlock
                    updateReq(source);
                    addNewField();
                    source.setState(AddRemoveButton.State.REMOVE);
                }
            } else if (source.state == AddRemoveButton.State.REMOVE) {
                removeItem(source);
                buttonList.remove(source);
//                removeLastField();
                source.setState(AddRemoveButton.State.ADD);
                listing.remove(field);
            }
        });
        listing.add(field);
    }

    private void removeItem(AddRemoveButton button) {
        HashMap<Item, Integer> currentRequirements = getItemList();
        try {
            Item currentItem = (Item) Items.class.getDeclaredField(button.nameField.getText()).get(null);
            currentRequirements.remove(currentItem);
            setRequirements(currentRequirements);
        } catch (Exception e) {
            printError(e);
        }
    }

    private void setRequirements(HashMap<Item, Integer> currentRequirements) {
        ItemStack[] req = new ItemStack[currentRequirements.size()];
        int index = 0;
        for (Map.Entry<Item, Integer> mapEntry : currentRequirements.entrySet()) {
            req[index] = new ItemStack();
            req[index].item = mapEntry.getKey();
            req[index].amount = mapEntry.getValue();
            index++;
        }
        if (currentType == Types.requirements) {
            currentBlock.requirements(currentBlock.category, req);
        } else if (currentType == Types.consume) {
            currentBlock.hasItems = currentRequirements.size() > 0;
            consumes = currentRequirements;
            currentBlock.consumes.items(req);
        }
    }

    void removeLastField() {
        listing.remove(listing.getComponentCount() - 1);
    }

    public JPanel build() {
        return content;
    }

    private void updateReq(AddRemoveButton button) {
        HashMap<Item, Integer> currentRequirements = getItemList();
        String itemText = button.nameField.getText();
        if (Objects.equals(itemText, "")) return;
        try {
            Item currentItem = (Item) Items.class.getDeclaredField(itemText).get(null);
            if (!currentRequirements.containsKey(currentItem)) {
                currentRequirements.put(currentItem, Integer.valueOf(button.amountField.getText()));
            } else {
                currentRequirements.replace(currentItem, currentRequirements.get(currentItem) + Integer.parseInt(button.amountField.getText()));
            }
            setRequirements(currentRequirements);
        } catch (NoSuchFieldException n) {
            errorLog.setText("Cant find item: " + itemText);
        } catch (Exception e) {
            e.printStackTrace();
            printError(e);
        }
    }

    private HashMap<Item, Integer> getItemList() {
        HashMap<Item, Integer> currentRequirements = new HashMap<>();
        if (currentType == Types.requirements) {
            for (ItemStack itemStack : currentBlock.requirements) {
                currentRequirements.put(itemStack.item, itemStack.amount);
            }
        } else {
            currentRequirements = consumes;
        }
        return currentRequirements;
    }
}

class FieldButtonPair extends JPanel {
    JTextField nameField;
    JTextField amountField;
    AddRemoveButton button;

    FieldButtonPair() {
        super(new BorderLayout());
        setLayout(new GridLayout(1, 3));
        nameField = new JTextField();
        add(nameField, BorderLayout.WEST);
        amountField = new JTextField();
        add(amountField, BorderLayout.CENTER);
        button = new AddRemoveButton(nameField, amountField);
        add(button, BorderLayout.EAST);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension pref = super.getPreferredSize();
        pref.width = Math.max(480, pref.width);
        return pref;
    }
}

class AddRemoveButton extends JButton {
    public JTextField nameField;
    public JTextField amountField;
    State state = State.ADD;

    AddRemoveButton(JTextField nameField, JTextField amountField) {
        setText(state.name());
        this.nameField = nameField;
        this.amountField = amountField;
    }

    void setState(State state) {
        setText(state.name());
        this.state = state;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension pref = super.getPreferredSize();

        Font f = getFont();
        FontMetrics fm = getFontMetrics(f);
        int w = fm.stringWidth(State.REMOVE.name());
        Insets ins = getInsets();

        pref.width = (ins.left + w + ins.right);
        return pref;
    }

    enum State {ADD, REMOVE}
}