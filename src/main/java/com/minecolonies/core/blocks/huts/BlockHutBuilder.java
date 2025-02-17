package com.minecolonies.core.blocks.huts;

import com.minecolonies.api.blocks.AbstractBlockHut;
import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import org.jetbrains.annotations.NotNull;

/**
 * Hut for the builder. No different from {@link AbstractBlockHut}
 */
public class BlockHutBuilder extends AbstractBlockHut<BlockHutBuilder> {
    public BlockHutBuilder() {
        //No different from Abstract parent
        super();
    }

    @NotNull
    @Override
    public String getHutName() {
        return "blockhutbuilder";
    }

    @Override
    public BuildingEntry getBuildingEntry() {
        return ModBuildings.builder.get();
    }
}
