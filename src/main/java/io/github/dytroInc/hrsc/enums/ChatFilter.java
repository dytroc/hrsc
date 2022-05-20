package io.github.dytroInc.hrsc.enums;

public enum ChatFilter {
    BLOCK_ALL("filter.block_all.name"),
    BLOCK_NON_ADMINS("filter.block_non_admins.name"),
    BLOCK_NOTHING("filter.block_nothing.name");

    private final String translationKey;

    ChatFilter(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public static ChatFilter fromString(String string) {
        if (string == null) return BLOCK_NOTHING;
        if (string.equals("ALL")) return BLOCK_ALL;
        if (string.equals("NON_ADMINS")) return BLOCK_NON_ADMINS;
        return BLOCK_NOTHING;
    }

    public String asString() {
        if (this == BLOCK_ALL) return "ALL";
        if (this == BLOCK_NON_ADMINS) return "NON_ADMINS";
        return "NOTHING";
    }
}
