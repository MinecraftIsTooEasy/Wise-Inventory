package moddedmite.wiseinventory.mixins;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.event.TempInputHandler;
import moddedmite.wiseinventory.feat.BetterContainerClosing;
import moddedmite.wiseinventory.inventory.InventoryTweaks;
import moddedmite.wiseinventory.inventory.InventoryUtil;
import moddedmite.wiseinventory.inventory.section.SectionHandler;
import moddedmite.wiseinventory.util.ItemUtil;
import net.minecraft.GuiContainer;
import net.minecraft.GuiScreen;
import net.minecraft.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {
    @Shadow
    public Slot theSlot;

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiScreen;mouseClicked(III)V", shift = At.Shift.AFTER), cancellable = true)
    private void preTricks(int par1, int par2, int button, CallbackInfo ci) {
        if (TempInputHandler.shouldCancelClick((GuiContainer) (Object) this, button, this.theSlot)) {
            ci.cancel();
        }
    }

    @Inject(method = "mouseMovedOrUp", at = @At("HEAD"), cancellable = true)
    private void cancelRelease(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (TempInputHandler.shouldCancelRelease(mouseButton)) {
            ci.cancel();
        }
    }

    @Inject(method = "mouseClickMove", at = @At("HEAD"), cancellable = true)
    private void onMouseMove(int par1, int par2, int par3, long par4, CallbackInfo ci) {
        TempInputHandler.mouseMoved(par1, par2);
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onRender(int par1, int par2, float par3, CallbackInfo ci) {
        InventoryTweaks.onRender((GuiContainer) (Object) this, par1, par2, this.theSlot);
    }


    @Inject(method = "keyTyped", at = @At("HEAD"))
    private void dropAll(char par1, int par2, CallbackInfo ci) {
        Slot mouseOver = this.theSlot;
        if (InventoryTweaks.shouldDoTrick((GuiContainer) (Object) this, mouseOver) && WiseInventoryConfig.DropSimilar.getKeybind().isKeybindHeld()) {
            SectionHandler.getSection(mouseOver).predicateRun(ItemUtil.predicateIDMeta(mouseOver.getStack()), InventoryUtil::dropStack);
        }
    }

    @Inject(method = "keyTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityClientPlayerMP;closeScreen()V"))
    private void onCloseScreen(char typedChar, int keyCode, CallbackInfo ci) {
        BetterContainerClosing.onCloseScreen();
    }

}
