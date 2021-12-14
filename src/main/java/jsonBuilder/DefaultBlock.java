package main.java.jsonBuilder;

import arc.graphics.Color;
import arc.scene.Group;
import arc.struct.EnumSet;
import arc.struct.ObjectMap;
import mindustry.content.Fx;
import mindustry.entities.TargetPriority;
import mindustry.gen.Sounds;
import mindustry.graphics.CacheLayer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.Attributes;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;

public class DefaultBlock {
    public static Block getDefaultBlock() {
        return new Block("default") {
            {
                outputsLiquid = false;
                consumesPower = true;
                outputsPower = false;
                outputsPayload = false;
                acceptsPayload = false;
                acceptsItems = false;
                separateItemCapacity = false;
                itemCapacity = 10;
                liquidCapacity = 10f;
                liquidPressure = 1f;
                outputFacing = true;
                noSideBlend = false;
                displayFlow = true;
                inEditor = true;
                saveConfig = false;
                copyConfig = true;
                unloadable = true;
                allowResupply = false;
                variants = 0;
                drawArrow = true;
                rebuildable = true;
                requiresWater = false;
                placeableLiquid = false;
                placeablePlayer = true;
                placeableOn = true;
                insulated = false;
                squareSprite = true;
                absorbLasers = false;
                enableDrawStatus = true;
                drawDisabled = true;
                autoResetEnabled = true;
                noUpdateDisabled = false;
                updateInUnits = true;
                useColor = true;
                itemDrop = null;
                attributes = new Attributes();
                health = -1;
                baseExplosiveness = 0f;
                destroyBullet = null;
                floating = false;
                size = 1;
                offset = 0f;
                expanded = false;
                clipSize = -1f;
                timers = 0;
                cacheLayer = CacheLayer.normal;
                fillsTile = true;
                alwaysReplace = false;
                replaceable = true;
                group = BlockGroup.none;
                flags = EnumSet.of();
                priority = TargetPriority.base;        /** How much this affects the unit cap by.         * The flags must contain unitModifier in order for this to work. */
                unitCapModifier = 0;
                allowConfigInventory = true;
                logicConfigurable = false;

                drawLiquidLight = true;
                envRequired = 0;
                envEnabled = Env.terrestrial;
                envDisabled = 0;
                schematicPriority = 0;        /**         * The color of this when displayed on the minimap or map preview.         * Do not set manually! This is overridden when loading for most .         */
                mapColor = new Color(0, 0, 0, 1);
                hasColor = false;
                targetable = true;
                canOverdrive = true;
                outlineColor = Color.valueOf("404049");
                outlineIcon = false;
                outlineRadius = 4;
                outlinedIcon = -1;
                hasShadow = true;
                placePitchChange = true;
                breakPitchChange = true;
                placeSound = Sounds.place;
                breakSound = Sounds.breaks;
                destroySound = Sounds.boom;
                albedo = 0f;
                lightColor = Color.white.cpy();        /**         * Whether this environmental passively emits light.         * Does not change behavior for non-environmental , but still updates clipSize. */
                emitLight = false;
                lightRadius = 60f;
                loopSound = Sounds.none;
                loopSoundVolume = 0.5f;
                ambientSound = Sounds.none;
                ambientSoundVolume = 0.05f;
                requirements = new ItemStack[]{};
                category = Category.distribution;
                buildCost = 20f;
                buildVisibility = BuildVisibility.hidden;
                buildCostMultiplier = 1f;
                deconstructThreshold = 0f;
                instantDeconstruct = false;
                breakEffect = Fx.breakBlock;
                researchCostMultiplier = 1;
                instantTransfer = false;
                quickRotate = true;
                highUnloadPriority = false;
                buildType = null;
                configurations = new ObjectMap<>();
            }
        };
    }
}