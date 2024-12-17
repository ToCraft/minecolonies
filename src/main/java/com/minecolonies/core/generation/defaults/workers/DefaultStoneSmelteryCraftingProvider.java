package com.minecolonies.core.generation.defaults.workers;

import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.core.generation.CustomRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Datagen for StoneSmeltery
 */
public class DefaultStoneSmelteryCraftingProvider extends CustomRecipeProvider {
    private static final String STONE_SMELTERY = ModJobs.STONE_SMELTERY_ID.getPath();

    public DefaultStoneSmelteryCraftingProvider(@NotNull final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @NotNull
    @Override
    public String getName() {
        return "DefaultStoneSmelteryCraftingProvider";
    }

    @Override
    protected void registerRecipes(@NotNull final Consumer<CustomRecipeBuilder> consumer) {
    }
}
