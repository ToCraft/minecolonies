package com.minecolonies.core.colony.jobs;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.entity.ai.workers.guard.training.EntityAICombatTraining;
import net.minecraft.resources.ResourceLocation;

/**
 * The Knight's Training Job class
 */
public class JobCombatTraining extends AbstractJob<EntityAICombatTraining, JobCombatTraining> {
    /**
     * Initialize citizen data.
     *
     * @param entity the citizen data.
     */
    public JobCombatTraining(final ICitizenData entity) {
        super(entity);
    }


    @Override
    public ResourceLocation getModel() {
        return ModModelTypes.KNIGHT_GUARD_ID;
    }

    @Override
    public EntityAICombatTraining generateAI() {
        return new EntityAICombatTraining(this);
    }
}
