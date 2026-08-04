package moddedmite.wiseinventory.util;

import moddedmite.wiseinventory.inventory.section.IContainer;
import net.minecraft.Container;

public class AccessorUtil {
    public static String getTypeString(Container container) {
        return ((IContainer) container).dc$getTypeString();
    }
}
