package com.minecolonies.core.entity.mobs.drownedpirates;

import com.minecolonies.api.entity.mobs.drownedpirate.AbstractDrownedEntityPirate;
import com.minecolonies.api.entity.mobs.pirates.IMeleePirateEntity;
import com.minecolonies.core.entity.pathfinding.navigation.MovementHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * Class for the Pirate entity.
 */
public class EntityDrownedPirate extends AbstractDrownedEntityPirate implements IMeleePirateEntity {

    /**
     * Constructor of the entity.
     *
     * @param type    the entity type.
     * @param worldIn world to construct it in.
     */
    public EntityDrownedPirate(final EntityType<? extends EntityDrownedPirate> type, final Level worldIn) {
        super(type, worldIn);
        this.moveControl = new MovementHandler(this);
    }

    @Override
    public void initStatsFor(final double baseHealth, final double difficulty, final double baseDamage) {
        super.initStatsFor(baseHealth, difficulty, baseDamage);
        this.getAttribute(Attributes.ARMOR).setBaseValue(0.25);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(baseHealth * 1.5);
        this.setHealth(this.getMaxHealth());
    }
}
