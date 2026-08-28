package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import net.warphan.iss_magicfromtheeast.item.LoadableWeaponContents;
import org.apache.commons.lang3.math.Fraction;

/**
 * PORT 1.20.1: GuiGraphics#blitSprite / the gui sprite atlas do not exist on 1.20.1.
 * The sprites are blitted directly from their texture files; the nine-sliced background
 * (32x32, border 4 - see background.png.mcmeta) is drawn by {@link #blitNineSliced}.
 */
@OnlyIn(Dist.CLIENT)
public class ClientLoadableWeaponTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/gui/sprites/container/repeating_crossbow/background.png");
    private static final int BACKGROUND_TEXTURE_SIZE = 32;
    private static final int BACKGROUND_BORDER = 4;
    private final LoadableWeaponContents contents;

    public ClientLoadableWeaponTooltip(LoadableWeaponTooltipComponent contents) {
        this.contents = contents.contents;
    }

    @Override
    public int getHeight() {
        return this.backgroundHeight() + 4;
    }

    @Override
    public int getWidth(Font p_169901_) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * 18 + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * 20 + 2;
    }

    @Override
    public void renderImage(Font p_194042_, int p_194043_, int p_194044_, GuiGraphics p_282522_) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        blitNineSliced(p_282522_, BACKGROUND_TEXTURE, p_194043_, p_194044_, this.backgroundWidth(), this.backgroundHeight());
        boolean flag = this.contents.weight().compareTo(Fraction.ONE) >= 0;
        int k = 0;

        for (int l = 0; l < j; l++) {
            for (int i1 = 0; i1 < i; i1++) {
                int j1 = p_194043_ + i1 * 18 + 1;
                int k1 = p_194044_ + l * 20 + 1;
                this.renderSlot(j1, k1, k++, flag, p_282522_, p_194042_);
            }
        }
    }

    private void renderSlot(int p_283180_, int p_282972_, int p_282547_, boolean p_283053_, GuiGraphics p_283625_, Font p_281863_) {
        if (p_282547_ >= this.contents.size()) {
            this.blit(p_283625_, p_283180_, p_282972_, p_283053_ ? Texture.BLOCKED_SLOT : Texture.SLOT);
        } else {
            ItemStack itemstack = this.contents.getItemUnsafe(p_282547_);
            this.blit(p_283625_, p_283180_, p_282972_, Texture.SLOT);
            p_283625_.renderItem(itemstack, p_283180_ + 1, p_282972_ + 1, p_282547_);
            p_283625_.renderItemDecorations(p_281863_, itemstack, p_283180_ + 1, p_282972_ + 1);
            if (p_282547_ == 0) {
                AbstractContainerScreen.renderSlotHighlight(p_283625_, p_283180_ + 1, p_282972_ + 1, 0);
            }
        }
    }

    private void blit(GuiGraphics p_281273_, int p_282428_, int p_281897_, Texture p_281917_) {
        p_281273_.blit(p_281917_.texture, p_282428_, p_281897_, 0.0F, 0.0F, p_281917_.w, p_281917_.h, p_281917_.w, p_281917_.h);
    }

    /**
     * Draws a nine-sliced texture (corners fixed, edges/center stretched). Replacement for the
     * 1.21 sprite-atlas nine-slice handled by blitSprite + mcmeta.
     */
    private static void blitNineSliced(GuiGraphics gui, ResourceLocation texture, int x, int y, int width, int height) {
        int b = BACKGROUND_BORDER;
        int tex = BACKGROUND_TEXTURE_SIZE;
        int innerTex = tex - 2 * b;
        int innerW = width - 2 * b;
        int innerH = height - 2 * b;
        // corners
        gui.blit(texture, x, y, 0.0F, 0.0F, b, b, tex, tex);
        gui.blit(texture, x + width - b, y, (float) (tex - b), 0.0F, b, b, tex, tex);
        gui.blit(texture, x, y + height - b, 0.0F, (float) (tex - b), b, b, tex, tex);
        gui.blit(texture, x + width - b, y + height - b, (float) (tex - b), (float) (tex - b), b, b, tex, tex);
        // edges
        gui.blit(texture, x + b, y, innerW, b, (float) b, 0.0F, innerTex, b, tex, tex);
        gui.blit(texture, x + b, y + height - b, innerW, b, (float) b, (float) (tex - b), innerTex, b, tex, tex);
        gui.blit(texture, x, y + b, b, innerH, 0.0F, (float) b, b, innerTex, tex, tex);
        gui.blit(texture, x + width - b, y + b, b, innerH, (float) (tex - b), (float) b, b, innerTex, tex, tex);
        // center
        gui.blit(texture, x + b, y + b, innerW, innerH, (float) b, (float) b, innerTex, innerTex, tex, tex);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.contents.size() + 1.0)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.contents.size() + 1.0) / (double)this.gridSizeX());
    }

    @OnlyIn(Dist.CLIENT)
    static enum Texture {
        BLOCKED_SLOT(ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID,"textures/gui/sprites/container/repeating_crossbow/blocked_slot.png"), 18, 20),
        SLOT(ResourceLocation.fromNamespaceAndPath(ISS_MagicFromTheEast.MOD_ID, "textures/gui/sprites/container/repeating_crossbow/slot.png"), 18, 20);

        public final ResourceLocation texture;
        public final int w;
        public final int h;

        private Texture(ResourceLocation p_295000_, int p_169928_, int p_169929_) {
            this.texture = p_295000_;
            this.w = p_169928_;
            this.h = p_169929_;
        }
    }

    public static record LoadableWeaponTooltipComponent(LoadableWeaponContents contents) implements TooltipComponent { }
}
