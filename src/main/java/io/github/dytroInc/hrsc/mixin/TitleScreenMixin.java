package io.github.dytroInc.hrsc.mixin;

import io.github.dytroInc.hrsc.HRSCMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    private final ServerAddress address = new ServerAddress("roin.tv", 25565);
    private final ServerInfo info = new ServerInfo("Roin Online", "roin.tv:25565", false);

    @Inject(method = "initWidgetsNormal", at = @At("RETURN"))
    private void drawButtons(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(new TexturedButtonWidget(
                this.width / 2 + 104, y + spacingY, 20, 20, 0, 0, 20, new Identifier("hrsc:textures/gui/roin_server_button.png"), 20, 40,
                (button -> ConnectScreen.connect(
                        HRSCMod.getClient().currentScreen,
                        HRSCMod.getClient(),
                        address,
                        info
                ))
        ));
    }
}
