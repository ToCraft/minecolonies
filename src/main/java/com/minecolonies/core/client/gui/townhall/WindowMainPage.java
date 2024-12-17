package com.minecolonies.core.client.gui.townhall;

import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ButtonImage;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.DropDownList;
import com.ldtteam.structurize.client.gui.WindowSwitchPack;
import com.ldtteam.structurize.storage.StructurePacks;
import com.minecolonies.core.client.gui.WindowBannerPicker;
import com.minecolonies.core.client.gui.map.WindowColonyMap;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingTownHall;
import com.minecolonies.core.network.messages.server.colony.ColonyNameStyleMessage;
import com.minecolonies.core.network.messages.server.colony.ColonyStructureStyleMessage;
import com.minecolonies.core.network.messages.server.colony.ColonyTextureStyleMessage;
import com.minecolonies.core.network.messages.server.colony.TeamColonyColorChangeMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.minecolonies.api.util.constant.Constants.TICKS_FOURTY_MIN;
import static com.minecolonies.api.util.constant.WindowConstants.*;
import static com.minecolonies.core.event.TextureReloadListener.TEXTURE_PACKS;

/**
 * BOWindow for the town hall.
 */
public class WindowMainPage extends AbstractWindowTownHall {
    /**
     * Drop down list for style.
     */
    private DropDownList colorDropDownList;

    /**
     * Drop down list for style.
     */
    private DropDownList textureDropDownList;

    /**
     * Drop down list for name style.
     */
    private DropDownList nameStyleDropDownList;

    /**
     * The initial texture index.
     */
    private final int initialTextureIndex;

    /**
     * The initial texture index.
     */
    private final int initialNamePackIndex;


    /**
     * Label for the colony name.
     */
    private final Text title;


    /**
     * Constructor for the town hall window.
     *
     * @param building {@link BuildingTownHall.View}.
     */
    public WindowMainPage(final BuildingTownHall.View building) {
        super(building, "layoutactions.xml");
        initDropDowns();

        title = findPaneOfTypeByID(LABEL_BUILDING_NAME, Text.class);

        registerButton(BUTTON_CHANGE_SPEC, this::doNothing);
        registerButton(BUTTON_RENAME, this::renameClicked);
        registerButton(BUTTON_MERCENARY, this::mercenaryClicked);
        registerButton(BUTTON_TOWNHALLMAP, this::mapButtonClicked);

        registerButton(BUTTON_COLONY_SWITCH_STYLE, this::switchPack);

        findPaneOfTypeByID(BUTTON_COLONY_SWITCH_STYLE, ButtonImage.class).setText(Component.literal(building.getColony().getStructurePack()));
        registerButton(BUTTON_BANNER_PICKER, this::openBannerPicker);

        this.colorDropDownList.setSelectedIndex(building.getColony().getTeamColonyColor().ordinal());
        this.textureDropDownList.setSelectedIndex(TEXTURE_PACKS.indexOf(building.getColony().getTextureStyleId()));
        this.initialTextureIndex = textureDropDownList.getSelectedIndex();

        this.nameStyleDropDownList.setSelectedIndex(building.getColony().getNameFileIds().indexOf(building.getColony().getNameStyle()));
        this.initialNamePackIndex = nameStyleDropDownList.getSelectedIndex();
    }


    /**
     * Switch the structure style pack.
     */
    private void switchPack() {
        new WindowSwitchPack(() -> {
            building.getColony().setStructurePack(StructurePacks.selectedPack.getName());
            new ColonyStructureStyleMessage(building.getColony(), StructurePacks.selectedPack.getName()).sendToServer();
            return new WindowMainPage((BuildingTownHall.View) this.building);
        }).open();
    }

    /**
     * Initialise the previous/next and drop down list for style.
     */
    private void initDropDowns() {
        findPaneOfTypeByID(DROPDOWN_COLOR_ID, DropDownList.class).setEnabled(enabled);

        colorDropDownList = findPaneOfTypeByID(DROPDOWN_COLOR_ID, DropDownList.class);
        colorDropDownList.setHandler(this::onDropDownListChanged);

        final List<ChatFormatting> textColors = Arrays.stream(ChatFormatting.values()).filter(ChatFormatting::isColor).toList();

        colorDropDownList.setDataProvider(new DropDownList.DataProvider() {
            @Override
            public int getElementCount() {
                return textColors.size();
            }

            @Override
            public MutableComponent getLabel(final int index) {
                if (index >= 0 && index < textColors.size()) {
                    final String colorName = textColors.get(index).getName().replace("_", " ");
                    return Component.literal(colorName.substring(0, 1).toUpperCase(Locale.US) + colorName.substring(1));
                }
                return Component.empty();
            }
        });

        textureDropDownList = findPaneOfTypeByID(DROPDOWN_TEXT_ID, DropDownList.class);
        textureDropDownList.setHandler(this::toggleTexture);
        textureDropDownList.setDataProvider(new DropDownList.DataProvider() {
            @Override
            public int getElementCount() {
                return TEXTURE_PACKS.size();
            }

            @Override
            public MutableComponent getLabel(final int index) {
                return Component.literal(TEXTURE_PACKS.get(index));
            }
        });
        textureDropDownList.enable();
        textureDropDownList.show();

        nameStyleDropDownList = findPaneOfTypeByID(DROPDOWN_NAME_ID, DropDownList.class);
        nameStyleDropDownList.setHandler(this::toggleNameFile);
        nameStyleDropDownList.setDataProvider(new DropDownList.DataProvider() {
            @Override
            public int getElementCount() {
                return building.getColony().getNameFileIds().size();
            }

            @Override
            public MutableComponent getLabel(final int index) {
                return Component.literal(building.getColony().getNameFileIds().get(index));
            }
        });
        nameStyleDropDownList.enable();
    }

    /**
     * Toggle the dropdownlist with the selected index to change the texture of the colonists.
     *
     * @param dropDownList the toggle dropdown list.
     */
    private void toggleTexture(final DropDownList dropDownList) {
        if (dropDownList.getSelectedIndex() != initialTextureIndex) {
            new ColonyTextureStyleMessage(building.getColony(), TEXTURE_PACKS.get(dropDownList.getSelectedIndex())).sendToServer();
        }
    }

    /**
     * Toggle the dropdownlist with the selected index to change the texture of the colonists.
     *
     * @param dropDownList the toggle dropdown list.
     */
    private void toggleNameFile(final DropDownList dropDownList) {
        if (dropDownList.getSelectedIndex() != initialNamePackIndex) {
            new ColonyNameStyleMessage(building.getColony(), building.getColony().getNameFileIds().get(dropDownList.getSelectedIndex())).sendToServer();
        }
    }

    /**
     * Called when the dropdownList changed.
     *
     * @param dropDownList the list.
     */
    private void onDropDownListChanged(final DropDownList dropDownList) {
        new TeamColonyColorChangeMessage(dropDownList.getSelectedIndex(), building).sendToServer();
    }

    /**
     * Opens the banner picker window. BOWindow does not use blockui, so is started manually.
     *
     * @param button the trigger button
     */
    private void openBannerPicker(@NotNull final Button button) {
        Screen window = new WindowBannerPicker(building.getColony(), this);
        Minecraft.getInstance().setScreen(window);
    }

    @Override
    public void onOpened() {
        super.onOpened();

        title.setText(Component.literal(building.getColony().getName()));

        if (building.getColony().getMercenaryUseTime() != 0
                && building.getColony().getWorld().getGameTime() - building.getColony().getMercenaryUseTime() < TICKS_FOURTY_MIN) {
            findPaneOfTypeByID(BUTTON_MERCENARY, Button.class).disable();
        }
    }

    /**
     * Action performed when rename button is clicked.
     */
    private void renameClicked() {
        new WindowTownHallNameEntry(building.getColony()).open();
    }

    /**
     * Action performed when mercenary button is clicked.
     */
    private void mercenaryClicked() {
        new WindowTownHallMercenary(building.getColony()).open();
    }

    /**
     * Opens the map on button clicked
     */
    private void mapButtonClicked() {
        new WindowColonyMap(building).open();
    }

    @Override
    protected String getWindowId() {
        return BUTTON_ACTIONS;
    }
}
