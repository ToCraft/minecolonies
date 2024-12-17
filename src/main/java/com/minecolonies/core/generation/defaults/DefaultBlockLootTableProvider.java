package com.minecolonies.core.generation.defaults;

import com.minecolonies.api.blocks.AbstractBlockHut;
import com.minecolonies.api.blocks.ModBlocks;
import com.minecolonies.api.loot.ModLootConditions;
import com.minecolonies.core.blocks.BlockMinecoloniesRack;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetBannerPatternFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DefaultBlockLootTableProvider extends BlockLootSubProvider {
    public DefaultBlockLootTableProvider(@NotNull final HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        saveBlocks(Arrays.asList(ModBlocks.getHuts()));

        saveBlock(ModBlocks.blockHutWareHouse);
        saveBlock(ModBlocks.blockStash);

        saveBlock(ModBlocks.blockRack);
        saveBlock(ModBlocks.blockWayPoint);
        saveBlock(ModBlocks.blockBarrel);
        saveBlock(ModBlocks.blockScarecrow);
        saveBlock(ModBlocks.blockPlantationField);
        saveBlock(ModBlocks.blockColonyBanner);
        saveBlock(ModBlocks.blockColonyWallBanner);
        saveBlock(ModBlocks.blockIronGate);
        saveBlock(ModBlocks.blockWoodenGate);
        saveBlock(ModBlocks.blockCompostedDirt,
                lootPool -> lootPool.add(AlternativesEntry.alternatives()
                        .otherwise(LootItem.lootTableItem(ModBlocks.blockCompostedDirt)
                                .when(ModLootConditions.hasSilkTouch(enchantments)))
                        .otherwise(LootItem.lootTableItem(Blocks.DIRT)
                                .when(ExplosionCondition.survivesExplosion()))));

        saveBlock(ModBlocks.farmland, lootPool -> lootPool.add(AlternativesEntry.alternatives().otherwise(LootItem.lootTableItem(Blocks.DIRT))));
        saveBlock(ModBlocks.floodedFarmland, lootPool -> lootPool.add(AlternativesEntry.alternatives().otherwise(LootItem.lootTableItem(Blocks.DIRT))));

        for (Block block : ModBlocks.getCrops()) {
            final LootItemBlockStatePropertyCondition.Builder cropCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 6));
            saveBlock(block, lootPool -> lootPool.add(LootItem.lootTableItem(block.asItem()).when(cropCondition).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)).otherwise(LootItem.lootTableItem(block.asItem()))));
        }

        // intentionally no drops -- creative only
        //saveBlock(ModBlocks.blockDecorationPlaceholder);
    }

    private <T extends Block> void saveBlocks(@NotNull final List<T> blocks) {
        for (final Block block : blocks) {
            saveBlock(block);
        }
    }

    private void saveBlock(@NotNull final Block block) {
        final LootPoolSingletonContainer.Builder<?> item = LootItem.lootTableItem(block);
        if (block instanceof AbstractBlockHut || block instanceof BlockMinecoloniesRack) {
            item.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        }

        this.saveBlock(block, lootPool -> lootPool.add(item).when(ExplosionCondition.survivesExplosion()));
    }

    private void saveBlock(@NotNull final Block block, final Consumer<Builder> lootPoolConfigurer) {
        final Builder lootPoolbuilder = LootPool.lootPool();
        lootPoolConfigurer.accept(lootPoolbuilder);
        add(block, LootTable.lootTable().withPool(lootPoolbuilder));
    }

    private void saveBannerBlock(@NotNull final Block block) {
        add(block,
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(block))
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(SetBannerPatternFunction.setBannerPattern(false))
                        .when(ExplosionCondition.survivesExplosion())
                ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Stream.concat(Arrays.stream(ModBlocks.getCrops()), Stream.concat(Arrays.stream(ModBlocks.getHuts()), Stream.of(
                ModBlocks.blockHutWareHouse,
                ModBlocks.blockStash,
                //ModBlocks.blockConstructionTape, // no loot table
                ModBlocks.blockRack,
                ModBlocks.blockWayPoint,
                ModBlocks.blockBarrel,
                ModBlocks.blockScarecrow,
                ModBlocks.blockPlantationField,
                ModBlocks.blockColonyBanner,
                ModBlocks.blockColonyWallBanner,
                ModBlocks.blockIronGate,
                ModBlocks.blockWoodenGate,
                ModBlocks.blockCompostedDirt,
                //ModBlocks.blockDecorationPlaceholder, // creative only
                ModBlocks.floodedFarmland,
                ModBlocks.farmland
        )).map(Block.class::cast)).toList();
    }
}
