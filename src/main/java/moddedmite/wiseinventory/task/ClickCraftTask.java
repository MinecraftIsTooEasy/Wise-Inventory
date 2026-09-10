package moddedmite.wiseinventory.task;

import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.section.EnumSection;
import net.minecraft.GuiCrafting;
import net.minecraft.Minecraft;
import net.minecraft.Slot;

public class ClickCraftTask extends AbstractTimedTask {
    public ClickCraftTask(int ticks) {
        super(ticks);
    }

    @Override
    public boolean shouldExecute(Minecraft client) {
        return super.shouldExecute(client)
                && client.currentScreen instanceof GuiCrafting
                && !Minecraft.getMinecraft().thePlayer.crafting_proceed;
    }

    @Override
    public Boolean execute(Minecraft client) {
        Slot slot = EnumSection.CraftResult.get().slots().get(0);
        InventoryUtil.leftClick(slot);
        return true;
    }
}
