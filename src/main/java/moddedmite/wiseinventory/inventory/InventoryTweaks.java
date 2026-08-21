package moddedmite.wiseinventory.inventory;

import fi.dy.masa.malilib.util.GuiUtils;
import moddedmite.wiseinventory.feat.WheelMoving;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import moddedmite.wiseinventory.inventory.section.SectionHandler;
import moddedmite.wiseinventory.util.ItemUtil;
import net.minecraft.*;

import java.util.Optional;

public class InventoryTweaks {

    public static boolean shouldDoTrick(GuiContainer guiContainer, Slot mouseOver) {
        return mouseOver != null && mouseOver.getHasStack() && !(guiContainer instanceof GuiContainerCreative);
    }

    private static ContainerSection expandSectionIfPossible(ContainerSection section) {
        if (GuiUtils.getCurrentScreen() instanceof GuiInventory) return section;
        if (section.isOf(EnumSection.InventoryHotBar) || section.isOf(EnumSection.InventoryStorage))
            return EnumSection.InventoryWhole.get();
        return section;
    }

    public static void onRender(GuiContainer guiContainer, int mouseX, int mouseY, Slot mouseOver) {
        if (!shouldDoTrick(guiContainer, mouseOver)) return;

//        if (WiseInventoryConfig.ContinuousOperation.getBooleanValue()) {
//            ContinuousOperation.legacyQuickMoving(guiContainer, mouseX, mouseY, mouseOver);
//        }

        WheelMoving.onRender(mouseOver);
    }

    public static boolean trySpreading(boolean rightClick) {
        if (!InventoryUtil.isHoldingItem()) return false;

        Optional<ContainerSection> sectionOptional = SectionHandler.getSectionMouseOver();
        if (sectionOptional.isEmpty()) return false;
        ContainerSection section = sectionOptional.get();

        InventoryUtil.startSpreading(rightClick);
        section.allRun(x -> InventoryUtil.addToSpreading(x, rightClick));
        InventoryUtil.finishSpreading(rightClick);

        return true;
    }

    public static void tryMoveSimilar() {
        InventoryUtil.getSlotMouseOver().ifPresent(slot -> {
            if (slot.getHasStack()) {
                ItemStack template = slot.getStack().copy();
                ContainerSection section = SectionHandler.getSection(slot);
                section = expandSectionIfPossible(section);
                section.predicateRun(ItemUtil.predicateIDMeta(template), InventoryUtil::quickMove);
            }
        });
    }

    public static boolean tryThrowSection() {
        Optional<ContainerSection> section = SectionHandler.getSectionMouseOver();
        if (section.isEmpty()) return false;
        section.get().notEmptyRun(InventoryUtil::dropStack);
        return true;
    }

    // try to put held item to this section, if fail then drop
    public static void clearCursor(ContainerSection section) {
        ItemStack heldItem = InventoryUtil.getHeldStack();
        if (heldItem == null) return;
        Optional<Slot> mergeSlot = section.absorbsOneScroll(heldItem);
        while (mergeSlot.isPresent()) {
            InventoryUtil.leftClick(mergeSlot.get());
            heldItem = InventoryUtil.getHeldStack();
            if (heldItem == null) {
                return;// merge success
            } else {
                mergeSlot = section.absorbsOneScroll(heldItem);// try merge to other slot
            }
        }
        if (InventoryUtil.isHoldingItem()) {// if still
            Optional<Slot> emptySlot = section.getEmptySlot();
            if (emptySlot.isPresent()) {
                InventoryUtil.leftClick(emptySlot.get());// put held to empty
            } else {
                InventoryUtil.dropHeldItem();// just drop
            }
        }
    }

    public static boolean quickMove(Slot slot, ContainerSection destination) {
        if (!slot.getHasStack()) return false;
        if (InventoryUtil.isHoldingItem()) return false;
        InventoryUtil.leftClick(slot);
        clearCursor(destination);
        return true;
    }
}
