package moddedmite.wiseinventory.inventory;

import fi.dy.masa.malilib.util.GuiUtils;
import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.feat.ContinuousOperation;
import moddedmite.wiseinventory.feat.WheelMoving;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import moddedmite.wiseinventory.inventory.section.SectionHandler;
import moddedmite.wiseinventory.util.ItemUtil;
import net.minecraft.*;

import java.util.*;

public class InventoryTweaks {
    public static final Set<Integer> BUTTON_UP_CANCEL_SET = new HashSet<>();

    public static boolean shouldDoTrick(GuiContainer guiContainer, Slot mouseOver) {
        return mouseOver != null && mouseOver.getHasStack() && !(guiContainer instanceof GuiContainerCreative);
    }

    private static ContainerSection expandSectionIfPossible(ContainerSection section) {
        if (GuiUtils.getCurrentScreen() instanceof GuiInventory) return section;
        if (section.isOf(EnumSection.InventoryHotBar) || section.isOf(EnumSection.InventoryStorage))
            return EnumSection.InventoryWhole.get();
        return section;
    }

    public static boolean shouldCancelLeftClick(GuiContainer guiContainer, Slot mouseOver) {
        if (mouseOver == null) return false;
        if (guiContainer instanceof GuiContainerCreative) return false;

        ContainerSection section = SectionHandler.getSection(mouseOver);
        if (tryMoveSimilar()) {
            return true;
        }
        if (WiseInventoryConfig.ModifierMoveAll.getKeybind().isKeybindHeld()) {
            InventoryUtil.putHeldItemDown(section);
            section.notEmptyRun(InventoryUtil::quickMove);
            return true;
        }
        if (WiseInventoryConfig.ModifierSpreadItem.getKeybind().isKeybindHeld() && trySpreading(false)) {
            BUTTON_UP_CANCEL_SET.add(0);
            return true;
        }
        return false;
    }

    public static boolean shouldCancelRightClick(GuiContainer guiContainer, Slot mouseOver) {
        if (mouseOver == null) return false;
        if (guiContainer instanceof GuiContainerCreative) return false;

        if (WiseInventoryConfig.ModifierSpreadItem.getKeybind().isKeybindHeld() && trySpreading(true)) {
            BUTTON_UP_CANCEL_SET.add(1);
            return true;
        }
        return false;
    }

    public static void onRender(GuiContainer guiContainer, int mouseX, int mouseY, Slot mouseOver) {
        if (!shouldDoTrick(guiContainer, mouseOver)) return;

        if (WiseInventoryConfig.ContinuousOperation.getBooleanValue()) {
            ContinuousOperation.quickMoving(guiContainer, mouseX, mouseY, mouseOver);
        }

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

    public static boolean tryMoveSimilar() {
        if (WiseInventoryConfig.ModifierMoveSimilar.getKeybind().isKeybindHeld()) {
            InventoryUtil.getSlotMouseOver().ifPresent(slot -> {
                if (slot.getHasStack()) {
                    ItemStack template = slot.getStack().copy();
                    ContainerSection section = SectionHandler.getSection(slot);
                    section = expandSectionIfPossible(section);
                    section.predicateRun(ItemUtil.predicateIDMeta(template), InventoryUtil::quickMove);
                }
            });
            return true;
        }
        return false;
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
}
