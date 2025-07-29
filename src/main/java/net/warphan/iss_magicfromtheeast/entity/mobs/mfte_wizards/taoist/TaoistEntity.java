package net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.taoist;

import com.google.common.collect.Sets;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.entity.mobs.wizards.IMerchantWizard;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.loot.SpellFilter;
import io.redspace.ironsspellbooks.player.AdditionalWanderingTrades;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.warphan.iss_magicfromtheeast.registries.MFTEItemRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESchoolRegistries;
import net.warphan.iss_magicfromtheeast.registries.MFTESpellRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class TaoistEntity extends NeutralWizard implements IMerchantWizard {
    public TaoistEntity(EntityType<? extends AbstractSpellCastingMob> entityType, Level level) {
        super(entityType, level);
        xpReward = 25;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 25, 60)
                .setSpells(
                        List.of(MFTESpellRegistries.DRAGON_GLIDE_SPELL.get(), MFTESpellRegistries.LAUNCH_SPELL.get(), MFTESpellRegistries.SWORD_DANCE_SPELL.get()),
                        List.of(MFTESpellRegistries.BAGUA_ARRAY_CIRCLE_SPELL.get(), MFTESpellRegistries.DRAPES_OF_REFLECTION.get()),
                        List.of(),
                        List.of(MFTESpellRegistries.JADE_JUDGEMENT_SPELL.get())
                )
                .setSingleUseSpell(MFTESpellRegistries.JIANGSHI_INVOKE_SPELL.get(), 80, 280, 4, 6)
                .setDrinksPotions()
        );
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 30, .75f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType reason, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomSource = Utils.random;
        this.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        return super.finalizeSpawn(level, difficultyInstance, reason, spawnGroupData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MFTEItemRegistries.TAOIST_HAT.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(MFTEItemRegistries.TAOIST_ROBES.get()));
        this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, .25);
    }

    @Override
    public Optional<SoundEvent> getAngerSound() {
        return Optional.of(SoundRegistry.TRADER_NO.get());
    }

    //Merchant Stuffs

    @Nullable
    private Player tradingPlayer;
    @Nullable
    protected MerchantOffers offers;

    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean preventTrading = (!this.level.isClientSide && this.getOffers().isEmpty()) || this.getTarget() != null || isAngryAt(player);
        if (hand == InteractionHand.MAIN_HAND) {
            if (preventTrading && !this.level.isClientSide) {
                //me don't want to trade with u
            }
        }
        if (!preventTrading) {
            if (!this.level.isClientSide && !this.getOffers().isEmpty()) {
                if (shouldRestock()) {
                    restock();
                }
                this.startTrading(player);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    private void startTrading(Player player) {
        this.setTradingPlayer(player);
        this.lookControl.setLookAt(player);
        this.openTradingScreen(player, this.getDisplayName(), 0);
    }

    @Override
    public int getRestocksToday() {
        return numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocks) {
        this.numberOfRestocksToday = restocks;
    }

    @Override
    public long getLastRestockGameTime() {
        return lastRestockGameTime;
    }

    @Override
    public void setLastRestockGameTime(long time) {
        this.lastRestockGameTime = time;
    }

    @Override
    public long getLastRestockCheckDayTime() {
        return lastRestockCheckDayTime;
    }

    @Override
    public void setLastRestockCheckDayTime(long time) {
        this.lastRestockCheckDayTime = time;
    }

    @Override
    public Level level() {
        return this.level;
    }

    @Override
    public void setTradingPlayer(@org.jetbrains.annotations.Nullable Player pTradingPlayer) {
        this.tradingPlayer = pTradingPlayer;
    }

    @Override
    public Player getTradingPlayer() {
        return tradingPlayer;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();

            this.offers.addAll(createRandomOffers(2, 3));

            if (this.random.nextFloat() < 0.25f) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_COMMON.get()).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_UNCOMMON.get()).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < 0.25f) {
                this.offers.add(new AdditionalWanderingTrades.InkBuyTrade((InkItem) ItemRegistry.INK_RARE.get()).getOffer(this, this.random));
            }

            this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(MFTESchoolRegistries.SYMMETRY.get()), 0f, .25f).getOffer(this, this.random));
            if (this.random.nextFloat() < .8f) {
                this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(MFTESchoolRegistries.SYMMETRY.get()), .3f, .7f).getOffer(this, this.random));
            }
            if (this.random.nextFloat() < .8f) {
                this.offers.add(new AdditionalWanderingTrades.RandomScrollTrade(new SpellFilter(MFTESchoolRegistries.SYMMETRY.get()), .8f, 1f).getOffer(this, this.random));
            }
            this.offers.add(new AdditionalWanderingTrades.SimpleSell(1, new ItemStack(MFTEItemRegistries.BAGUA_MIRROR.get()), 56, 64).getOffer(this, this.random));
            this.offers.removeIf(Objects::isNull);
            numberOfRestocksToday++;
        }
        return this.offers;
    }

    private static final List<VillagerTrades.ItemListing> fillerOffers = List.of(
            new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost(MFTEItemRegistries.RAW_JADE.get(), 5), 1, 3),
            new AdditionalWanderingTrades.SimpleSell(6, new ItemStack(MFTEItemRegistries.RED_STRING, 1), 8, 12),
            new AdditionalWanderingTrades.SimpleSell(8, new ItemStack(Items.FLINT, 3), 4, 7),
            new AdditionalWanderingTrades.SimpleSell(8, new ItemStack(Items.QUARTZ, 3), 6, 10),
            new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost(MFTEItemRegistries.COPPER_COINS.get(), 16), 2, 4),
            new AdditionalWanderingTrades.SimpleBuy(4, new ItemCost(MFTEItemRegistries.REFINED_JADE_INGOT.get(), 1), 6, 12),
            new AdditionalWanderingTrades.SimpleBuy(16, new ItemCost(MFTEItemRegistries.YIN_YANG_CORE.get(), 1), 4, 6)
    );

    private Collection<MerchantOffer> createRandomOffers(int min, int max) {
        Set<Integer> set = Sets.newHashSet();
        int fillerTrades = random.nextIntBetweenInclusive(min, max);
        for (int i = 0; i < 10 && set.size() < fillerTrades; i++) {
            set.add(random.nextInt(fillerOffers.size()));
        }
        Collection<MerchantOffer> offers = new ArrayList<>();
        for (Integer integer : set) {
            offers.add(fillerOffers.get(integer).getOffer(this, this.random));
        }
        return offers;
    }

    @Override
    public void overrideOffers(MerchantOffers pOffers) {

    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || isTrading();
    }

    @Override
    public void notifyTrade(MerchantOffer pOffer) {
        pOffer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
    }

    @Override
    public void notifyTradeUpdated(ItemStack pStack) {
        if (!this.level.isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!pStack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    protected SoundEvent getTradeUpdatedSound(boolean pIsYesSound) {
        return pIsYesSound ? SoundRegistry.TRADER_YES.get() : SoundRegistry.TRADER_NO.get();
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundRegistry.TRADER_YES.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        serializeMerchant(pCompound, this.offers, this.lastRestockGameTime, this.numberOfRestocksToday);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        deserializeMerchant(pCompound, c -> this.offers = c);
    }
}
