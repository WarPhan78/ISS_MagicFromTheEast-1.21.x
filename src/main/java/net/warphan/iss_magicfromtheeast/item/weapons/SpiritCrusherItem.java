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
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiritCrusherItem extends ExtendedSwordItem {
    private static final int BASE_PERCENT_FROM_TARGET_HEALTH = 2;

    public SpiritCrusherItem(MFTEExtendedWeaponTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float health = target.getHealth();
        float bonusDamage = health / 100 * (BASE_PERCENT_FROM_TARGET_HEALTH + (getBonusPercent(stack, attacker) * 2));
        if (MFTEServerConfigs.ALLOW_SPIRIT_CRUSHER_DAMAGE_CAP.get()) {
            double maxDamage = MFTEServerConfigs.SPIRIT_CRUSHER_DAMAGE_CAP.get();
            if (bonusDamage > maxDamage) {
                target.hurt(attacker.damageSources().source(MFTEDamageTypes.SOUL_WEAPONS_BONUS), (float) maxDamage);
            } else
                target.hurt(attacker.damageSources().source(MFTEDamageTypes.SOUL_WEAPONS_BONUS), bonusDamage);
        } else {
            target.hurt(attacker.damageSources().source(MFTEDamageTypes.SOUL_WEAPONS_BONUS), bonusDamage);
        }
        // PORT 1.20.1: Item#postHurtEnemy does not exist - its logic is merged here
        // (durability damage is handled by super.hurtEnemy / SwordItem).
        if (target.getHealth() < target.getMaxHealth() * 0.3f) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    //This is for serverside, make the weapon be able to deal extra soul damage
    public static int getBonusPercent(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            int bonusPercent = MFTEEnchantmentHelper.modifySoulDamage(serverLevel, stack, livingEntity, 0);
            return Mth.floor(bonusPercent);
        } else return 0;
    }

    //This is for clientside, make the tooltip show correctly how much percent of bonus damage can be dealt
    public static double getDisplaySoulDamage(ItemStack stack, Entity entity) {
        double basePercent = BASE_PERCENT_FROM_TARGET_HEALTH;
        // PORT 1.20.1: enchantment effect components do not exist - inner impact grants +1 soul
        // damage per level (see MFTEEnchantments), read straight from the enchantment level.
        if (!stack.isEmpty() && stack.isEnchanted() && entity != null) {
            basePercent = basePercent + (EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.INNER_IMPACT.get(), stack) * 2);
        } return basePercent;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description",
                Component.literal(Utils.stringTruncation(getDisplaySoulDamage(stack, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.YELLOW))
        ).withStyle(ChatFormatting.AQUA));
    }
}
