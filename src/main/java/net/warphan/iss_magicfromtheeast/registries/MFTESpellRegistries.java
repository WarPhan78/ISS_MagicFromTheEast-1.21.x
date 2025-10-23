package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.spells.spirit.*;
import net.warphan.iss_magicfromtheeast.spells.symmetry.*;

import java.util.function.Supplier;

public class MFTESpellRegistries extends SpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    //SYMMETRY SPELLS
//    public static final Supplier<AbstractSpell> LAUNCH_SPELL = registerSpell(new LaunchSpell());
//    public static final Supplier<AbstractSpell> FORCE_SWORD_SPELL = registerSpell(new ForceSwordSpell());
    public static final Supplier<AbstractSpell> QIGONG_CONTROLLING_SPELL = registerSpell(new QigongControllingSpell());
    public static final Supplier<AbstractSpell> SWORD_DANCE_SPELL = registerSpell(new SwordDanceSpell());
    public static final Supplier<AbstractSpell> BAGUA_ARRAY_CIRCLE_SPELL = registerSpell(new BaguaArrayCircleSpell());
    public static final Supplier<AbstractSpell> DRAGON_GLIDE_SPELL = registerSpell(new DragonGlideSpell());
    public static final Supplier<AbstractSpell> JADE_JUDGEMENT_SPELL = registerSpell(new JadeJudgementSpell());
    public static final Supplier<AbstractSpell> JIANGSHI_INVOKE_SPELL = registerSpell(new JiangshiInvokeSpell());
//  public static final Supplier<AbstractSpell> JADE_SENTINEL_SPELL = registerSpell(new JadeSentinelSpell());
    public static final Supplier<AbstractSpell> UNDERWORLD_AID_SPELL = registerSpell(new UnderworldAidSpell());
    public static final Supplier<AbstractSpell> PUNISHING_HEAVEN_SPELL = registerSpell(new PunishingHeavenSpell());
    public static final Supplier<AbstractSpell> DRAPES_OF_REFLECTION = registerSpell(new DrapesOfReflectionSpell());
    public static final Supplier<AbstractSpell> CLOUD_RIDE_SPELL = registerSpell(new CloudRideSpell());

    //SPIRIT SPELLS
    public static final Supplier<AbstractSpell> SOUL_CATALYST_SPELL = registerSpell(new SoulCatalystSpell());
    public static final Supplier<AbstractSpell> SOUL_BURST_SPELL = registerSpell(new SoulBurstSpell());
    public static final Supplier<AbstractSpell> SPIRIT_CHALLENGING = registerSpell(new SpiritChallengingSpell());
    public static final Supplier<AbstractSpell> BONE_HANDS_SPELL = registerSpell(new BoneHandsSpell());
    public static final Supplier<AbstractSpell> CALAMITY_CUT_SPELL = registerSpell(new CalamityCutSpell());
    public static final Supplier<AbstractSpell> KITSUNE_PACK_SPELL = registerSpell(new KitsunePackSpell());
    public static final Supplier<AbstractSpell> REVENANT_OF_HONOR_SPELL = registerSpell(new RevenantOfHonorSpell());
    public static final Supplier<AbstractSpell> ASHIGARU_SQUAD_SPELL = registerSpell(new AshigaruSquadSpell());
    public static final Supplier<AbstractSpell> PHANTOM_CHARGE_SPELL = registerSpell(new PhantomChargeSpell());

    //DUNE SPELLS


}
