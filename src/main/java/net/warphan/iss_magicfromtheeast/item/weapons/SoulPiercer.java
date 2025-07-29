package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.entity.spirit_arrow.SpiritArrow;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class SoulPiercer extends ProjectileWeaponItem {
    public int MAX_MANA_COST = 50;
    public int SPIRIT_POWER_SCALE = 10;

    public SoulPiercer(Properties properties) {
        super(properties);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int use) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(stack, livingEntity) - use;
            if (i < 0) {
                return;
            }

            float f = getPowerForTime(i);
            float damage = (f * (float) (player.getAttributeValue(MFTEAttributeRegistries.SPIRIT_SPELL_POWER) * (SPIRIT_POWER_SCALE + getSpiritPowerScale(stack, livingEntity) - 1)));
            SpiritArrow spiritArrow = new SpiritArrow(level, player, stack);
            spiritArrow.setPos(player.position().add(0, 1.5, 0));
            spiritArrow.shoot(player.getLookAngle());
            spiritArrow.setDamage(damage);
            spiritArrow.setOnWorldTick((int) (f * 40));
            spiritArrow.tick();

            level.addFreshEntity(spiritArrow);

            int manaConsume = MAX_MANA_COST + getManaOnUse(stack, player);
            MagicData playerMana = MagicData.getPlayerMagicData(player);
            if (!player.isCreative()) {
                playerMana.addMana(-(f * manaConsume));
            }
            stack.hurtAndBreak(this.getDurabilityUse(stack), livingEntity, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));

            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), MFTESoundRegistries.SPIRIT_ARROW_SHOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

    }

    public static int getSpiritPowerScale(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level instanceof ServerLevel serverLevel) {
            int powerScalePoint = MFTEEnchantmentHelper.modifyPowerScale(serverLevel, stack, livingEntity, 1);
            return Mth.floor(powerScalePoint);
        } else return 0;
    }

    public static int getManaOnUse(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level instanceof ServerLevel serverLevel) {
            int manaUseAmount = MFTEEnchantmentHelper.processManaUse(serverLevel, stack, -5);
            return Mth.floor(manaUseAmount);
        } else return 0;
    }

    @Override
    protected void shootProjectile(LivingEntity p_331372_, Projectile p_332000_, int p_330631_, float p_331251_, float p_331199_, float p_330857_, @javax.annotation.Nullable LivingEntity p_331572_) {
        p_332000_.shootFromRotation(p_331372_, p_331372_.getXRot(), p_331372_.getYRot() + p_330857_, 0.0F, p_331251_, p_331199_);
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 7200;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        MagicData magicData = MagicData.getPlayerMagicData(player);
        if (magicData.getMana() < (MAX_MANA_COST + getManaOnUse(itemstack, player)) && !player.isCreative()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return 20;
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, components, flag);
        components.add(Component.translatable("item.iss_magicfromtheeast.soulpiercer.description").
                withStyle(ChatFormatting.AQUA)
        );
    }
}
