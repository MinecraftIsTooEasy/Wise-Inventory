package moddedmite.wiseinventory.feat;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import net.minecraft.*;

public class BetterContainerClosing {
    public static void onCloseScreen(GuiContainer guiContainer) {
        if (!WiseInventoryConfig.BetterContainerClosing.getBooleanValue()) return;

        ContainerSection destination = EnumSection.InventoryWhole.get();

        InventoryTweaks.clearCursor(destination);

        ContainerSection section = ContainerSection.EMPTY;
        if (guiContainer instanceof GuiEnchantment) {
            section = EnumSection.Unidentified.get();
        }
        if (guiContainer instanceof GuiMerchant) {
            section = EnumSection.MerchantIn.get();
        }
        if (guiContainer instanceof GuiRepair) {
            section = EnumSection.AnvilIn1.get().mergeWith(EnumSection.AnvilIn2.get());
        }
        if (guiContainer instanceof GuiCrafting) {
            section = EnumSection.CraftMatrix.get();
        }

        section.notEmptyRun(slot -> InventoryTweaks.quickMove(slot, destination));
    }
}
