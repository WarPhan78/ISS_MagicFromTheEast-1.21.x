package net.warphan.iss_magicfromtheeast.events;

import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.datagen.MFTEDamageTypeTagGenerator;
import net.warphan.iss_magicfromtheeast.entity.mobs.bone_hands.BoneHandsEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.jade_executioner.JadeExecutionerEntity;
import net.warphan.iss_magicfromtheeast.entity.mobs.spirit_samurai.SpiritSamuraiEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.jade_drape.JadeDrapesEntity;
import net.warphan.iss_magicfromtheeast.entity.spells.spirit_challenging.ExtractedSoul;
import net.warphan.iss_magicfromtheeast.item.armor.BootsOfMistArmorItem;
import net.warphan.iss_magicfromtheeast.item.armor.ElementalCommanderArmorItem;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
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
    public static void ignoreKnockBackEntityList(LivingKnockBackEvent event) {
        var entity = event.getEntity();
        if (entity instanceof ExtractedSoul
                || entity instanceof BoneHandsEntity
                || entity instanceof JadeExecutionerEntity)
        {
            event.setCanceled(true);
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

    //Flag effects action
    @SubscribeEvent
    public static void modifyElementalAttack(LivingIncomingDamageEvent event) {
        Entity attacker = event.getSource().getEntity();
        var type = event.getSource();
        if (attacker instanceof LivingEntity livingAttacker) {
            double enhancingBonus = livingAttacker.getAttributeValue(MFTEAttributeRegistries.SYMMETRY_SPELL_POWER) * 0.25;
            var oldDamageAmount = event.getAmount();

            var effectFire = livingAttacker.getEffect(MFTEEffectRegistries.FLAG_FIRE);
            var effectNature = livingAttacker.getEffect(MFTEEffectRegistries.FLAG_NATURE);
            var effectLightning = livingAttacker.getEffect(MFTEEffectRegistries.FLAG_LIGHTNING);
            var effectIce = livingAttacker.getEffect(MFTEEffectRegistries.FLAG_ICE);
            var effectHoly = livingAttacker.getEffect(MFTEEffectRegistries.FLAG_HOLY);

                if (effectFire != null && type.is(ISSDamageTypes.FIRE_MAGIC)) {
                    event.setAmount(oldDamageAmount + (oldDamageAmount * (float) enhancingBonus));
                }
                if (effectNature != null && type.is(ISSDamageTypes.NATURE_MAGIC)) {
                    event.setAmount(oldDamageAmount + (oldDamageAmount * (float) enhancingBonus));
                }
                if (effectLightning != null && type.is(ISSDamageTypes.LIGHTNING_MAGIC)) {
                    event.setAmount(oldDamageAmount + (oldDamageAmount * (float) enhancingBonus));
                }
                if (effectIce != null && type.is(ISSDamageTypes.ICE_MAGIC)) {
                    event.setAmount(oldDamageAmount + (oldDamageAmount * (float) enhancingBonus));
                }
                if (effectHoly != null && type.is(ISSDamageTypes.HOLY_MAGIC)) {
                    event.setAmount(oldDamageAmount + (oldDamageAmount * (float) enhancingBonus));
            }
        }
    }

    //Flag effects added
    @SubscribeEvent
    public static void onHitTriggerAbilityEvent(LivingDamageEvent.Pre event) {

        //Elemental Commander Chestplate passive
        Entity attacker = event.getSource().getEntity();
        var type = event.getSource();
        if (attacker instanceof LivingEntity livingAttacker) {
            if (livingAttacker.getItemBySlot(EquipmentSlot.CHEST).is(MFTEItemRegistries.ELEMENTAL_COMMANDER_CHESTPLATE) && (!(livingAttacker instanceof Player player) || !player.getCooldowns().isOnCooldown(MFTEItemRegistries.ELEMENTAL_COMMANDER_CHESTPLATE.get()))) {
                if (type.is(ISSDamageTypes.FIRE_MAGIC)) {
                    livingAttacker.addEffect(new MobEffectInstance(MFTEEffectRegistries.FLAG_FIRE, 200, 0));
                    doVisualAndSound(livingAttacker, SoundRegistry.FIRE_CAST, ParticleHelper.EMBERS);
                    processCooldown(livingAttacker);
                } else if (type.is(ISSDamageTypes.NATURE_MAGIC)) {
                    livingAttacker.addEffect(new MobEffectInstance(MFTEEffectRegistries.FLAG_NATURE, 200, 0));
                    doVisualAndSound(livingAttacker, SoundRegistry.NATURE_CAST, ParticleHelper.ACID_BUBBLE);
                    processCooldown(livingAttacker);
                } else if (type.is(ISSDamageTypes.LIGHTNING_MAGIC)) {
                    livingAttacker.addEffect(new MobEffectInstance(MFTEEffectRegistries.FLAG_LIGHTNING, 200, 0));
                    doVisualAndSound(livingAttacker, SoundRegistry.LIGHTNING_CAST, ParticleHelper.ELECTRICITY);
                    processCooldown(livingAttacker);
                } else if (type.is(ISSDamageTypes.ICE_MAGIC)) {
                    livingAttacker.addEffect(new MobEffectInstance(MFTEEffectRegistries.FLAG_ICE, 200, 0));
                    doVisualAndSound(livingAttacker, SoundRegistry.ICE_CAST, ParticleHelper.SNOWFLAKE);
                    processCooldown(livingAttacker);
                } else if (type.is(ISSDamageTypes.HOLY_MAGIC)) {
                    livingAttacker.addEffect(new MobEffectInstance(MFTEEffectRegistries.FLAG_HOLY, 200, 0));
                    doVisualAndSound(livingAttacker, SoundRegistry.HOLY_CAST, ParticleHelper.CLEANSE_PARTICLE);
                    processCooldown(livingAttacker);
                } else return;
            }
        }

        //Boots of Mist passive
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.level.isClientSide
                    || type.is(DamageTypeTagGenerator.BYPASS_EVASION)
                    || type.is(MFTEDamageTypes.SOUL_DAMAGE)
                    || type.is(DamageTypeTags.IS_FALL)
                    || type.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return;
            } else if (livingEntity.getItemBySlot(EquipmentSlot.FEET).is(MFTEItemRegistries.BOOTS_OF_MIST) && (!(livingEntity instanceof Player player) || !player.getCooldowns().isOnCooldown(MFTEItemRegistries.BOOTS_OF_MIST.get()))) {

                event.setNewDamage(0.0f);
                livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY, 120, 0, false, false, true));
                livingEntity.addEffect(new MobEffectInstance(MFTEEffectRegistries.MIST_STEP, 120, 0, false, false, true));

                if (livingEntity instanceof Player player) {
                    player.getCooldowns().addCooldown(MFTEItemRegistries.BOOTS_OF_MIST.get(), BootsOfMistArmorItem.COOLDOWN_TICKS);
                }
                return;
            }
        }
    }

    //For Elemental Commander Chestplate only
    public static void processCooldown(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            player.getCooldowns().addCooldown(MFTEItemRegistries.ELEMENTAL_COMMANDER_CHESTPLATE.get(), ElementalCommanderArmorItem.COOLDOWN_TICKS);
        } else return;
    }

    public static void doVisualAndSound(LivingEntity livingEntity, Holder<SoundEvent> soundEvents, ParticleOptions particleTypes) {
        var level = livingEntity.level;
        var pos = livingEntity.getBoundingBox().getCenter();
        level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvents, SoundSource.NEUTRAL, 1.0f, 1.0f);
        if (!level.isClientSide) {
            MagicManager.spawnParticles(level, particleTypes, pos.x, pos.y, pos.z, 16, .3, .3, .3, 0.2, false);
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
