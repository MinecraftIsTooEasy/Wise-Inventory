package moddedmite.wiseinventory.mixins;

import moddedmite.wiseinventory.config.WiseInventoryConfig;
import moddedmite.wiseinventory.feat.ItemSwitch;
import moddedmite.wiseinventory.feat.ToolSwitchMode;
import net.minecraft.Minecraft;
import net.minecraft.PlayerControllerMP;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public PlayerControllerMP playerController;

    @Inject(method = "tryClickEntity", at = @At("HEAD"))
    private void switchWeapon(int button, CallbackInfoReturnable<Boolean> cir) {
        if (!WiseInventoryConfig.WeaponSwitch.getBooleanValue() || this.playerController.isInCreativeMode()) return;
        ItemSwitch.trySwitchToolOrWeapon(false);
    }

    @Inject(method = "sendClickBlockToController", at = @At(value = "FIELD", target = "Lnet/minecraft/RaycastCollision;block_hit_x:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    private void switchTool(int par1, boolean par2, CallbackInfo ci) {
        if (WiseInventoryConfig.ToolSwitch.getEnumValue() == ToolSwitchMode.None || this.playerController.isInCreativeMode()) return;
        ItemSwitch.trySwitchToolOrWeapon(true);
    }
}
