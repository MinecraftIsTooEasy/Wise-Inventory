package moddedmite.wiseinventory.config;

import moddedmite.wiseinventory.feat.SortInventory;
import moddedmite.wiseinventory.feat.TradingRestock;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.util.Predicates;
import net.minecraft.GuiMerchant;
import net.minecraft.Minecraft;

public class Callbacks {

    public static void init(Minecraft client) {

        WiseInventoryConfig.OpenConfigScreen.getKeybind().setCallback((keyAction, iKeybind) -> {
            Minecraft.getMinecraft().displayGuiScreen(WiseInventoryConfig.getInstance().getConfigScreen(null));
            return true;
        });

        WiseInventoryConfig.SortInventory.getKeybind().setCallback((keyAction, iKeybind) -> SortInventory.onKey(client));

        WiseInventoryConfig.TradingRestock.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiMerchant guiMerchant) {
                TradingRestock.tryTradingRestock(guiMerchant);
                client.sndManager.playSoundFX("random.click", 1.0f, 1.0f);
                return true;
            }
            return false;
        });

        WiseInventoryConfig.ThrowSection.getKeybind().setCallback((keyAction, iKeybind) -> {
            if (Predicates.notInGuiContainer(client)) return false;
            return InventoryTweaks.tryThrowSection();
        });
    }
}
