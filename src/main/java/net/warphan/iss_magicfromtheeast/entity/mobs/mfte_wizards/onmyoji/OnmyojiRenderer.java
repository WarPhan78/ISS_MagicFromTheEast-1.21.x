package net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.onmyoji;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class OnmyojiRenderer extends AbstractSpellCastingMobRenderer {
    public OnmyojiRenderer(EntityRendererProvider.Context context) {
        super(context, new OnmyojiModel());
    }
}
