package net.warphan.iss_magicfromtheeast.events;

import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.warphan.iss_magicfromtheeast.registries.ItemRegistries;
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
            player.setItemInHand(hand, ItemUtils.createFilledResult(useItem, player, new ItemStack(ItemRegistries.BOTTLE_OF_SOULS.get())));
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

    //@SubscribeEvent
}
