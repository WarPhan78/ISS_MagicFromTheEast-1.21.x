//package net.warphan.iss_magicfromtheeast.item.weapons;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.minecraft.advancements.CriteriaTriggers;
//import net.minecraft.core.component.DataComponents;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.network.chat.CommonComponents;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.stats.Stats;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.CrossbowItem;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.component.ChargedProjectiles;
//import net.minecraft.world.level.Level;
//import net.neoforged.neoforge.event.EventHooks;
//import net.warphan.iss_magicfromtheeast.enchantment.MFTEEnchantmentHelper;
//import net.warphan.iss_magicfromtheeast.registries.MFTEDataComponentRegistries;
//import net.warphan.iss_magicfromtheeast.registries.MFTESoundRegistries;
//
//import javax.annotation.Nullable;
//import java.util.List;
//import java.util.function.Predicate;
//
//public class RepeatingCrossbowBackup extends CrossbowItem {
//    private static final int MAX_PROJECTILE_AMOUNT = 4;
//    public static final String AMMO_AMOUNT = "ammo_amount";
//    public static final String IS_LOADING = "is_loading";
//    public static final String LOADING_TICK = "loading_tick";
//    int loadingTick = 1;
//
//    public RepeatingCrossbowBackup(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    public Predicate<ItemStack> getSupportedHeldProjectiles() {
//        return ARROW_ONLY;
//    }
//
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
//        ItemStack itemstack = player.getItemInHand(pHand);
//        if (getAmmoAmount(itemstack) > 0) {
//            this.performShooting(pLevel, player, pHand, itemstack, 3f, 1.0f, null);
//            return InteractionResultHolder.consume(itemstack);
//        } else if (!player.getProjectile(itemstack).isEmpty()) {
//            player.startUsingItem(pHand);
//            if (isCharged(itemstack)) {
//                startLoadingAmmo(player, itemstack);
//            }
//            return InteractionResultHolder.consume(itemstack);
//        } else {
//            return InteractionResultHolder.fail(itemstack);
//        }
//    }
//
//    public static void startLoadingAmmo(Player player, ItemStack crossbow) {
//        setLoadingAmmo(crossbow, true);
//        setLoadingTick(crossbow, (MAX_PROJECTILE_AMOUNT + getMaxProjectileAmount(crossbow, player)) * 15);
//    }
//
//    public static void stopLoadingAmmo(Player player, ItemStack crossbow) {
//        setLoadingAmmo(crossbow, false);
//        setLoadingTick(crossbow, 0);
//    }
//
//    @Override
//    public void performShooting(Level p_40888_, LivingEntity livingEntity, InteractionHand p_40890_, ItemStack crossbow, float p_40892_, float p_40893_, @Nullable LivingEntity p_331602_) {
//        if (p_40888_ instanceof ServerLevel serverlevel) {
//            if (livingEntity instanceof Player player) {
//                if (EventHooks.onArrowLoose(crossbow, livingEntity.level(), player, 1, true) < 0) {
//                    return;
//                }
//            }
//
//            ChargedProjectiles chargedprojectiles = (ChargedProjectiles)crossbow.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
//            if (getAmmoAmount(crossbow) > 0) {
//                this.shoot(serverlevel, livingEntity, p_40890_, crossbow, chargedprojectiles.getItems(), p_40892_, p_40893_, livingEntity instanceof Player, p_331602_);
//                setAmmoAmount(crossbow, getAmmoAmount(crossbow) - 1);
//                if (getAmmoAmount(crossbow) > 0) {
//                    List<ItemStack> list = draw(crossbow, livingEntity.getProjectile(crossbow), livingEntity);
//                    if (!list.isEmpty()) {
//                        crossbow.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
//                    }
//                } else
//                    setLoadingAmmo(crossbow, false);
//                if (livingEntity instanceof ServerPlayer) {
//                    ServerPlayer serverplayer = (ServerPlayer)livingEntity;
//                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, crossbow);
//                    serverplayer.awardStat(Stats.ITEM_USED.get(crossbow.getItem()));
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
//        int f = getChargeDuration(stack, livingEntity);
//        if (isCharged(stack)) {
//            return f + getLoadingTick(stack);
//        } else return f;
//    }
//
//    @Override
//    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack crossbow, int i) {
//        int PROJECTILE_AMOUNT = MAX_PROJECTILE_AMOUNT + getMaxProjectileAmount(crossbow, livingEntity);
//        if (!level.isClientSide) {
//            if (isLoading(crossbow)) {
//                loadingTick++;
//                if (getAmmoAmount(crossbow) < PROJECTILE_AMOUNT && !livingEntity.getProjectile(crossbow).isEmpty() && isCharged(crossbow)) {
//                    if (loadingTick % 5 == 0) {
//                        setAmmoAmount(crossbow, getAmmoAmount(crossbow) + 1);
//                        level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), MFTESoundRegistries.PROJECTILE_LOAD, SoundSource.PLAYERS, 1.0f, 1.0f);
//                        tryLoadProjectiles(livingEntity, crossbow);
//                    }
//                } else if (loadingTick == getLoadingTick(crossbow) || getAmmoAmount(crossbow) == PROJECTILE_AMOUNT || livingEntity.getProjectile(crossbow).isEmpty()) {
//                    if (livingEntity instanceof Player player) {
//                        stopLoadingAmmo(player, crossbow);
//                    }
//                }
//            }
//        }
//    }
//
//    public static int getMaxProjectileAmount(ItemStack stack, LivingEntity livingEntity) {
//        if (livingEntity.level instanceof ServerLevel serverLevel) {
//            int getExpanding = MFTEEnchantmentHelper.increaseAmmoLoad(serverLevel, stack, 1);
//            return Mth.floor(getExpanding);
//        } else return 0;
//    }
//
//    public static int getChargeDuration(ItemStack crossbow, LivingEntity livingEntity) {
//        return (livingEntity == null ? 25 : CrossbowItem.getChargeDuration(crossbow, livingEntity)) * 3;
//    }
//
//    public static boolean isLoading(ItemStack crossbow) {
//        return crossbow.has(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE) && crossbow.get(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE).isLoading();
//    }
//
//    public static void setLoadingAmmo(ItemStack crossbow, boolean isLoading) {
//        crossbow.set(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, crossbow.getOrDefault(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, new LoadingStateComponent(false, 0)).setLoadingAmmo(isLoading));
//    }
//
//    public static int getLoadingTick(ItemStack crossbow) {
//        return crossbow.has(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE) ? crossbow.get(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE).loadingTick() : 0;
//    }
//
//    public static void setLoadingTick(ItemStack crossbow, int loadingTick) {
//        crossbow.set(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, crossbow.getOrDefault(MFTEDataComponentRegistries.CROSSBOW_LOADING_STATE, new LoadingStateComponent(false, 0)).setLoadingTick(loadingTick));
//    }
//
//    public static int getAmmoAmount(ItemStack crossbow) {
//        return crossbow.has(MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT) ? crossbow.get(MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT).ammoAmount() : 0;
//    }
//
//    public static void setAmmoAmount(ItemStack crossbow, int amount) {
//        crossbow.set(MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT, crossbow.getOrDefault(MFTEDataComponentRegistries.CROSSBOW_AMMO_AMOUNT, new ProjectileAmountComponent(0)).setAmmoAmount(amount));
//    }
//
//    @Override
//    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
//        int ammoAmount = getAmmoAmount(pStack);
//        pTooltip.add(Component.translatable("item.iss_magicfromtheeast.repeating_crossbow.ammo_amount").append(CommonComponents.SPACE).append(String.valueOf(ammoAmount)));
//    }
//
//    public record ProjectileAmountComponent(int ammoAmount) {
//        public static final Codec<ProjectileAmountComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
//                Codec.INT.optionalFieldOf(AMMO_AMOUNT, 0).forGetter(ProjectileAmountComponent::ammoAmount)
//        ).apply(builder, ProjectileAmountComponent::new));
//
//        public static final StreamCodec<FriendlyByteBuf, ProjectileAmountComponent> STREAM_CODEC = StreamCodec.of((buf, data) -> {
//            buf.writeInt(data.ammoAmount);
//        }, (buf) -> new ProjectileAmountComponent(buf.readInt()));
//
//        public ProjectileAmountComponent setAmmoAmount(int ammoAmount) {
//            return new ProjectileAmountComponent(ammoAmount);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj == this || (obj instanceof ProjectileAmountComponent projectileAmountComponent && projectileAmountComponent.ammoAmount == this.ammoAmount);
//        }
//    }
//
//    public record LoadingStateComponent(boolean isLoading, int loadingTick) {
//        public static final Codec<LoadingStateComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
//                Codec.BOOL.optionalFieldOf(IS_LOADING, false).forGetter(LoadingStateComponent::isLoading),
//                Codec.INT.optionalFieldOf(LOADING_TICK, 0).forGetter(LoadingStateComponent::loadingTick)
//        ).apply(builder, LoadingStateComponent::new));
//
//        public static final StreamCodec<FriendlyByteBuf, LoadingStateComponent> STREAM_CODEC = StreamCodec.of((buf, data) -> {
//            buf.writeBoolean(data.isLoading);
//            buf.writeInt(data.loadingTick);
//        }, (buf) -> new LoadingStateComponent(buf.readBoolean(), buf.readInt()));
//
//        public LoadingStateComponent setLoadingAmmo(boolean isLoading) {
//            return new LoadingStateComponent(isLoading, this.loadingTick);
//        }
//
//        public LoadingStateComponent setLoadingTick(int loadingTick) {
//            return new LoadingStateComponent(this.isLoading, loadingTick);
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj == this || (obj instanceof LoadingStateComponent loadingStateComponent && loadingStateComponent.isLoading == this.isLoading && loadingStateComponent.loadingTick == this.loadingTick);
//        }
//    }
//}
