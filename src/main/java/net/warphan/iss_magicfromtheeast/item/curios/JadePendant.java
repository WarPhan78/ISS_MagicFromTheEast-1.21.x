package net.warphan.iss_magicfromtheeast.item.curios;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.item.curios.PassiveAbilityCurio;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.warphan.iss_magicfromtheeast.compat.MFTECurios;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.setup.MFTERarity;

@EventBusSubscriber
public class JadePendant extends PassiveAbilityCurio {
    public static final int COOLDOWN_IN_TICKS = 8 * 20;
    public static final int RADIUS = 5;
    public static final int RADIUS_SPR = RADIUS * RADIUS;

    public JadePendant() {
        super(new Properties().stacksTo(1).rarity(MFTERarity.JADELIGHT_RARITY_PROXY.getValue()), MFTECurios.BELT_SLOT);
    }

    @Override
    protected int getCooldownTicks() {
        return COOLDOWN_IN_TICKS;
    }

    @SubscribeEvent
    public static void toughnessReflection(LivingIncomingDamageEvent event) {
        var PENDANT = ((JadePendant) MFTEItemRegistries.JADE_PENDANT.get());
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getSource().getEntity() != null && PENDANT.isEquippedBy(player) && PENDANT.tryProcCooldown(player)) {
                var vec3 = player.getBoundingBox().getCenter();
                var level = player.level;

                //reflection visual
                if (!level.isClientSide) {
                    MagicManager.spawnParticles(level, new BlastwaveParticleOptions(MFTESchoolRegistries.SYMMETRY.get().getTargetingColor(), RADIUS), vec3.x, vec3.y + .165f, vec3.z, 1, 0, 0, 0, 0, true);
                }
                level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundRegistry.ECHOING_STRIKE, SoundSource.NEUTRAL, 1.0f, 1.0f);

                //damage
                float incomingDamage = event.getOriginalAmount();
                double armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue();
                float reflectDamage = incomingDamage * (0.5f + (float) (0.05f * armorToughness));
                var source = player.damageSources().source(MFTEDamageTypes.PENDANT_REFLECT, player);
                level.getEntities(player, player.getBoundingBox().inflate(RADIUS, 4, RADIUS), (target) -> !DamageSources.isFriendlyFireBetween(target, player)
                        && Utils.hasLineOfSight(level, player, target, true)).forEach(target -> {
                    if (target instanceof LivingEntity livingEntity && livingEntity.distanceToSqr(player) < RADIUS_SPR) {
                        DamageSources.applyDamage(target, reflectDamage, source);
                    }}
                );
            }
        }
    }
}
