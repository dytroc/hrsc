package io.github.dytroInc.hrsc.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.dytroInc.hrsc.HRSCMod;
import io.github.dytroInc.hrsc.enums.ChatFilter;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatFilterGUI extends LightweightGuiDescription {
    public ChatFilterGUI() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300, 40);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(new LiteralText("사용할 채팅 필터"));
        root.add(label, 0, 0, 2, 1);

        AtomicInteger index = new AtomicInteger();
        Arrays.stream(ChatFilter.values()).forEachOrdered((filterType) -> {
            WButton button = new WButton(new TranslatableText(filterType.getTranslationKey()));
            button.setOnClick(() -> {
                HRSCMod.setChatFilter(filterType);
                HRSCMod.getClient().player.sendMessage(new LiteralText("사용하고 있는 채팅 필터 : ").append(new TranslatableText(
                        filterType.getTranslationKey()
                )).setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false);
            });

            root.add(button, index.getAndIncrement() * 6, 1, 5, 1);
        });

        root.validate(this);
    }
}
