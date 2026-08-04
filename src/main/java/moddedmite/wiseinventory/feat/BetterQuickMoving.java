package moddedmite.wiseinventory.feat;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import net.minecraft.*;

import java.util.List;

public class BetterQuickMoving {
    public static void onQuickMove(int index, int button) {
        if (button == 0 && WiseInventoryConfig.BetterQuickMoving.getBooleanValue()) {

            GuiContainer guiContainer = InventoryUtil.getGuiContainer();
            Slot slot = InventoryUtil.getSlots().get(index);

            if (!slot.getHasStack()) return;

            if (guiContainer instanceof GuiCrafting) {

                ContainerSection craftMatrix = EnumSection.CraftMatrix.get();
                if (craftMatrix.hasSlot(slot)) {
                    EnumSection.InventoryWhole.get().moveToEmpty(slot);
                } else {// player inventory
                    craftMatrix.moveToEmpty(slot);
                }

                return;
            }

            if (guiContainer instanceof GuiEnchantment) {

                if (slot.getStack().isEnchantable()) return;// for vanilla clicking
                ContainerSection inventoryStorage = EnumSection.InventoryStorage.get();
                if (inventoryStorage.hasSlot(slot)) {
                    EnumSection.InventoryHotBar.get().moveToEmpty(slot);
                } else {
                    inventoryStorage.moveToEmpty(slot);
                }

                return;
            }

            if (guiContainer instanceof GuiMerchant) {

                ContainerSection merchantInSection = EnumSection.MerchantIn.get();
                if (merchantInSection.hasSlot(slot) || EnumSection.MerchantOut.get().hasSlot(slot)) {
                    return;// for vanilla clicking
                }
                List<Slot> slots = merchantInSection.slots();
                Slot emptyMerchantSlot = slots.get(0);
                if (emptyMerchantSlot.getHasStack()) {
                    emptyMerchantSlot = slots.get(1);
                    if (emptyMerchantSlot.getHasStack()) {
                        return;
                    }
                }
                InventoryUtil.moveToEmpty(slot, emptyMerchantSlot);

            }

        }
    }
}
