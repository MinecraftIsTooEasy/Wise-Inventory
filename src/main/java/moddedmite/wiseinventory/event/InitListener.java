package moddedmite.wiseinventory.event;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.event.TickHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import moddedmite.wiseinventory.config.Callbacks;
import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.event.tick.TaskManager;
import net.minecraft.Minecraft;

public class InitListener implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfig(WiseInventoryConfig.getInstance());
        TickHandler.getInstance().registerClientTickHandler(TaskManager.getInstance());
        Callbacks.init(Minecraft.getMinecraft());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputListener.getInstance());
    }
}
