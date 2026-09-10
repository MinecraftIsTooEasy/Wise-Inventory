package moddedmite.wiseinventory.feat;

import moddedmite.wiseinventory.WiseInventory;
import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.event.tick.TaskManager;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import moddedmite.wiseinventory.task.ClickCraftTask;
import moddedmite.wiseinventory.task.SupplyTask;
import net.minecraft.GuiCrafting;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.Slot;

import javax.annotation.Nullable;
import java.util.List;

public class AutoCrafting {
    private static final int CRAFT_START_RETRY = 5;

    @Nullable
    private static ItemStack[] RECIPE = null;

    public static boolean isActive() {
        return WiseInventoryConfig.AutoCrafting.getBooleanValue() && Minecraft.getMinecraft().currentScreen instanceof GuiCrafting;
    }

    public static void recordRecipe() {
        ContainerSection section = EnumSection.CraftMatrix.get();
        List<Slot> slots = section.slots();
        RECIPE = new ItemStack[slots.size()];
        for (int i = 0; i < slots.size(); i++) {
            ItemStack stack = slots.get(i).getStack();
            RECIPE[i] = stack == null ? null : stack.copy();
        }
    }

    public static void tryAutoCraft() {
        if (RECIPE == null) {
            WiseInventory.LOGGER.warn("auto craft before recipe is recorded");
            return;
        }
        int tick = 1;
        ContainerSection section = EnumSection.CraftMatrix.get();
        List<Slot> slots = section.slots();
        for (int i = 0; i < slots.size(); i++) {
            ItemStack demand = RECIPE[i];
            if (demand == null) continue;// no demand
            Slot slot = slots.get(i);
            if (slot.getHasStack()) continue;// satisfied
            TaskManager.getInstance().addTimedTask(new SupplyTask(tick, demand, slot.slotNumber));
            tick++;
        }
        for (int i = 0; i < CRAFT_START_RETRY; i++) {
            TaskManager.getInstance().addTimedTask(new ClickCraftTask(tick));
            tick++;
        }
    }
}
