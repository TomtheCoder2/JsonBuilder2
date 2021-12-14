package main.java.jsonBuilder;

import com.google.gson.*;
import mindustry.world.Block;

import javax.swing.*;
import java.util.Objects;

import static main.java.jsonBuilder.JsonBlock.toJson;

public class Preview {
    public JsonObject lastBlock = new JsonObject();

    public Preview(Block block) {
    }

    public void update(Block block, JEditorPane EPane) throws IllegalAccessException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject json = toJson(block);
        JsonElement el = JsonParser.parseString(json.toString());

        // check if the block has changed
        if (!Objects.equals(json.toString(), lastBlock.toString())) {
            System.out.println("update text field...");
            EPane.setText(gson.toJson(el));
            lastBlock = json;
        }
    }
}
