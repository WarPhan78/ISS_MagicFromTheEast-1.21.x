package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.spells.spirit.*;
import net.warphan.iss_magicfromtheeast.spells.symmetry.*;

public class MFTESpellRegistries extends SpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, ISS_MagicFromTheEast.MOD_ID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    //SYMMETRY SPELLS
//    public static final RegistryObject<AbstractSpell> LAUNCH_SPELL = registerSpell(new LaunchSpell());
//    public static final RegistryObject<AbstractSpell> QIGONG_CONTROLLING_SPELL = registerSpell(new QigongControllingSpell());
    public static final RegistryObject<AbstractSpell> SWORD_DANCE_SPELL = registerSpell(new SwordDanceSpell());
    public static final RegistryObject<AbstractSpell> BAGUA_ARRAY_CIRCLE_SPELL = registerSpell(new BaguaArrayCircleSpell());
    public static final RegistryObject<AbstractSpell> DRAGON_GLIDE_SPELL = registerSpell(new DragonGlideSpell());
    public static final RegistryObject<AbstractSpell> JADE_JUDGEMENT_SPELL = registerSpell(new JadeJudgementSpell());
    public static final RegistryObject<AbstractSpell> JIANGSHI_INVOKE_SPELL = registerSpell(new JiangshiInvokeSpell());
    public static final RegistryObject<AbstractSpell> UNDERWORLD_AID_SPELL = registerSpell(new UnderworldAidSpell());
    public static final RegistryObject<AbstractSpell> PUNISHING_HEAVEN_SPELL = registerSpell(new PunishingHeavenSpell());
    public static final RegistryObject<AbstractSpell> DRAPES_OF_REFLECTION_SPELL = registerSpell(new DrapesOfReflectionSpell());
    public static final RegistryObject<AbstractSpell> CLOUD_RIDE_SPELL = registerSpell(new CloudRideSpell());
    public static final RegistryObject<AbstractSpell> NEPHRITE_SLASH_SPELL = registerSpell(new NephriteSlashSpell());
    public static final RegistryObject<AbstractSpell> JADE_BULLET_SPELL = registerSpell(new JadeBulletSpell());

    //SPIRIT SPELLS
    public static final RegistryObject<AbstractSpell> SOUL_CATALYST_SPELL = registerSpell(new SoulCatalystSpell());
    public static final RegistryObject<AbstractSpell> SOUL_BURST_SPELL = registerSpell(new SoulBurstSpell());
    public static final RegistryObject<AbstractSpell> SPIRIT_CHALLENGING = registerSpell(new SpiritChallengingSpell());
    public static final RegistryObject<AbstractSpell> BONE_HANDS_SPELL = registerSpell(new BoneHandsSpell());
    public static final RegistryObject<AbstractSpell> CALAMITY_CUT_SPELL = registerSpell(new CalamityCutSpell());
    public static final RegistryObject<AbstractSpell> KITSUNE_PACK_SPELL = registerSpell(new KitsunePackSpell());
    public static final RegistryObject<AbstractSpell> REVENANT_OF_HONOR_SPELL = registerSpell(new RevenantOfHonorSpell());
    public static final RegistryObject<AbstractSpell> ASHIGARU_SQUAD_SPELL = registerSpell(new AshigaruSquadSpell());
    public static final RegistryObject<AbstractSpell> PHANTOM_CHARGE_SPELL = registerSpell(new PhantomChargeSpell());
    public static final RegistryObject<AbstractSpell> ANCHORING_KUNAI = registerSpell(new AnchoringKunaiSpell());
    public static final RegistryObject<AbstractSpell> SPLITTING_SHURIKEN = registerSpell(new SplittingShurikenSpell());

    //DUNE SPELLS

}
