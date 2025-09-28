package net.warphan.iss_magicfromtheeast.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MFTEServerConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Double> SOULBURN_MAX_DAMAGE;
    public static final ModConfigSpec.ConfigValue<Integer> SOULBURN_DAMAGE_SCALING;
    public static final ModConfigSpec.ConfigValue<Double> IMPERMANENCE_LIMIT_PERCENT;

    static {
        BUILDER.comment("Serverside Configurations");
        {
            BUILDER.push("Spells");

            BUILDER.comment("The limit of damage Impermanence's Verdict spell can reach based on target's max health. Default: 0.4 (40% target's max health)");
            IMPERMANENCE_LIMIT_PERCENT = BUILDER.worldRestart().define("impermanencePercentLimit", 0.4);
            BUILDER.pop();
        }
        {
            BUILDER.push("Effects");

            BUILDER.comment("The maximum amount of damage soulburn effect can reach to. Default: 10.0");
            SOULBURN_MAX_DAMAGE = BUILDER.define("maxSoulburnDamage", 10.0);
            BUILDER.comment("The percent amount of damage soulburn effect dealt based on target's max health. Default: 5 (5% of target's max health)");
            SOULBURN_DAMAGE_SCALING = BUILDER.define("soulburnDamageScaling", 5);
        }

        SPEC = BUILDER.build();
    }

    public static void onConfigReload() {}
}
