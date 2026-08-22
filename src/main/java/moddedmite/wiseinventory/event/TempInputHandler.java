package moddedmite.wiseinventory.event;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.feat.ContinuousOperation;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.SectionHandler;
import moddedmite.wiseinventory.util.Predicates;
import net.minecraft.GuiContainer;
import net.minecraft.GuiContainerCreative;
import net.minecraft.Minecraft;
import net.minecraft.Slot;

import java.util.HashSet;
import java.util.Set;

// replace it in manylib 3.x
public class TempInputHandler {
    private static final Set<Integer> BUTTON_UP_CANCEL_SET = new HashSet<>();

    @SuppressWarnings("RedundantIfStatement")
    public static boolean shouldCancelClick(GuiContainer guiContainer, int button, Slot slot) {
        if (slot == null) return false;
        if (guiContainer instanceof GuiContainerCreative) return false;

        if (button == 0 && TempInputHandler.shouldCancelLeftClick(guiContainer, slot)) {
            return true;
        }
        if (button == 1 && TempInputHandler.shouldCancelRightClick(guiContainer, slot)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean shouldCancelLeftClick(GuiContainer guiContainer, Slot mouseOver) {
        ContainerSection section = SectionHandler.getSection(mouseOver);
        if (WiseInventoryConfig.ModifierMoveAll.getKeybind().isKeybindHeld()) {
            InventoryUtil.putHeldItemDown(section);
            section.notEmptyRun(InventoryUtil::quickMove);
            return true;
        }
        if (WiseInventoryConfig.ModifierSpreadItem.getKeybind().isKeybindHeld() && InventoryTweaks.trySpreading(false)) {
            cancelButtonUp(0);
            return true;
        }
        if (WiseInventoryConfig.ContinuousOperation.getBooleanValue() && ContinuousOperation.start()) {
            return true;
        }
        return false;
    }

    private static boolean shouldCancelRightClick(GuiContainer guiContainer, Slot mouseOver) {
        if (WiseInventoryConfig.ModifierSpreadItem.getKeybind().isKeybindHeld() && InventoryTweaks.trySpreading(true)) {
            cancelButtonUp(1);
            return true;
        }
        return false;
    }

    private static void cancelButtonUp(int eventButton) {
        BUTTON_UP_CANCEL_SET.add(eventButton);
    }

    public static boolean shouldCancelRelease(GuiContainer guiContainer, int eventButton) {
        if (eventButton == 0) {

            if (Predicates.notInGuiContainer(Minecraft.getMinecraft()))
                return false;// the below assuming valid environment

            ContinuousOperation.stop();
        }
        if (TempInputHandler.BUTTON_UP_CANCEL_SET.contains(eventButton)) {
            TempInputHandler.BUTTON_UP_CANCEL_SET.remove(eventButton);
            return true;
        }
        return false;
    }

    public static void mouseMoved(GuiContainer guiContainer, int mouseX, int mouseY) {
        if (Predicates.notInGuiContainer(Minecraft.getMinecraft())) return;// the below assuming valid environment

        ContinuousOperation.mouseMove();
    }
}
