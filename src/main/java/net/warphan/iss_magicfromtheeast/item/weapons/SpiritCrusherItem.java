package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.warphan.iss_magicfromtheeast.configs.MFTEServerConfigs;
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;

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
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        if (target.getHealth() < target.getMaxHealth() * 0.3f) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
        }
    }

    //This is for serverside, make the weapon be able to deal extra soul damage
    public static int getBonusPercent(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level instanceof ServerLevel serverLevel) {
            int bonusPercent = MFTEEnchantmentHelper.modifySoulDamage(serverLevel, stack, livingEntity, 0);
            return Mth.floor(bonusPercent);
        } else return 0;
    }

    //This is for clientside, make the tooltip show correctly how much percent of bonus damage can be dealt
    public static double getDisplaySoulDamage(ItemStack stack, Entity entity) {
        double basePercent = BASE_PERCENT_FROM_TARGET_HEALTH;
        if (!stack.isEmpty() && stack.has(DataComponents.ENCHANTMENTS) && entity != null) {
            basePercent = basePercent + (Utils.processEnchantment(entity.level, MFTEEnchantments.INNER_IMPACT, MFTEDataComponentRegistries.SOUL_DAMAGE.get(), stack.get(DataComponents.ENCHANTMENTS)) * 2);
        } return basePercent;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description",
                Component.literal(Utils.stringTruncation(getDisplaySoulDamage(stack, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.YELLOW))
        ).withStyle(ChatFormatting.AQUA));
    }
}
