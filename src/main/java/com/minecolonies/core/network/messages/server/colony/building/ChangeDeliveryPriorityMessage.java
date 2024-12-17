package com.minecolonies.core.network.messages.server.colony.building;

import com.ldtteam.common.network.PlayMessageType;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.buildings.workerbuildings.Stash;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class ChangeDeliveryPriorityMessage extends AbstractBuildingServerMessage<IBuilding> {
    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(Constants.MOD_ID, "change_delivery_priority", ChangeDeliveryPriorityMessage::new);

    /**
     * If up true, if down false.
     */
    private boolean up;

    /**
     * Creates message for player to change the priority of the delivery.
     *
     * @param building view of the building to read data from
     * @param up       up or down?
     */
    public ChangeDeliveryPriorityMessage(@NotNull final IBuildingView building, final boolean up) {
        super(TYPE, building);
        this.up = up;
    }

    /**
     * Transformation from a byteStream to the variables.
     *
     * @param buf the used byteBuffer.
     */
    protected ChangeDeliveryPriorityMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type) {
        super(buf, type);
        this.up = buf.readBoolean();
    }

    /**
     * Transformation to a byteStream.
     *
     * @param buf the used byteBuffer.
     */
    @Override
    protected void toBytes(@NotNull final RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(this.up);
    }

    @Override
    protected void onExecute(final IPayloadContext ctxIn, final ServerPlayer player, final IColony colony, final IBuilding building) {
        if ((building != null && building.hasModule(WorkerBuildingModule.class)) || building instanceof Stash) {
            if (up) {
                building.alterPickUpPriority(1);
            } else {
                building.alterPickUpPriority(-1);
            }
            building.markDirty();
        }
    }
}
