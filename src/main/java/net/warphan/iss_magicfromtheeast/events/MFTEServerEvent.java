package net.warphan.iss_magicfromtheeast.events;

import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ChallengedSoul;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;

@EventBusSubscriber
public class MFTEServerEvent {

    @SubscribeEvent
    public static void useBottleOnSoulFire(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        BlockPos pos = event.getHitVec().getBlockPos();
        var block = event.getLevel().getBlockState(pos);
        var hand = event.getHand();
        var useItem = player.getItemInHand(hand);
        if (useItem.is(Items.GLASS_BOTTLE) && block.is(Blocks.SOUL_FIRE)) {
            player.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.SOUL_ESCAPE, SoundSource.NEUTRAL, 1.0f, 1.0f);
            player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
            player.swing(hand);
            player.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 40, 0));
            player.setItemInHand(hand, ItemUtils.createFilledResult(useItem, player, new ItemStack(MFTEItemRegistries.BOTTLE_OF_SOULS.get())));
            event.setCancellationResult(InteractionResultHolder.consume(player.getItemInHand(hand)).getResult());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void moreDangerousSoulFire(EntityTickEvent.Pre event) {
        var entity = event.getEntity();
        var level = entity.level;
        if (!level.isClientSide) {
            if (entity.tickCount % 20 == 0) {
                BlockPos pos = entity.blockPosition();
                BlockState blockState = entity.level.getBlockState(pos);
                if (blockState.is(Blocks.SOUL_FIRE) || blockState.is(Blocks.SOUL_CAMPFIRE) || blockState.is(BlockRegistry.BRAZIER_SOUL)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 160, 0));
                    }
                }
            }
        }
    }
    // NOTE: Fix the problem with Soul Fire on Soul Sand

    @SubscribeEvent
    public static void weaponBreakingSoul(LivingDamageEvent.Post event) {
        var damageSource = event.getSource();
        var target = event.getEntity();
        if (damageSource.getEntity() instanceof LivingEntity attacker && (damageSource.getDirectEntity() == attacker || damageSource.getDirectEntity() instanceof AbstractArrow) && !(damageSource instanceof SpellDamageSource)) {
            var hand = attacker.getUsedItemHand();
            var attackItem = attacker.getItemInHand(hand);
            if (attackItem.is(MFTEItemRegistries.SOUL_BREAKER)) {
                target.hurt(target.damageSources().magic(), 5);
            }
        }
    }

    @SubscribeEvent
    public static void ignoreKnockBackEntityList(LivingKnockBackEvent event) {
        var entity = event.getEntity();
        if (entity instanceof ChallengedSoul || entity instanceof BoneHandsEntity || entity instanceof JadeExecutionerEntity) {
            event.setCanceled(true);
        }
    }

    //Soul Challenging Events
    @SubscribeEvent
    public static void linkedSoulChallengingEvent(EntityTickEvent.Pre event) {
        var entity = event.getEntity();
        if (entity instanceof ChallengedSoul challengedSoul) {
            var soulOwner = challengedSoul.getSummoner();
            float linkingRange = 10.0f;
            if (!challengedSoul.level.isClientSide && soulOwner != null) {
                float distance = challengedSoul.distanceTo(soulOwner);
                if (distance > linkingRange) {
                    challengedSoul.onUnSummon();
                    soulOwner.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 200, 0));
                    soulOwner.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, 200, 3));
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void soulOwnerHurtEvent(LivingDamageEvent.Post event) {
        var entity = event.getEntity();
        if (entity instanceof ChallengedSoul challengedSoul1) {
            var soulOwner = challengedSoul1.getSummoner();
            var damageOnSoul = event.getOriginalDamage();
            var damageAmount = damageOnSoul * challengedSoul1.bonusPercent;
            if (soulOwner != null) {
            if (damageOnSoul <= challengedSoul1.getHealth()) {
                soulOwner.hurt(soulOwner.damageSources().magic(), damageAmount);
            } else if (damageOnSoul > challengedSoul1.getHealth()) {
                soulOwner.hurt(soulOwner.damageSources().magic(), challengedSoul1.getHealth() * challengedSoul1.bonusPercent);
            }
            }
        }
    }

    @SubscribeEvent
    public static void onSoulCrushedEvent(LivingDeathEvent event) {
        var entity = event.getEntity();
        if (!entity.level.isClientSide) {
            if (entity instanceof ChallengedSoul challengedSoul) {
                var soulOwner = challengedSoul.getSummoner();
                if (soulOwner != null) {
                    soulOwner.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 200, 0));
                    soulOwner.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, 200, 3));
                }
            }
        }
    }

    @SubscribeEvent
    public static void counterSpellCastOnSoul(CounterSpellEvent event) {
        var entity = event.target;
        var caster = event.caster;
        if (entity instanceof ChallengedSoul challengedSoul) {
            if (caster == challengedSoul.getSummoner()) {
                var soulOwner = challengedSoul.getSummoner();
                soulOwner.addEffect(new MobEffectInstance(MFTEEffectRegistries.SOULBURN, 200, 0));
                soulOwner.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, 200, 3));
            }
        }
    }

    @SubscribeEvent
    public static void jadeExecutionerAntiCounterSpell(CounterSpellEvent event) {
        var target = event.target;
        var caster = event.caster;
        if (target instanceof JadeExecutionerEntity jadeExecutioner) {
            if (caster != jadeExecutioner.getSummoner()) {
                event.setCanceled(true);
                float percentDamage = jadeExecutioner.getMaxHealth() / 10;
                jadeExecutioner.hurt(jadeExecutioner.damageSources().generic(), percentDamage);
            }
        }
    }

    @SubscribeEvent
    public static void spiritSamuraiAntiCounterSpell(CounterSpellEvent event) {
        var target = event.target;
        var caster = event.caster;
        if (target instanceof SpiritSamuraiEntity samurai) {
            if (caster != samurai.getSummoner()) {
                event.setCanceled(true);
                float percentDamage = samurai.getMaxHealth() / 3;
                samurai.hurt(samurai.damageSources().generic(), percentDamage);
            }
        }
    }

    @SubscribeEvent
    public static void drapeReflectionEvent(ProjectileImpactEvent event) {
        var ray = event.getRayTraceResult();
        var projectile = event.getProjectile();
        if (ray instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof ShieldPart shieldPart && shieldPart.parentEntity instanceof JadeDrapesEntity jadeDrapes) {
            event.setCanceled(true);
            if (projectile.getOwner() != jadeDrapes.getSummoner()) {
            Vec3 reflectionVec = projectile.getDeltaMovement().reverse();

            if (projectile instanceof AbstractMagicProjectile magicProjectile) {
                magicProjectile.setOwner(jadeDrapes.getSummoner());
                magicProjectile.setDamage(magicProjectile.getDamage() * jadeDrapes.percentReflectDamage);
            }

            jadeDrapes.playSound(SoundEvents.ENDER_EYE_DEATH);
            projectile.setDeltaMovement(reflectionVec);
            }

            //For some special case
            if (projectile.getOwner() == null) {
                projectile.setOwner(jadeDrapes.getSummoner());

                jadeDrapes.playSound(SoundEvents.ENDER_EYE_DEATH);
                projectile.setDeltaMovement(projectile.getDeltaMovement().reverse());
            }
        }
    }

    @SubscribeEvent
    public static void counterSpellHitDrapes(CounterSpellEvent event) {
        var target = event.target;
        if (target instanceof JadeDrapesEntity jadeDrapes) {
            event.setCanceled(false);
            jadeDrapes.onUnSummon();
        }
    }

    //@SubscribeEvent
}
