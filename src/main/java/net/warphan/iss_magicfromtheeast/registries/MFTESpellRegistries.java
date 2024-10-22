package net.warphan.iss_magicfromtheeast.registries;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
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
    public static final Supplier<AbstractSpell> THROW_UP_SPELL = registerSpell(new ThrowUpSpell());
    public static final Supplier<AbstractSpell> FORCE_SWORD = registerSpell(new ForceSword());
    public static final Supplier<AbstractSpell> QIGONG_CONTROLLING = registerSpell(new QigongControlling());
    public static final Supplier<AbstractSpell> BAGUA_ARRAY_CIRCLE = registerSpell(new BaguaArrayCircle());
    public static final Supplier<AbstractSpell> DRAGON_GLIDE = registerSpell(new DragonGlide());
    public static final Supplier<AbstractSpell> JADE_JUDGEMENT = registerSpell(new JadeJudgement());
    public static final Supplier<AbstractSpell> JIANGSHI_INVOKE = registerSpell(new JiangshiInvoke());
    public static final Supplier<AbstractSpell> JADE_SENTINEL = registerSpell(new JadeSentinel());
    public static final Supplier<AbstractSpell> UNDERWORLD_AID = registerSpell(new UnderworldAid());

    //SPIRIT SPELLS


    //DUNE SPELLS


}
