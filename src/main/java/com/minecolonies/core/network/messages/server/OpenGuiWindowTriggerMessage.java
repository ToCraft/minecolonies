package com.minecolonies.core.network.messages.server;

import com.ldtteam.common.network.AbstractServerPlayMessage;
import com.ldtteam.common.network.PlayMessageType;
import com.minecolonies.api.advancements.AdvancementTriggers;
import com.minecolonies.api.util.constant.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenGuiWindowTriggerMessage extends AbstractServerPlayMessage {
    public static final PlayMessageType<?> TYPE = PlayMessageType.forServer(Constants.MOD_ID, "open_gui_window_trigger", OpenGuiWindowTriggerMessage::new);

    /**
     * The window's Resource
     */
    private final String resource;

    public OpenGuiWindowTriggerMessage(final String resource) {
        super(TYPE);
        this.resource = resource;
    }

    @Override
    protected void toBytes(final RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.resource);
    }

    protected OpenGuiWindowTriggerMessage(final RegistryFriendlyByteBuf buf, final PlayMessageType<?> type) {
        super(buf, type);
        this.resource = buf.readUtf(32767);
    }

    @Override
    protected void onExecute(final IPayloadContext ctxIn, final ServerPlayer player) {
        AdvancementTriggers.OPEN_GUI_WINDOW.get().trigger(player, this.resource);
    }
}
