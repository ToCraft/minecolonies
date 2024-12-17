package com.minecolonies.core.network.messages.server.colony.building.fields;

import com.ldtteam.common.network.PlayMessageType;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.colony.fields.IField;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.colony.buildings.modules.FieldsModule;
import com.minecolonies.core.colony.fields.registry.FieldDataManager;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Message which handles the assignment of fields to farmers.
 */
public class AssignFieldMessage extends AbstractBuildingServerMessage<IBuilding> {
    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(Constants.MOD_ID, "assign_field", AssignFieldMessage::new);

    /**
     * The modules ID
     */
    private final int moduleID;

    /**
     * The field to (un)assign.
     */
    private final RegistryFriendlyByteBuf fieldData;

    /**
     * Whether to assign or un-assign this field.
     */
    private final boolean assign;

    /**
     * Creates the message to assign a field.
     *
     * @param assign   assign if true, free if false.
     * @param field    the field.
     * @param building the building we're executing on.
     */
    public AssignFieldMessage(final IBuildingView building, final IField field, final boolean assign, final int moduleID) {
        super(TYPE, building);
        this.assign = assign;
        this.fieldData = FieldDataManager.fieldToBuffer(field, building.getColony().getWorld().registryAccess());
        this.moduleID = moduleID;
    }

    @Override
    protected void toBytes(@NotNull final RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        fieldData.resetReaderIndex();
        buf.writeBoolean(assign);
        buf.writeInt(moduleID);
        buf.writeByteArray(fieldData.array());
    }

    protected AssignFieldMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type) {
        super(buf, type);
        assign = buf.readBoolean();
        moduleID = buf.readInt();
        fieldData = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray()), buf.registryAccess());
    }

    @Override
    protected void onExecute(final IPayloadContext ctxIn, final ServerPlayer player, final IColony colony, final IBuilding building) {
        final IField parsedField = FieldDataManager.bufferToField(fieldData);
        colony.getBuildingManager().getField(otherField -> otherField.equals(parsedField)).ifPresent(field -> {

            if (building.getModule(moduleID) instanceof final FieldsModule fieldsModule) {
                if (assign) {
                    fieldsModule.assignField(field);
                } else {
                    fieldsModule.freeField(field);
                }
            }
        });
    }
}

