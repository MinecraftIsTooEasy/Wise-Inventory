package moddedmite.wiseinventory.task;

import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.section.ContainerSection;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import moddedmite.wiseinventory.util.ItemUtil;
import net.minecraft.GuiCrafting;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.Slot;

public class SupplyTask extends AbstractTimedTask {
    private final ItemStack template;
    private final int globalIndex;

    public SupplyTask(int ticks, ItemStack template, int globalIndex) {
        super(ticks);
        this.template = template;
        this.globalIndex = globalIndex;
    }

    @Override
    public Boolean execute(Minecraft client) {
        Slot slot = InventoryUtil.getSlots().get(this.globalIndex);
        ContainerSection section = EnumSection.InventoryWhole.get();
        for (Slot slot1 : section.slots()) {
            ItemStack stack = slot1.getStack();
            if (stack != null && ItemUtil.compareIDMeta(stack, this.template)) {
                InventoryUtil.putHeldItemDown(section);
                InventoryUtil.moveOneItem(slot, slot1);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldExecute(Minecraft client) {
        return client.currentScreen instanceof GuiCrafting;
    }
}
