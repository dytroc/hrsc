package io.github.dytroInc.hrsc.mixin;

import io.github.dytroInc.hrsc.HRSCMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.InetSocketAddress;

@Environment(EnvType.CLIENT)
@Mixin(ClientConnection.class)
public class ConnectionMixin {

    @Inject(method = "connect", at = @At("HEAD"))
    private static void connect(InetSocketAddress address, boolean useEpoll, CallbackInfoReturnable<ClientConnection> cir) {
        HRSCMod mod = HRSCMod.getInstance();
        if ("158.247.218.69".equals(address.getAddress().getHostAddress())) {
            mod.setRoinServer(true);
        }
    }

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(Text disconnectReason, CallbackInfo ci) {
        HRSCMod mod = HRSCMod.getInstance();
        mod.setRoinServer(false);
    }
}
