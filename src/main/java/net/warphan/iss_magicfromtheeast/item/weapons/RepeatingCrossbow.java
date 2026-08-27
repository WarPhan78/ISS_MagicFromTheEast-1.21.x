package net.warphan.iss_magicfromtheeast.item.weapons;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantments;
import net.warphan.iss_magicfromtheeast.item.LoadableWeaponContents;
import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
import net.warphan.iss_magicfromtheeast.setup.ClientLoadableWeaponTooltip;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * PORT 1.20.1:
 * <ul>
 *   <li>The 1.21 data components (ProjectileAmountComponent/ChargeStateComponent/
 *       LoadingStateComponent/LoadableWeaponContents) became plain NBT on the ItemStack -
 *       see {@link MFTEDataComponentRegistries} and {@link LoadableWeaponContents}.</li>
 *   <li>ProjectileWeaponItem has no shoot/createProjectile/shootProjectile infrastructure on
 *       1.20.1 - a local implementation mirroring the 1.21 logic (and the vanilla 1.20.1
 *       CrossbowItem arrow creation) is used instead.</li>
 *   <li>Enchantment effect components (spread/projectile count/charging sounds/ammo use) are
 *       replaced by their 1.20.1 equivalents: MULTISHOT, QUICK_CHARGE, PIERCING levels and the
 *       MFTE "expanding" enchantment level (+1 ammo per level, see MFTEEnchantments).</li>
 * </ul>
 */
public class RepeatingCrossbow extends ProjectileWeaponItem {
    private static final int DEFAULT_PROJECTILE_AMOUNT = 5;
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    int loadingTick = 1;

    public RepeatingCrossbow(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
        ItemStack itemstack = player.getItemInHand(pHand);
        if (itemstack == player.getMainHandItem()) {
            if (getAmmoAmount(itemstack) > 0 && isCharged(itemstack)) {
                this.performShooting(pLevel, player, pHand, itemstack, 3f, 1.0f, null);
                return InteractionResultHolder.consume(itemstack);
            } else if (!player.getProjectile(itemstack).isEmpty() && getAmmoAmount(itemstack) <= 0) {
                player.startUsingItem(pHand);
                if (isCharged(itemstack)) {
                    startLoadingAmmo(itemstack);
                }
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack crossbow, int i) {
        int PROJECTILE_AMOUNT = getBonusProjectileAmount(crossbow, livingEntity);
        var projectile = livingEntity.getProjectile(crossbow);
        if (!level.isClientSide) {
            if (isCharged(crossbow) && isLoading(crossbow)) {
                loadingTick++;
                if (getAmmoAmount(crossbow) < PROJECTILE_AMOUNT && !projectile.isEmpty()) {
                    if (loadingTick % 10 == 0) {
                        setAmmoAmount(crossbow, getAmmoAmount(crossbow) + 1);
                        LoadableWeaponContents contents = LoadableWeaponContents.get(crossbow);
                        if (contents != null) {
                            LoadableWeaponContents.Mutable mutable = new LoadableWeaponContents.Mutable(contents);
                            mutable.tryInsert(projectile);
                            LoadableWeaponContents.set(crossbow, mutable.toImmutable());
                        }
                        //here the method load projectile in

                        level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), MFTESoundRegistries.PROJECTILE_LOAD.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                } else if (getAmmoAmount(crossbow) == PROJECTILE_AMOUNT || livingEntity.getProjectile(crossbow).isEmpty()) {
                    if (livingEntity instanceof Player player) {
                        stopLoadingAmmo(player, crossbow);
                    }
                }
            }
            //playing charging sounds
            playingSound(level, livingEntity, crossbow, i);
        }
    }

    public void performShooting(Level level, LivingEntity livingEntity, InteractionHand hand, ItemStack crossbow, float f1, float f2, @Nullable LivingEntity nullableEntity) {
        if (level instanceof ServerLevel serverlevel) {
            if (livingEntity instanceof Player player) {
                if (ForgeEventFactory.onArrowLoose(crossbow, livingEntity.level(), player, 1, true) < 0) {
                    return;
                }
            }

            LoadableWeaponContents contents = LoadableWeaponContents.get(crossbow);
            LoadableWeaponContents.set(crossbow, LoadableWeaponContents.EMPTY);

            if (getAmmoAmount(crossbow) > 0 && contents != null) {

                LoadableWeaponContents.Mutable mutable = new LoadableWeaponContents.Mutable(contents);
                // PORT 1.20.1: EnchantmentHelper.processProjectileSpread -> multishot check (vanilla +-10 degrees).
                float spread = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow) > 0 ? 10.0F : 0.0F;

                List<ItemStack> itemStacks = mutable.getItems();

                List<ItemStack> projectileStack = new ArrayList<>(1);
                if (!itemStacks.isEmpty()) {
                    projectileStack.add(itemStacks.get(0));
                }

                if (spread > 0) {
                    this.shoot(serverlevel, livingEntity, hand, crossbow, itemStacks, spread, f1, f2, livingEntity instanceof Player, nullableEntity);
                } else
                    this.shoot(serverlevel, livingEntity, hand, crossbow, projectileStack, spread, f1, f2, livingEntity instanceof Player, nullableEntity);
                mutable.removeOne();

                LoadableWeaponContents.set(crossbow, mutable.toImmutable());

                if (getAmmoAmount(crossbow) <= 1) {
                    LoadableWeaponContents.remove(crossbow);
                    MFTEDataComponentRegistries.remove(crossbow, MFTEDataComponentRegistries.CROSSBOW_CHARGE_STATE);
                }

                if (spread > 0) {
                    setAmmoAmount(crossbow, 0);
                    LoadableWeaponContents.remove(crossbow);
                    MFTEDataComponentRegistries.remove(crossbow, MFTEDataComponentRegistries.CROSSBOW_CHARGE_STATE);
                } else
                    setAmmoAmount(crossbow, getAmmoAmount(crossbow) - 1);

                if (livingEntity instanceof ServerPlayer serverplayer) {
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, crossbow);
                    serverplayer.awardStat(Stats.ITEM_USED.get(crossbow.getItem()));
                }
            }
        }
    }

    /**
     * Local replacement for the 1.21 ProjectileWeaponItem#shoot (same spread math).
     */
    protected void shoot(ServerLevel serverLevel, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float spread, float velocity, float inaccuracy, boolean isPlayer, @Nullable LivingEntity target) {
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * spread / (float) (projectileItems.size() - 1);
        float f2 = (float) ((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for (int i = 0; i < projectileItems.size(); i++) {
            ItemStack itemstack = projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float) ((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(serverLevel, shooter, weapon, itemstack, isPlayer);
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                serverLevel.addFreshEntity(projectile);
            }
        }
    }

    /**
     * Local replacement for the 1.21 ProjectileWeaponItem#createProjectile (mirrors the vanilla
     * 1.20.1 CrossbowItem arrow creation).
     */
    protected Projectile createProjectile(Level level, LivingEntity livingEntity, ItemStack weapon, ItemStack ammo, boolean isPlayer) {
        ArrowItem arrowitem = (ArrowItem) (ammo.getItem() instanceof ArrowItem item ? item : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, livingEntity);
        if (isPlayer) {
            abstractarrow.setCritArrow(true);
        }
        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractarrow.setShotFromCrossbow(true);
        int pierceLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, weapon);
        if (pierceLevel > 0) {
            abstractarrow.setPierceLevel((byte) pierceLevel);
        }
        return abstractarrow;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int tick) {
        int i = this.getUseDuration(itemStack) - tick;
        float f = getPowerForTime(i, itemStack);
        if (f >= 1.0F && !isCharged(itemStack) && tryChargeCrossbowUp(livingEntity, itemStack)) {
            level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END, livingEntity.getSoundSource(), 1.0F, 1.0F / (livingEntity.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    public static boolean tryChargeCrossbowUp(LivingEntity livingEntity, ItemStack stack) {
        List<ItemStack> list = noProjectileTakenDraw(stack, livingEntity.getProjectile(stack), livingEntity);
        if (!list.isEmpty()) {
            MFTEDataComponentRegistries.setBoolean(stack, MFTEDataComponentRegistries.CROSSBOW_CHARGE_STATE, true);
            LoadableWeaponContents.set(stack, LoadableWeaponContents.EMPTY);
            return true;
        } else {
            return false;
        }
    }

    protected static List<ItemStack> noProjectileTakenDraw(ItemStack p_331565_, ItemStack stack, LivingEntity livingEntity) {
        if (stack.isEmpty()) {
            return List.of();
        } else {
            List<ItemStack> list = new ArrayList<>(1);
            ItemStack itemstack1 = stack.copy();

            list.add(itemstack1);

            return list;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        return MFTEDataComponentRegistries.getBoolean(stack, MFTEDataComponentRegistries.CROSSBOW_CHARGE_STATE, false);
    }

    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float f1, float f2, float f3, @Nullable LivingEntity nullableEntity) {
        Vector3f vector3f;
        if (nullableEntity != null) {
            double d0 = nullableEntity.getX() - livingEntity.getX();
            double d1 = nullableEntity.getZ() - livingEntity.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = nullableEntity.getY(0.3333333333333333) - projectile.getY() + d2 * 0.20000000298023224;
            vector3f = getProjectileShotVector(livingEntity, new Vec3(d0, d3, d1), f3);
        } else {
            Vec3 vec3 = livingEntity.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double) (f3 * 0.017453292F), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = livingEntity.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        projectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), f1, f2);
        float f = getShotPitch(livingEntity.getRandom(), i);
        livingEntity.level().playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_SHOOT, livingEntity.getSoundSource(), 1.0F, f);
    }

    private static Vector3f getProjectileShotVector(LivingEntity livingEntity, Vec3 vec, float f) {
        Vector3f vector3f = vec.toVector3f().normalize();
        Vector3f vector3f1 = (new Vector3f(vector3f)).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double) vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = livingEntity.getUpVector(1.0F);
            vector3f1 = (new Vector3f(vector3f)).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = (new Vector3f(vector3f)).rotateAxis(1.5707964F, vector3f1.x, vector3f1.y, vector3f1.z);
        return (new Vector3f(vector3f)).rotateAxis(f * 0.017453292F, vector3f2.x, vector3f2.y, vector3f2.z);
    }

    private static float getShotPitch(RandomSource randomSource, int i) {
        return i == 0 ? 1.0F : getRandomShotPitch((i & 1) == 1, randomSource);
    }

    private static float getRandomShotPitch(boolean var1000, RandomSource randomSource) {
        float f = var1000 ? 0.63F : 0.43F;
        return 1.0F / (randomSource.nextFloat() * 0.5F + 1.8F) + f;
    }

    public static int getBonusProjectileAmount(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity.level() instanceof ServerLevel serverLevel) {
            int bonusProjectileAmount = MFTEEnchantmentHelper.increaseAmmoLoad(serverLevel, itemStack, 0) + DEFAULT_PROJECTILE_AMOUNT;
            return Mth.floor(bonusProjectileAmount);
        } else return 0;
    }

    //Client display
    public static double getDisplayProjectile(ItemStack stack, Entity entity) {
        double baseAmmoAmount = DEFAULT_PROJECTILE_AMOUNT;
        // PORT 1.20.1: enchantment effect components do not exist - "expanding" grants +1 loadable
        // ammo per level (see MFTEEnchantments), read straight from the enchantment level.
        if (!stack.isEmpty() && stack.isEnchanted() && entity != null) {
            baseAmmoAmount = baseAmmoAmount + EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.EXPANDING.get(), stack);
        } return baseAmmoAmount;
    }

    public static int getLoadDuration(ItemStack stack, LivingEntity livingEntity) {
        return getBonusProjectileAmount(stack, livingEntity) * 12;
    }

    /** Client-safe load duration (enchantment level based, no ServerLevel required). */
    public static int getLoadDuration(ItemStack stack) {
        int amount = DEFAULT_PROJECTILE_AMOUNT + EnchantmentHelper.getItemEnchantmentLevel(MFTEEnchantments.EXPANDING.get(), stack);
        return amount * 12;
    }

    /**
     * PORT 1.20.1: EnchantmentHelper.modifyCrossbowChargingTime -> vanilla 1.20.1 quick charge
     * formula (1.25s base, -0.25s per level).
     */
    public static int getChargeDuration(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    public static int getUsingDuration(ItemStack stack, LivingEntity livingEntity) {
        return getUsingDuration(stack);
    }

    public static int getUsingDuration(ItemStack stack) {
        if (!isCharged(stack)) {
            return getChargeDuration(stack);
        } else return getLoadDuration(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return getUsingDuration(stack) + 3;
    }

    private static float getPowerForTime(int i, ItemStack itemStack) {
        float f = (float) i / (float) getChargeDuration(itemStack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    //a copy from crossbow onUseTick method (1.20.1 quick-charge based sounds)
    public void playingSound(Level level, LivingEntity livingEntity, ItemStack stack, int i) {
        if (!isCharged(stack)) {
            int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent startSound = getStartSound(quickCharge);
            SoundEvent midSound = quickCharge < 1 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (stack.getUseDuration() - i) / (float) getChargeDuration(stack);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), startSound, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && midSound != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), midSound, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    /** Mirrors vanilla 1.20.1 CrossbowItem#getStartSound. */
    private static SoundEvent getStartSound(int quickChargeLevel) {
        return switch (quickChargeLevel) {
            case 1 -> SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2 -> SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3 -> SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default -> SoundEvents.CROSSBOW_LOADING_START;
        };
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return Optional.ofNullable(LoadableWeaponContents.get(itemStack)).map(ClientLoadableWeaponTooltip.LoadableWeaponTooltipComponent::new);
    }

    public static void startLoadingAmmo(ItemStack crossbow) {
        setLoadingAmmo(crossbow, true);
    }

    public static void stopLoadingAmmo(Player player, ItemStack crossbow) {
        setLoadingAmmo(crossbow, false);
    }

    public static boolean isLoading(ItemStack crossbow) {
        return MFTEDataComponentRegistries.getBoolean(crossbow, MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, false);
    }

    public static void setLoadingAmmo(ItemStack crossbow, boolean isLoading) {
        MFTEDataComponentRegistries.setBoolean(crossbow, MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, isLoading);
    }

    public static int getAmmoAmount(ItemStack crossbow) {
        return MFTEDataComponentRegistries.getInt(crossbow, MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT, 0);
    }

    public static void setAmmoAmount(ItemStack crossbow, int amount) {
        MFTEDataComponentRegistries.setInt(crossbow, MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT, amount);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        int ammoAmount = getAmmoAmount(pStack);
        pTooltip.add(Component.translatable("item.iss_magicfromtheeast.repeating_crossbow.ammo_amount").append(CommonComponents.SPACE).append(String.valueOf(ammoAmount)).append(" / ").append(
                Component.literal(Utils.stringTruncation(getDisplayProjectile(pStack, MinecraftInstanceHelper.getPlayer()), 1))));
        TooltipsUtils.addShiftTooltip(pTooltip, List.of(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.YELLOW)));
    }
}
