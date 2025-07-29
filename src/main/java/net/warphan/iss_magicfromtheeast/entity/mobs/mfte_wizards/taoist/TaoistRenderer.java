package net.warphan.iss_magicfromtheeast.entity.mobs.mfte_wizards.taoist;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TaoistRenderer extends AbstractSpellCastingMobRenderer {
    public TaoistRenderer(EntityRendererProvider.Context context) {
        super(context, new TaoistModel());
    }
}
