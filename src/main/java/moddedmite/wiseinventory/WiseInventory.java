package moddedmite.wiseinventory;

import fi.dy.masa.malilib.event.InitializationHandler;
import moddedmite.wiseinventory.event.InitListener;
import net.fabricmc.api.ClientModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WiseInventory implements ClientModInitializer {
    public static final String MOD_ID = "wise_inventory";
    public static final String MOD_NAME = "Wise Inventory";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        InitializationHandler.getInstance().registerInitializationHandler(new InitListener());
    }
}
