package com.example.examplemod.block;

import com.example.examplemod.AE2Channels;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(AE2Channels.MODID);

    public static final DeferredBlock<Block> WIRELESS_TRANSMITTER =
            BLOCKS.register(
                    "wireless_transmitter",
                    () -> new WirelessTransmitterBlock(
                            BlockBehaviour.Properties.of()
                                    .strength(3.0f)
                                    .requiresCorrectToolForDrops()
                    )
            );
}