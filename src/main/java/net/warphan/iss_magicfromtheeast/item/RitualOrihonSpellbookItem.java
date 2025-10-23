package net.warphan.iss_magicfromtheeast.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.warphan.iss_magicfromtheeast.registries.MFTEAttributeRegistries;

public class RitualOrihonSpellbookItem extends SpellBook {
    public RitualOrihonSpellbookItem() {
        super(10);
        withSpellbookAttributes(
                new AttributeContainer(MFTEAttributeRegistries.SPIRIT_SPELL_POWER, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.SUMMON_DAMAGE, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void handleCustomLecternPosing(PoseStack poseStack) {
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
        poseStack.mulPose(Axis.YP.rotationDegrees(0));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.translate(0.125, -0.15, 0.01);
    }
}
