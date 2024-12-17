package com.minecolonies.core.colony.jobs;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.entity.ai.workers.production.EntityAIWorkLumberjack;
import com.minecolonies.core.entity.ai.workers.util.Tree;
import com.minecolonies.core.util.AttributeModifierUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.minecolonies.api.util.constant.CitizenConstants.SKILL_BONUS_ADD_NAME;
import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_TREE;

/**
 * The Lumberjack job class.
 */
public class JobLumberjack extends AbstractJobCrafter<EntityAIWorkLumberjack, JobLumberjack> {
    /**
     * Walking speed bonus per level
     */
    public static final double BONUS_SPEED_PER_LEVEL = 0.003;

    /**
     * The tree this lumberjack is currently working on.
     */
    @Nullable
    private Tree tree;

    /**
     * Create a lumberjack job.
     *
     * @param entity the lumberjack.
     */
    public JobLumberjack(final ICitizenData entity) {
        super(entity);
    }

    @Override
    public CompoundTag serializeNBT(@NotNull final HolderLookup.Provider provider) {
        final CompoundTag compound = super.serializeNBT(provider);
        @NotNull final CompoundTag treeTag = new CompoundTag();

        if (tree != null) {
            tree.write(provider, treeTag);
        }

        compound.put(TAG_TREE, treeTag);
        return compound;
    }

    @NotNull
    @Override
    public ResourceLocation getModel() {
        return ModModelTypes.LUMBERJACK_ID;
    }

    @Override
    public void deserializeNBT(@NotNull final HolderLookup.Provider provider, final CompoundTag compound) {
        super.deserializeNBT(provider, compound);
        if (compound.contains(TAG_TREE)) {
            tree = Tree.read(provider, compound.getCompound(TAG_TREE));
            if (!tree.isTree()) {
                tree = null;
            }
        }
    }

    @Override
    public void onLevelUp() {
        if (getCitizen().getEntity().isPresent()) {
            final AbstractEntityCitizen worker = getCitizen().getEntity().get();
            final AttributeModifier speedModifier = new AttributeModifier(SKILL_BONUS_ADD_NAME, (getCitizen().getCitizenSkillHandler().getLevel(getCitizen().getWorkBuilding().getModule(
                    BuildingModules.FORESTER_WORK).getSecondarySkill()) / 2.0) * BONUS_SPEED_PER_LEVEL, AttributeModifier.Operation.ADD_VALUE);
            AttributeModifierUtils.addModifier(worker, speedModifier, Attributes.MOVEMENT_SPEED);
        }
    }

    /**
     * Get the current tree the lumberjack is cutting.
     *
     * @return the tree.
     */
    @Nullable
    public Tree getTree() {
        return tree;
    }

    /**
     * Set the tree he is currently cutting.
     *
     * @param tree the tree.
     */
    public void setTree(@Nullable final Tree tree) {
        this.tree = tree;
    }

    @NotNull
    @Override
    public EntityAIWorkLumberjack generateAI() {
        return new EntityAIWorkLumberjack(this);
    }
}
