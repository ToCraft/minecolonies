package com.minecolonies.core.colony.requestsystem.resolvers;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.location.ILocation;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.crafting.PrivateCrafting;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.requestsystem.resolvers.core.AbstractCraftingProductionResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrivateWorkerCraftingProductionResolver extends AbstractCraftingProductionResolver<PrivateCrafting> {
    /**
     * Constructor to initialize.
     *
     * @param location the location.
     * @param token    the id.
     */
    public PrivateWorkerCraftingProductionResolver(@NotNull final ILocation location, @NotNull final IToken<?> token, @NotNull final JobEntry entry) {
        super(location, token, entry, PrivateCrafting.class);
    }

    @Nullable
    @Override
    public List<IRequest<?>> getFollowupRequestForCompletion(@NotNull final IRequestManager manager, @NotNull final IRequest<? extends PrivateCrafting> completedRequest) {
        return null;
    }

    @Override
    public void onAssignedRequestBeingCancelled(@NotNull final IRequestManager manager, @NotNull final IRequest<? extends PrivateCrafting> request) {

    }

    @Override
    public void onAssignedRequestCancelled(@NotNull final IRequestManager manager, @NotNull final IRequest<? extends PrivateCrafting> request) {

    }

    @Override
    public void onRequestedRequestComplete(@NotNull final IRequestManager manager, @NotNull final IRequest<?> request) {

    }

    @Override
    public void onRequestedRequestCancelled(@NotNull final IRequestManager manager, @NotNull final IRequest<?> request) {

    }

    @Override
    public void resolveForBuilding(@NotNull final IRequestManager manager, @NotNull final IRequest<? extends PrivateCrafting> request, @NotNull final AbstractBuilding building) {
        manager.updateRequestState(request.getId(), RequestState.FINALIZING);

        final IRecipeStorage storage = IColonyManager.getInstance().getRecipeManager().getRecipes().get(request.getRequest().getRecipeID());

        if (storage == null) {
            manager.updateRequestState(request.getId(), RequestState.FAILED);
            return;
        }

        final ICraftingBuildingModule module = building.getCraftingModuleForRecipe(request.getRequest().getRecipeID());
        if (module == null) {
            manager.updateRequestState(request.getId(), RequestState.FAILED);
            return;
        }
        for (int i = 0; i < request.getRequest().getCount(); i++) {
            module.fullFillRecipe(storage);
        }

        manager.updateRequestState(request.getId(), RequestState.RESOLVED);
    }
}
