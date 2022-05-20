package io.github.dytroInc.hrsc.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.dytroInc.hrsc.HRSCMod;
import io.github.dytroInc.hrsc.utils.VersionUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkMixin {
    HRSCMod mod = HRSCMod.getInstance();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo callbackInfo) {
        if (packet.getType() == MessageType.CHAT) {
            if (RenderSystem.isOnRenderThread()) {
                if (mod.handleChat(packet.getMessage())) callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (!VersionUtils.isLatest()) {
            assert HRSCMod.getClient().player != null;
            VersionUtils.showUpdateMessage(HRSCMod.getClient().player);
        }
    }
}
