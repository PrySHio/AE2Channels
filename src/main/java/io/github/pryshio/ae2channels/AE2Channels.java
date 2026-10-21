package io.github.pryshio.ae2channels;

import io.github.pryshio.ae2channels.registry.ModBlockEntities;
import io.github.pryshio.ae2channels.registry.ModCapabilities;
import io.github.pryshio.ae2channels.registry.ModItems;
import io.github.pryshio.ae2channels.registry.ModCreativeTabs;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import io.github.pryshio.ae2channels.item.ChannelMeterHandler;
import io.github.pryshio.ae2channels.registry.ModBlocks;
import appeng.api.parts.PartModels;

@Mod(AE2Channels.MODID)
public class AE2Channels {

    public static final String MODID = "ae2channels";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AE2Channels(IEventBus modEventBus) {
        LOGGER.info("AE2Channels loaded!");

        PartModels.registerModels(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                        MODID,
                        "part/cable/amplified/amplified"
                )
        );

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(ModCapabilities::register);

        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(
                ChannelMeterHandler.class
        );
    }
}