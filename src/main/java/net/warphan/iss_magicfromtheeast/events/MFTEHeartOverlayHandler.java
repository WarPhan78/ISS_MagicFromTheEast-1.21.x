package net.warphan.iss_magicfromtheeast.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.registries.MFTEEffectRegistries;
import net.warphan.iss_magicfromtheeast.setup.MFTEHeartTypes;

/**
 * PORT 1.20.1: replaces the 1.21 PlayerHeartTypeEvent + Gui.HeartType enum extension.
 * Forge 1.20.1 has neither, so we cancel the vanilla PLAYER_HEALTH overlay and redraw
 * the heart bar ourselves (same approach as Overflowing Bars) whenever one of the
 * custom-heart effects is active. When no effect is active we do nothing, so vanilla
 * (or other HUD mods) render normally.
 *
 * Jade hearts  -> Reversal Healing (Bagua Array)
 * Soul hearts  -> Soulburn
 */
@Mod.EventBusSubscriber(modid = ISS_MagicFromTheEast.MOD_ID, value = Dist.CLIENT)
public class MFTEHeartOverlayHandler {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private static final RandomSource RANDOM = RandomSource.create();
    private static int tickCount;
    private static int lastHealth;
    private static int displayHealth;
    private static long lastHealthTime;
    private static long healthBlinkTime;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCount++;
        }
    }

    // HIGH priority so we take over before other HUD mods replace the health bar
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.gui instanceof ForgeGui gui)) {
            return;
        }
        if (minecraft.options.hideGui || !gui.shouldDrawSurvivalElements()) {
            return;
        }
        if (!(minecraft.getCameraEntity() instanceof Player player)) {
            return;
        }

        String heartSet;
        if (player.hasEffect(MFTEEffectRegistries.REVERSAL_HEALING.get())) {
            heartSet = "jade";
        } else if (player.hasEffect(MFTEEffectRegistries.SOULBURN.get())) {
            heartSet = "soul";
        } else {
            return; // no custom hearts -> let vanilla/other mods render
        }

        event.setCanceled(true);
        minecraft.getProfiler().push("mfte_health");
        RenderSystem.enableBlend();

        int currentHealth = Mth.ceil(player.getHealth());
        boolean blink = healthBlinkTime > (long) tickCount && (healthBlinkTime - (long) tickCount) / 3L % 2L == 1L;
        long millis = net.minecraft.Util.getMillis();
        if (currentHealth < lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = millis;
            healthBlinkTime = tickCount + 20;
        } else if (currentHealth > lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = millis;
            healthBlinkTime = tickCount + 10;
        }
        if (millis - lastHealthTime > 1000L) {
            displayHealth = currentHealth;
            lastHealthTime = millis;
        }
        lastHealth = currentHealth;

        RANDOM.setSeed(tickCount * 312871L);
        float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH), (float) Math.max(displayHealth, currentHealth));
        int absorption = Mth.ceil(player.getAbsorptionAmount());
        int healthRows = Mth.ceil((maxHealth + absorption) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        int left = event.getWindow().getGuiScaledWidth() / 2 - 91;
        int top = event.getWindow().getGuiScaledHeight() - gui.leftHeight;
        gui.leftHeight += healthRows * rowHeight;
        if (rowHeight != 10) {
            gui.leftHeight += 10 - rowHeight;
        }

        int regenHeart = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regenHeart = tickCount % Mth.ceil(maxHealth + 5.0F);
        }

        renderHearts(event.getGuiGraphics(), player, heartSet, left, top, rowHeight, regenHeart, maxHealth, currentHealth, displayHealth, absorption, blink);

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }

    private static void renderHearts(GuiGraphics guiGraphics, Player player, String heartSet, int left, int top, int rowHeight, int regenHeart, float maxHealth, int currentHealth, int displayHealth, int absorption, boolean blink) {
        boolean hardcore = player.level().getLevelData().isHardcore();
        int normalHearts = Mth.ceil((double) maxHealth / 2.0);
        int totalHearts = normalHearts + Mth.ceil((double) absorption / 2.0);

        for (int heart = totalHearts - 1; heart >= 0; --heart) {
            int x = left + heart % 10 * 8;
            int y = top - heart / 10 * rowHeight;
            if (currentHealth + absorption <= 4) {
                y += RANDOM.nextInt(2);
            }
            if (heart < normalHearts && heart == regenHeart) {
                y -= 2;
            }

            // container/outline (vanilla icons.png)
            blitIcon(guiGraphics, x, y, 16 + (blink ? 9 : 0), hardcore ? 45 : 0);

            if (heart >= normalHearts) {
                // absorption hearts keep the vanilla golden look (the 1.21 event also only swapped normal hearts' type)
                int absorbed = heart * 2 - normalHearts * 2;
                if (absorbed < absorption) {
                    boolean half = absorbed + 1 == absorption;
                    // vanilla ABSORBING sprites: index 8, no blink
                    blitIcon(guiGraphics, x, y, 16 + (16 + (half ? 1 : 0)) * 9, hardcore ? 45 : 0);
                }
            } else {
                if (blink && heart * 2 < displayHealth) {
                    boolean half = heart * 2 + 1 == displayHealth;
                    blitCustom(guiGraphics, x, y, MFTEHeartTypes.texture(heartSet, half, true, hardcore));
                }
                if (heart * 2 < currentHealth) {
                    boolean half = heart * 2 + 1 == currentHealth;
                    blitCustom(guiGraphics, x, y, MFTEHeartTypes.texture(heartSet, half, false, hardcore));
                }
            }
        }
    }

    private static void blitIcon(GuiGraphics guiGraphics, int x, int y, int u, int v) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 0.03F);
        guiGraphics.blit(GUI_ICONS_LOCATION, x, y, u, v, 9, 9);
        guiGraphics.pose().popPose();
    }

    private static void blitCustom(GuiGraphics guiGraphics, int x, int y, ResourceLocation texture) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 0.03F);
        guiGraphics.blit(texture, x, y, 0, 0.0F, 0.0F, 9, 9, 9, 9);
        guiGraphics.pose().popPose();
    }
}
