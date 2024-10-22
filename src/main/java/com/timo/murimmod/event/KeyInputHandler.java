package com.timo.murimmod.event;

import com.timo.murimmod.MurimMod;
import com.timo.murimmod.gui.CultivationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = MurimMod.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {
    public static final KeyBinding CULTIVATION_KEY = new KeyBinding("key.murimmod.cultivation", GLFW.GLFW_KEY_C, "key.categories.murimmod");

    @OnlyIn(Dist.CLIENT)
    public static void register() {
        ClientRegistry.registerKeyBinding(CULTIVATION_KEY);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (CULTIVATION_KEY.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                mc.setScreen(new CultivationScreen(MurimMod.getPlayerCultivation(mc.player)));
            }
        }
    }
}
