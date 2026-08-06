package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulBreakerItem extends ExtendedSwordItem {
    public static final float BASE_SOUL_DAMAGE = 3;

    public SoulBreakerItem(MFTEExtendedWeaponTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.hurt(attacker.damageSources().source(MFTEDamageTypes.SOUL_WEAPONS_BONUS), BASE_SOUL_DAMAGE + getBonusSoulDamage(stack, attacker));
        // PORT 1.20.1: Item#postHurtEnemy does not exist - its logic is merged here
        // (durability damage is handled by super.hurtEnemy / SwordItem).
        if (target.getHealth() < target.getMaxHealth() * 0.6f) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    //This is for serverside, make the weapon be able to deal extra soul damage
    public static int getBonusSoulDamage(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            int bonusSoulDamage = MFTEEnchantmentHelper.modifySoulDamage(serverLevel, stack, livingEntity, 0);
            return Mth.floor(bonusSoulDamage);
        } else return 0;
    }

    //This is for clientside, make the tooltip show correctly how much extra soul damage you can deal
    public static double getDisplaySoulDamage(ItemStack stack, Entity entity) {
        double baseDamage = BASE_SOUL_DAMAGE;
        // PORT 1.20.1: enchantment effect components do not exist - inner impact grants +1 soul
        // damage per level (see MFTEEnchantments), read straight from the enchantment level.
        if (!stack.isEmpty() && stack.isEnchanted() && entity != null) {
            baseDamage = baseDamage + EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.INNER_IMPACT.get(), stack);
        } return baseDamage;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description",
                Component.literal(Utils.stringTruncation(getDisplaySoulDamage(stack, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.YELLOW))
        ).withStyle(ChatFormatting.AQUA));
    }
}
