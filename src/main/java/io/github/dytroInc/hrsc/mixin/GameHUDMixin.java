package io.github.dytroInc.hrsc.mixin;

import io.github.dytroInc.hrsc.HRSCMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class GameHUDMixin {
    HRSCMod mod = HRSCMod.getInstance();

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void onRenderSidebar(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo ci) {
        if (mod.isRoinSurvival()) {
            Scoreboard scoreboard = objective.getScoreboard();
            List<MutableText> list = scoreboard.getAllPlayerScores(objective).stream()
                    .map((score) -> {
                        if (score.getPlayerName() == null) return new LiteralText("");
                        Team team = scoreboard.getPlayerTeam(score.getPlayerName());
                        MutableText text = Team.decorateName(team, new LiteralText(score.getPlayerName()));
                        return text;
                    })
                    .filter((text) -> {
                        List<Text> siblings1 = text.getSiblings();
                        if (siblings1.size() <= 1) return false;
                        List<Text> siblings2 = siblings1.get(0).getSiblings();
                        if (siblings2.size() == 0) return false;
                        return "코인: ".equals(siblings2.get(0).asString()) || "서버 TPS : ".equals(siblings2.get(0).asString());
                    })
                    .toList();
            if (list.size() > 0) {
                HRSCMod.getClient().player.sendMessage(
                        list.get(0).formatted(Formatting.GOLD).append(new LiteralText(" | ").formatted(Formatting.WHITE)).append(list.get(1))
                        , true);
            }
            ci.cancel();
        }
    }
}
