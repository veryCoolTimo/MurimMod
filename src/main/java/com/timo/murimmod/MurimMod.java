package com.timo.murimmod;

import com.timo.murimmod.cultivation.PlayerCultivation;
import com.timo.murimmod.event.KeyInputHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(MurimMod.MOD_ID)
public class MurimMod {
    public static final String MOD_ID = "murimmod";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<UUID, PlayerCultivation> playerCultivationMap = new HashMap<>();

    public MurimMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Murim Mod is initializing...");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        KeyInputHandler.register();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID playerUUID = player.getUUID();
        if (!playerCultivationMap.containsKey(playerUUID)) {
            playerCultivationMap.put(playerUUID, new PlayerCultivation());
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerUUID = event.getPlayer().getUUID();
        playerCultivationMap.remove(playerUUID);
    }

    public static PlayerCultivation getPlayerCultivation(PlayerEntity player) {
        return playerCultivationMap.get(player.getUUID());
    }
}
