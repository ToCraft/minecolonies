package com.minecolonies.api.entity.ai.workers.util;

import com.minecolonies.api.equipment.ModEquipmentTypes;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public final class GuardGearBuilder {
    /**
     * Private constructor to hide implicit one.
     */
    private GuardGearBuilder() {
        /*
         * Intentionally left empty.
         */
    }

    /**
     * Build the gear for a certain armor level and level range.
     *
     * @param minArmorLevel      the min armor level.
     * @param maxArmorLevel      the max armor level.
     * @param levelRange         the level range of the guard.
     * @param buildingLevelRange the building level range.
     * @return the list of items.
     */
    public static List<GuardGear> buildGearForLevel(
            final int minArmorLevel,
            final int maxArmorLevel,
            final Tuple<Integer, Integer> levelRange,
            final Tuple<Integer, Integer> buildingLevelRange) {
        final List<GuardGear> armorList = new ArrayList<>();
        armorList.add(new GuardGear(ModEquipmentTypes.boots.get(), EquipmentSlot.FEET, minArmorLevel, maxArmorLevel, levelRange, buildingLevelRange));
        armorList.add(new GuardGear(ModEquipmentTypes.chestplate.get(), EquipmentSlot.CHEST, minArmorLevel, maxArmorLevel, levelRange, buildingLevelRange));
        armorList.add(new GuardGear(ModEquipmentTypes.helmet.get(), EquipmentSlot.HEAD, minArmorLevel, maxArmorLevel, levelRange, buildingLevelRange));
        armorList.add(new GuardGear(ModEquipmentTypes.leggings.get(), EquipmentSlot.LEGS, minArmorLevel, maxArmorLevel, levelRange, buildingLevelRange));
        return armorList;
    }
}
