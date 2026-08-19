package moddedmite.wiseinventory.config;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigEnum;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.KeyCodes;
import moddedmite.wiseinventory.WiseInventory;
import moddedmite.wiseinventory.feat.ToolSwitchMode;
import moddedmite.wiseinventory.feat.WheelMovingMode;
import moddedmite.wiseinventory.inventory.sort.SortCategory;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static fi.dy.masa.malilib.config.ConfigFactory.*;
import static moddedmite.wiseinventory.config.ConfigFactory.createKeyForGui;
import static moddedmite.wiseinventory.config.ConfigFactory.createKeyForModifierGui;

public class WiseInventoryConfig extends SimpleConfigs {
    public static final List<ConfigBase<?>> ALL_VALUES;
    public static final List<ConfigHotkey> ALL_HOTKEYS;


    // inventory tweaks
    public static final ConfigBoolean ContinuousOperation = new ConfigBoolean("连续操作", true, "长按Shift和左键时能连续移动物品");
    public static final ConfigEnum<WheelMovingMode> WheelMoving = ofEnum("ommc.wheel_moving_order", WheelMovingMode.DEFAULT);
    public static final ConfigBoolean BetterQuickMoving = ConfigFactory.ofBoolean("更好的快速移动", true, "例如将物品送入工作台");
    public static final ConfigBoolean BetterContainerClosing = ConfigFactory.ofBoolean("更好的关闭容器", true, "关闭屏幕时不再丢出");
    public static final ConfigBoolean CachedSorting = ConfigFactory.ofBoolean("ommc.cached_sorting", true, "相比直接操作, 可减少发包");
    public static final ConfigEnum<SortCategory> ItemSortingOrder = ofEnum("ommc.item_sorting_order", SortCategory.CREATIVE_INVENTORY);
    public static final ConfigBoolean AutoCrafting = ofBoolean("ommc.auto_crafting");
    public static final ConfigBoolean AutoRestock = ConfigFactory.ofBoolean("ommc.auto_restock", true, "使mite内置补货更快");
    public static final ConfigEnum<ToolSwitchMode> ToolSwitch = ofEnum("ommc.tool_switch_mode", ToolSwitchMode.None);
    public static final ConfigBoolean WeaponSwitch = ofBoolean("ommc.weapon_switch");


    //hotkeys
    public static final ConfigHotkey OpenConfigScreen = ofHotkey("打开配置屏幕", KeyCodes.getStorageString(Keyboard.KEY_I, Keyboard.KEY_C));
    public static final ConfigHotkey SortInventory = ofHotkey("整理物品栏", KeybindMulti.fromStorageString("R", KeybindSettings.GUI), "按区域进行, 自动兼容几乎所有模组");
    public static final ConfigHotkey DropSimilar = ofHotkey("丢出类似物品", createKeyForGui(Keyboard.KEY_LSHIFT, Keyboard.KEY_Q), null);
    public static final ConfigHotkey ThrowSection = ofHotkey("清空区域", createKeyForGui(Keyboard.KEY_SPACE, Keyboard.KEY_Q), "全部丢出");
    public static final ConfigHotkey TradingRestock = ofHotkey("交易补货", createKeyForModifierGui(Keyboard.KEY_SPACE), "类似高版本空格补货");

    public static final ConfigHotkey ModifierMoveSimilar = ofHotkey("移动类似物品(修饰键)", createKeyForGui(Keyboard.KEY_LCONTROL), "左键并按下可以移动同类物品");
    public static final ConfigHotkey ModifierMoveAll = ofHotkey("移动全部(修饰键)", createKeyForGui(Keyboard.KEY_SPACE), "左键并按下可以移动全部物品");
    public static final ConfigHotkey ModifierSpreadItem = ofHotkey("分散物品(修饰键)", createKeyForGui(Keyboard.KEY_LMENU), "按住时点击会尝试将手中物品均分到点击区域全部槽位");

    private static final WiseInventoryConfig Instance;

    public WiseInventoryConfig() {
        super(WiseInventory.MOD_ID, ALL_HOTKEYS, ALL_VALUES);
    }

    public static SimpleConfigs getInstance() {
        return Instance;
    }

    static {
        ALL_VALUES = List.of(
                ContinuousOperation,
                WheelMoving,
                BetterQuickMoving,
                BetterContainerClosing,
                CachedSorting,
                ItemSortingOrder,
                AutoCrafting,
                AutoRestock,
                ToolSwitch,
                WeaponSwitch
        );

        ALL_HOTKEYS = List.of(
                OpenConfigScreen,
                SortInventory,
                DropSimilar,
                ThrowSection,
                TradingRestock,
                ModifierMoveSimilar,
                ModifierMoveAll,
                ModifierSpreadItem
        );

        Instance = new WiseInventoryConfig();
    }
}
