package moddedmite.wiseinventory.feat;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.SlotActionType;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import net.minecraft.*;

import java.util.List;

public class BetterQuickMoving {

    public static boolean shouldCancelClick(int index, int button, int clickType) {
        if (button != 0) return false;
        if (clickType != SlotActionType.QUICK_MOVE.ordinal()) return false;
        if (!WiseInventoryConfig.BetterQuickMoving.getBooleanValue()) return false;
        return onQuickMove(index);
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean onQuickMove(int index) {
        GuiContainer guiContainer = InventoryUtil.getGuiContainer();
        List<Slot> slots = InventoryUtil.getSlots();

        if (index < 0 || index >= slots.size()) return false;// somehow -1 occurs

        Slot slot = slots.get(index);

        if (!slot.getHasStack()) return false;

        if (guiContainer instanceof GuiCrafting) {
            ContainerSection craftMatrix = EnumSection.CraftMatrix.get();
            ContainerSection inventory = EnumSection.InventoryWhole.get();
            if (inventory.hasSlot(slot) && InventoryTweaks.quickMove(slot, craftMatrix)) return true;
        }

        if (guiContainer instanceof GuiEnchantment) {
            if (slot.getStack().isEnchantable()) return false;// for vanilla clicking
            ContainerSection inventoryStorage = EnumSection.InventoryStorage.get();
            ContainerSection inventoryHotBar = EnumSection.InventoryHotBar.get();
            if (inventoryStorage.hasSlot(slot) && InventoryTweaks.quickMove(slot, inventoryHotBar)) return true;
            if (inventoryHotBar.hasSlot(slot) && InventoryTweaks.quickMove(slot, inventoryStorage)) return true;
        }

        if (guiContainer instanceof GuiMerchant) {
            ContainerSection merchantInSection = EnumSection.MerchantIn.get();
            ContainerSection inventory = EnumSection.InventoryWhole.get();
            if (inventory.hasSlot(slot) && InventoryTweaks.quickMove(slot, merchantInSection)) return true;
        }

        return false;
    }
}
