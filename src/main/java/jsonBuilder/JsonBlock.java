package main.java.jsonBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.consumers.ConsumeType;

import java.lang.reflect.Field;

import static main.java.jsonBuilder.JsonBuilder.currentCategory;
import static main.java.jsonBuilder.JsonBuilder.description;

public class JsonBlock {
    public static Block referenceDefault = new Block("referenceDefault");

    public static JsonObject toJson(Block block) throws IllegalAccessException {
        JsonObject json = new JsonObject();
        json.addProperty("name", String.valueOf(block));
        json.addProperty("type", currentCategory);
        json.addProperty("description", description);
        json.addProperty("category", String.valueOf(block.category));
//        json.addProperty("type", block.type);
//        block.init();
        block.setBars();
        // requirements
        JsonArray requirements = new JsonArray();
        for (ItemStack item : block.requirements) {
            JsonObject itemObject = new JsonObject();
            itemObject.addProperty("item", item.item.name);
            itemObject.addProperty("amount", item.amount);
            requirements.add(itemObject);
        }
        json.add("requirements", requirements);

        // consumption
        JsonObject consumption = new JsonObject();
        if (block.consumes.has(ConsumeType.power)) {
            consumption.addProperty("power", String.valueOf(block.consumes.getPower().usage));
        }
        if (block.hasItems) {
            // items to consume:
            JsonObject items = new JsonObject();
            JsonArray items2 = new JsonArray();
            for (ItemStack item : block.consumes.getItem().items) {
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("item", item.item.name);
                itemObject.addProperty("amount", item.amount);
                items2.add(itemObject);
            }
            items.add("items", items2);
            consumption.add("items", items);
            json.add("consumes", consumption);
        }

        for (Field field : Block.class.getDeclaredFields()) {
            field.setAccessible(true);
//            System.out.println(field.getName()
//                    + " - " + field.getType()
//                    + " - " + field.get(block));
            if (field.get(referenceDefault) == field.get(block)) continue; // check if its default value
            if (isStandardType(field)) {
//                System.out.println(field.get(block) + " : is iterable, class: " + field.getType());
                continue;
            }
            json.addProperty(field.getName(), (field.get(block) == null ? "null" : field.get(block).toString()));
        }
//        System.out.println(json);
        return json;
    }

    // check if it's a standard type
    private static boolean isStandardType(Field field) {
        return field.getType() != String.class &&
                field.getType() != int.class &&
                field.getType() != boolean.class &&
                field.getType() != float.class &&
                field.getType() != arc.graphics.Color.class;
    }
}
