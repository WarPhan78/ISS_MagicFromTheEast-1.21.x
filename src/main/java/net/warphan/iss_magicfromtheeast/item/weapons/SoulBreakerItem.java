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
import net.warphan.iss_magicfromtheeast.damage.MFTEDamageTypes;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;

import java.util.List;

public class SoulBreakerItem extends ExtendedSwordItem {
    public static final float BASE_SOUL_DAMAGE = 3;

    public SoulBreakerItem(MFTEExtendedWeaponTier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.hurt(attacker.damageSources().source(MFTEDamageTypes.SOUL_WEAPONS_BONUS), BASE_SOUL_DAMAGE + getBonusSoulDamage(stack, attacker));
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        if (target.getHealth() < target.getMaxHealth() * 0.6f) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
        }
    }

    //This is for serverside, make the weapon be able to deal extra soul damage
    public static int getBonusSoulDamage(ItemStack stack, LivingEntity livingEntity) {
        if (livingEntity.level instanceof ServerLevel serverLevel) {
            int bonusSoulDamage = MFTEEnchantmentHelper.modifySoulDamage(serverLevel, stack, livingEntity, 0);
            return Mth.floor(bonusSoulDamage);
        } else return 0;
    }

    //This is for clientside, make the tooltip show correctly how much extra soul damage you can deal
    public static double getDisplaySoulDamage(ItemStack stack, Entity entity) {
        double baseDamage = BASE_SOUL_DAMAGE;
        if (!stack.isEmpty() && stack.has(DataComponents.ENCHANTMENTS) && entity != null) {
                baseDamage = baseDamage + Utils.processEnchantment(entity.level, MFTEEnchantments.INNER_IMPACT, MFTEDataComponentRegistries.SOUL_DAMAGE.get(), stack.get(DataComponents.ENCHANTMENTS));
        } return baseDamage;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal(" ").append(Component.translatable(this.getDescriptionId() + ".description",
                Component.literal(Utils.stringTruncation(getDisplaySoulDamage(stack, MinecraftInstanceHelper.getPlayer()), 1)).withStyle(ChatFormatting.YELLOW))
        ).withStyle(ChatFormatting.AQUA));
    }
}
