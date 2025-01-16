package net.warphan.iss_magicfromtheeast.entity.spells.jade_judgement;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JadeDaoRenderer extends GeoEntityRenderer<JadeDao> {
    public JadeDaoRenderer(EntityRendererProvider.Context context) {
        super(context, new JadeDaoModel());
        this.shadowRadius = 2.0f;
    }
}
