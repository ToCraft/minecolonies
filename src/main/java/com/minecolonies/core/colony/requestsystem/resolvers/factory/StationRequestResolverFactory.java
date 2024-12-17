package com.minecolonies.core.colony.requestsystem.resolvers.factory;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolverFactory;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.constant.SerializationIdentifierConstants;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.colony.requestsystem.resolvers.StationRequestResolver;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * ------------ Class not Documented ------------
 */
public class StationRequestResolverFactory implements IRequestResolverFactory<StationRequestResolver> {
    /// /// --------------------------- NBTConstants --------------------------- \\\\\\
    private static final String NBT_TOKEN = "Token";
    private static final String NBT_LOCATION = "Location";

    /// /// --------------------------- NBTConstants --------------------------- \\\\\\

    @NotNull
    @Override
    public TypeToken<? extends StationRequestResolver> getFactoryOutputType() {
        return TypeToken.of(StationRequestResolver.class);
    }

    @NotNull
    @Override
    public TypeToken<? extends ILocation> getFactoryInputType() {
        return TypeConstants.ILOCATION;
    }

    @NotNull
    @Override
    public StationRequestResolver getNewInstance(@NotNull final IFactoryController factoryController, @NotNull final ILocation iLocation, @NotNull final Object... context)
            throws IllegalArgumentException {
        return new StationRequestResolver(iLocation, factoryController.getNewInstance(TypeConstants.ITOKEN));
    }

    @NotNull
    @Override
    public CompoundTag serialize(@NotNull final HolderLookup.Provider provider,
                                 @NotNull final IFactoryController controller, @NotNull final StationRequestResolver StationRequestResolver) {
        final CompoundTag compound = new CompoundTag();
        compound.put(NBT_TOKEN, controller.serializeTag(provider, StationRequestResolver.getId()));
        compound.put(NBT_LOCATION, controller.serializeTag(provider, StationRequestResolver.getLocation()));
        return compound;
    }

    @NotNull
    @Override
    public StationRequestResolver deserialize(@NotNull final HolderLookup.Provider provider, @NotNull final IFactoryController controller, @NotNull final CompoundTag nbt) {
        final IToken<?> token = controller.deserializeTag(provider, nbt.getCompound(NBT_TOKEN));
        final ILocation location = controller.deserializeTag(provider, nbt.getCompound(NBT_LOCATION));

        return new StationRequestResolver(location, token);
    }

    @Override
    public void serialize(IFactoryController controller, StationRequestResolver input, RegistryFriendlyByteBuf packetBuffer) {
        controller.serialize(packetBuffer, input.getId());
        controller.serialize(packetBuffer, input.getLocation());
    }

    @NotNull
    @Override
    public StationRequestResolver deserialize(IFactoryController controller, @NotNull RegistryFriendlyByteBuf buffer) throws Throwable {
        final IToken<?> token = controller.deserialize(buffer);
        final ILocation location = controller.deserialize(buffer);

        return new StationRequestResolver(location, token);
    }

    @Override
    public short getSerializationId() {
        return SerializationIdentifierConstants.STATION_REQUEST_RESOLVER_ID;
    }
}
