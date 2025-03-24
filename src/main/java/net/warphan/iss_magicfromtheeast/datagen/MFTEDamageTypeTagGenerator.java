//package net.warphan.iss_magicfromtheeast.datagen;
//
//import net.minecraft.core.HolderLookup.Provider;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.tags.TagsProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.damagesource.DamageType;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;
//import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
//import net.warphan.iss_magicfromtheeast.mftedamage.MFTEDamageTypes;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.concurrent.CompletableFuture;
//
//public class MFTEDamageTypeTagGenerator extends TagsProvider<DamageType> {
//    public MFTEDamageTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
//        super(output, Registries.DAMAGE_TYPE, lookupProvider, ISS_MagicFromTheEast.MOD_ID, existingFileHelper);
//    }
//
//    private static TagKey<DamageType> create(String name) {
//        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ISS_MagicFromTheEast.MOD_ID, name));
//    }
//
//    public static final TagKey<DamageType> SYMMETRY_MAGIC = create("symmetry_magic");
//    public static final TagKey<DamageType> SPIRIT_MAGIC = create("spirit_magic");
//    public static final TagKey<DamageType> DUNE_MAGIC = create("dune_magic");
//
//    protected void addTags(@NotNull Provider provider) {
//        tag(SYMMETRY_MAGIC).add(MFTEDamageTypes.SYMMETRY_MAGIC);
//        tag(SPIRIT_MAGIC).add(MFTEDamageTypes.SPIRIT_MAGIC);
//        tag(DUNE_MAGIC).add(MFTEDamageTypes.DUNE_MAGIC);
//    }
//}
