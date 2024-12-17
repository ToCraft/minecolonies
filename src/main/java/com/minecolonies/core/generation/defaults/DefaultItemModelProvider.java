package com.minecolonies.core.generation.defaults;

import com.minecolonies.api.items.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.minecolonies.api.util.constant.Constants.MOD_ID;

/**
 * Datagen for item models
 */
public class DefaultItemModelProvider extends ItemModelProvider {
    /**
     * Constructor
     */
    public DefaultItemModelProvider(final PackOutput packOutput, final ExistingFileHelper existingFileHelper) {
        super(packOutput, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        final ResourceLocation disabledGoggles = modLoc("build_goggles_disabled");
        basicItem(disabledGoggles);
        basicItem(ModItems.buildGoggles)
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("disabled"), 1.0F)
                .model(getExistingFile(disabledGoggles))
                .end();

        for (final Item foodItem : ModItems.getAllIngredients()) {
            getBuilder(foodItem.toString())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", new ResourceLocation(MOD_ID, "item/food/" + BuiltInRegistries.ITEM.wrapAsHolder(foodItem).getKey().location().getPath()));
        }

        for (final Item foodItem : ModItems.getAllFoods()) {
            getBuilder(foodItem.toString())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", new ResourceLocation(MOD_ID, "item/food/" + BuiltInRegistries.ITEM.wrapAsHolder(foodItem).getKey().location().getPath()));
        }
    }
}
