package com.minecolonies.api.events;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.event.ColonyDeletedEvent;
import com.minecolonies.api.util.Log;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Event manager for all forge events.
 */
public class ColonyEvents {
    /**
     * Event triggered when a colony is being deleted.
     *
     * @param colony the colony in question.
     */
    public static void deleteColony(final IColony colony) {
        sendEventSafe(new ColonyDeletedEvent(colony));
    }

    /**
     * Underlying logic for transmitting an event.
     */
    private static void sendEventSafe(final Event event) {
        try {
            NeoForge.EVENT_BUS.post(event);
        } catch (final Exception e) {
            Log.getLogger().atError().withThrowable(e).log("Exception occurred during {} event", event.getClass().getName());
        }
    }
}
