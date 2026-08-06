package net.warphan.iss_magicfromtheeast.compat;

/**
 * PORT 1.20.1: like ISS 1.20.1-3.16.2, the belt slot is registered by the Curios datapack JSON
 * (data/iss_magicfromtheeast/curios/slots/belt.json + curios/entities/mfte_entities.json), so no
 * InterModComms/SlotTypeMessage registration is needed here.
 */
public class MFTECurios {
    public static String BELT_SLOT = "belt";
}
