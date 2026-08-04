package moddedmite.wiseinventory.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.KeyCodes;

public class ConfigFactory {
    public static ConfigBoolean ofBoolean(String name, boolean defaultValue) {
        return ofBoolean(name, defaultValue, null);
    }

    public static ConfigBoolean ofBoolean(String name, boolean defaultValue, String comment) {
        return new ConfigBoolean(name, defaultValue, comment);
    }

    public static KeybindMulti createKeyForGui(int... keyCodes) {
        return KeybindMulti.fromStorageString(KeyCodes.getStorageString(keyCodes), KeybindSettings.GUI);
    }

    public static KeybindMulti createKeyForModifierGui(int... keyCodes) {
        return KeybindMulti.fromStorageString(KeyCodes.getStorageString(keyCodes), KeybindSettings.MODIFIER_GUI);
    }
}
