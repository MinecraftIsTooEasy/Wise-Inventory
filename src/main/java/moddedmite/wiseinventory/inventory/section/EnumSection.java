package moddedmite.wiseinventory.inventory.section;

public enum EnumSection {
    Armor,
    InventoryStorage,
    InventoryHotBar,

    InventoryWhole {
        @Override
        public ContainerSection get() {
            return InventoryHotBar.get().mergeWith(InventoryStorage.get());
        }
    },

    OffHand,// not exist in pre1.9

    CreativeTab,

    CraftMatrix,// this is widely used, crafter(grandpa cow), crafting, player crafting
    CraftResult,// this is widely used, cartography, crafter(grandpa cow), crafting, forging, grindstone, player crafting, stone cutter

    FurnaceIn,
    FurnaceOut,
    FurnaceFuel,

    MerchantIn,
    MerchantOut,

    BrewingBottles,
    BrewingIngredient,
    BrewingFuel,// no fuel in 1.6.4

    StoneCutterIn,

    CartographyIn,
    CartographyIn2,

    AnvilIn1,
    AnvilIn2,

    SmithIn1,
    SmithIn2,
    SmithIn3,

    GrindstoneIn,

    FakePlayerActions,
    FakePlayerArmor,
    FakePlayerInventoryStorage,
    FakePlayerInventoryHotBar,
    FakePlayerOffHand,

    FakePlayerEnderChestActions,
    FakePlayerEnderChestInventory,

    Unidentified,// Generally, it is a simple section like chest or shulker box, or absent in special containers that already identified in SectionIdentifier.

    Container {// Those slots in current container that are not for player inventory.

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public ContainerSection get() {
            return SectionHandler.streamAllSections()
                    .filter(section -> {
                        if (section.isOf(EnumSection.InventoryHotBar)) return false;
                        if (section.isOf(EnumSection.InventoryStorage)) return false;
                        return true;
                    })
                    .reduce(ContainerSection::mergeWith).orElse(ContainerSection.EMPTY);
        }
    },
    ;

    public ContainerSection get() {
        return SectionHandler.getSection(this);
    }
}
