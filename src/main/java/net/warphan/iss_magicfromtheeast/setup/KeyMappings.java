package net.warphan.iss_magicfromtheeast.setup;

import net.minecraft.client.KeyMapping;
import net.warphan.iss_magicfromtheeast.ISS_MagicFromTheEast;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class KeyMappings {
    public static final KeyMapping FLIGHT_DESCENT_KEY = keyMapping("flight descend", GLFW.GLFW_KEY_Z, "key.categories.movement");
    public static final KeyMapping FLIGHT_ASCENT_KEY = keyMapping("flight_ascent", GLFW.GLFW_KEY_SPACE, "key.categories.movement");

    @SuppressWarnings({"ConstantConditions"})
    private static KeyMapping keyMapping(String name, int defaultMapping, String category)
    {
        return new KeyMapping(String.format("key.%s.%s", ISS_MagicFromTheEast.MOD_ID, name), defaultMapping, category);
    }

    public static void registerKeybinds(Consumer<KeyMapping> registrar) {
        registrar.accept(FLIGHT_DESCENT_KEY);
        registrar.accept(FLIGHT_ASCENT_KEY);
    }
}
