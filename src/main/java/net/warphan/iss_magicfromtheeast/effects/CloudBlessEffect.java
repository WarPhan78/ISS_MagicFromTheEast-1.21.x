package net.warphan.iss_magicfromtheeast.effects;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;

@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID)
public class CloudBlessEffect extends MagicMobEffect {
    public CloudBlessEffect(MobEffectCategory category, int pColor) {
        super(category, pColor);
    }

    @SubscribeEvent
    public static void negateFallDamage(LivingHurtEvent event) {
        var entity = event.getEntity();
        var damageSource = event.getSource();
        var effect = entity.getEffect(MFTEEffectRegistries.CLOUD_BLESS_EFFECT.get());
        if (!entity.level.isClientSide && effect != null && damageSource.is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
        }
    }
}
