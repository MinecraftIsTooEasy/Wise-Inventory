package moddedmite.wiseinventory.feat;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.util.InputUtil;
import net.minecraft.Slot;

import javax.annotation.Nullable;
import java.util.Optional;

public class ContinuousOperation {
    private static Mode MODE = Mode.NONE;
    @Nullable
    private static Slot LAST_MOVED_SLOT = null;

    /**
     * @return If I should cancel left-clicking
     */
    public static boolean start() {
        if (InventoryUtil.getSlotMouseOver().isEmpty()) return false;
        if (InventoryUtil.isHoldingItem()) return false;

        MODE = detectMode();

        if (MODE.isNone()) return false;

        run(MODE);
        return true;
    }

    private static Mode detectMode() {
        if (InputUtil.isShiftDown()) {
            return Mode.SINGLE;
        }
        if (WiseInventoryConfig.ModifierMoveSimilar.getKeybind().isKeybindHeld()) {
            return Mode.SIMILAR;
        }
        return Mode.NONE;
    }

    public static void stop() {
        if (!MODE.isNone()) {
            LAST_MOVED_SLOT = null;
            MODE = Mode.NONE;
        }
    }

    public static void mouseMove() {
        if (MODE.isNone()) return;
        if (MODE != detectMode()) {
            stop();
            return;
        }
        run(MODE);
    }

    private static void run(Mode mode) {
        Optional<Slot> optional = InventoryUtil.getSlotMouseOver();
        if (optional.isEmpty()) return;
        Slot slot = optional.get();
        if (LAST_MOVED_SLOT == slot) return;

        handle(slot, mode);

        LAST_MOVED_SLOT = slot;
    }

    private static void handle(Slot slot, Mode mode) {
        if (mode == Mode.SINGLE) {
            InventoryUtil.quickMoveIfPossible(slot);
        }
        if (mode == Mode.SIMILAR) {
            InventoryTweaks.tryMoveSimilar();
        }
    }

    private enum Mode {
        NONE,
        SINGLE,
        SIMILAR,
        ;

        private boolean isNone() {
            return this == NONE;
        }
    }
}
