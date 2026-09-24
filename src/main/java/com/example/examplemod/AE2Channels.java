package com.example.examplemod;

import com.example.examplemod.item.ModItems;
import com.example.examplemod.item.ModCreativeTabs;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import com.example.examplemod.ae2.CableInteractionHandler;

@Mod(AE2Channels.MODID)
public class AE2Channels {

    public static final String MODID = "ae2channels";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AE2Channels(IEventBus modEventBus) {
        LOGGER.info("AE2Channels loaded!");

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(
                CableInteractionHandler.class
        );
    }
}