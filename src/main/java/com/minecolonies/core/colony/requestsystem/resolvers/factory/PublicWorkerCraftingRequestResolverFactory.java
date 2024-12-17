package com.minecolonies.core.colony.requestsystem.resolvers.factory;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.jobs.registry.IJobRegistry;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.SerializationIdentifierConstants;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.requestsystem.resolvers.PublicWorkerCraftingRequestResolver;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PublicWorkerCraftingRequestResolverFactory implements IRequestResolverFactory<PublicWorkerCraftingRequestResolver> {
    /// /// --------------------------- NBTConstants --------------------------- \\\\\\
    private static final String NBT_TOKEN = "Token";
    private static final String NBT_LOCATION = "Location";
    private static final String NBT_JOB = "Job";

    /// /// --------------------------- NBTConstants --------------------------- \\\\\\

    @NotNull
    @Override
    public TypeToken<? extends PublicWorkerCraftingRequestResolver> getFactoryOutputType() {
        return TypeToken.of(PublicWorkerCraftingRequestResolver.class);
    }

    @NotNull
    @Override
    public TypeToken<? extends ILocation> getFactoryInputType() {
        return TypeConstants.ILOCATION;
    }

    @NotNull
    @Override
    public PublicWorkerCraftingRequestResolver getNewInstance(
            @NotNull final IFactoryController factoryController,
            @NotNull final ILocation iLocation,
            @NotNull final Object... context) {
        return new PublicWorkerCraftingRequestResolver(iLocation, factoryController.getNewInstance(TypeConstants.ITOKEN), (JobEntry) context[0]);
    }

    @NotNull
    @Override
    public CompoundTag serialize(@NotNull final HolderLookup.Provider provider, @NotNull final IFactoryController controller, @NotNull final PublicWorkerCraftingRequestResolver publicWorkerCraftingRequestResolverFactory) {
        final CompoundTag compound = new CompoundTag();
        compound.put(NBT_TOKEN, controller.serializeTag(provider, publicWorkerCraftingRequestResolverFactory.getId()));
        compound.put(NBT_LOCATION, controller.serializeTag(provider, publicWorkerCraftingRequestResolverFactory.getLocation()));
        compound.putString(NBT_JOB, IJobRegistry.getInstance().getKey(publicWorkerCraftingRequestResolverFactory.getJobEntry()).toString());

        return compound;
    }

    @NotNull
    @Override
    public PublicWorkerCraftingRequestResolver deserialize(@NotNull final HolderLookup.Provider provider, @NotNull final IFactoryController controller, @NotNull final CompoundTag nbt) {
        final IToken<?> token = controller.deserializeTag(provider, nbt.getCompound(NBT_TOKEN));
        final ILocation location = controller.deserializeTag(provider, nbt.getCompound(NBT_LOCATION));
        final JobEntry entry = IJobRegistry.getInstance().get(ResourceLocation.parse(nbt.getString(NBT_JOB)));

        return new PublicWorkerCraftingRequestResolver(location, token, entry);
    }

    @Override
    public void serialize(IFactoryController controller, PublicWorkerCraftingRequestResolver input, RegistryFriendlyByteBuf packetBuffer) {
        controller.serialize(packetBuffer, input.getId());
        controller.serialize(packetBuffer, input.getLocation());
        packetBuffer.writeById(IMinecoloniesAPI.getInstance().getJobRegistry()::getIdOrThrow, input.getJobEntry());
    }

    @Override
    public PublicWorkerCraftingRequestResolver deserialize(IFactoryController controller, RegistryFriendlyByteBuf buffer) throws Throwable {
        final IToken<?> token = controller.deserialize(buffer);
        final ILocation location = controller.deserialize(buffer);
        final JobEntry entry = buffer.readById(IMinecoloniesAPI.getInstance().getJobRegistry()::byIdOrThrow);
        return new PublicWorkerCraftingRequestResolver(location, token, entry);
    }

    @Override
    public short getSerializationId() {
        return SerializationIdentifierConstants.PUBLIC_WORKER_CRAFTING_REQUEST_RESOLVER_ID;
    }
}
