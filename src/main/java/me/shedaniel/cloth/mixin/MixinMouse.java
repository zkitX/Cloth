package me.shedaniel.cloth.mixin;

import me.shedaniel.cloth.api.ClientUtils;
import me.shedaniel.cloth.hooks.ClothClientHooks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Window;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {
    
    @Shadow @Final private MinecraftClient client;
    
    @Shadow private int activeButton;
    
    @Shadow private double x;
    
    @Shadow private double y;
    
    @Inject(method = "onMouseScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;mouseScrolled(DDD)Z",
                     ordinal = 0), cancellable = true)
    public void onMouseScrolled(long long_1, double double_1, double double_2, CallbackInfo info) {
        double x = ClientUtils.getInstance().getMouseX(), y = ClientUtils.getInstance().getMouseY();
        double amount = double_2 * this.client.options.mouseWheelSensitivity;
        if (!info.isCancelled()) {
            ActionResult result = ClothClientHooks.SCREEN_MOUSE_SCROLLED.invoker().mouseScrolled(client, client.currentScreen, x, y, amount);
            if (result != ActionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "onMouseButton", at = @At(value = "INVOKE",
                                               target = "Lnet/minecraft/client/gui/screen/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                               ordinal = 0), cancellable = true)
    public void onMouseClicked(long long_1, int int_1, int int_2, int int_3, CallbackInfo info) {
        double x = ClientUtils.getInstance().getMouseX(), y = ClientUtils.getInstance().getMouseY();
        if (!info.isCancelled()) {
            ActionResult result = ClothClientHooks.SCREEN_MOUSE_CLICKED.invoker().mouseClicked(client, client.currentScreen, x, y, activeButton);
            if (result != ActionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "onMouseButton", at = @At(value = "INVOKE",
                                               target = "Lnet/minecraft/client/gui/screen/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                               ordinal = 1), cancellable = true)
    public void onMouseReleased(long long_1, int int_1, int int_2, int int_3, CallbackInfo info) {
        double x = ClientUtils.getInstance().getMouseX(), y = ClientUtils.getInstance().getMouseY();
        if (!info.isCancelled()) {
            ActionResult result = ClothClientHooks.SCREEN_MOUSE_RELEASED.invoker().mouseReleased(client, client.currentScreen, x, y, activeButton);
            if (result != ActionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "onCursorPos", at = @At(value = "INVOKE",
                                             target = "Lnet/minecraft/client/gui/screen/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                             ordinal = 1), cancellable = true)
    public void onMouseDragged(long long_1, double double_1, double double_2, CallbackInfo info) {
        Window window = MinecraftClient.getInstance().getWindow();
        double double_3 = double_1 * (double) window.getScaledWidth() / (double) window.getWidth();
        double double_4 = double_2 * (double) window.getScaledHeight() / (double) window.getHeight();
        double double_5 = (double_1 - this.x) * (double) window.getScaledWidth() / (double) window.getWidth();
        double double_6 = (double_2 - this.y) * (double) window.getScaledHeight() / (double) window.getHeight();
        if (!info.isCancelled()) {
            ActionResult result = ClothClientHooks.SCREEN_MOUSE_DRAGGED.invoker().mouseDragged(client, client.currentScreen, double_3, double_4, activeButton, double_5, double_6);
            if (result != ActionResult.PASS)
                info.cancel();
        }
    }
    
}
