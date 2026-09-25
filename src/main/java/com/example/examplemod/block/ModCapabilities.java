package com.example.examplemod.block;

import appeng.api.AECapabilities;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                AECapabilities.IN_WORLD_GRID_NODE_HOST,
                ModBlockEntities.WIRELESS_TRANSMITTER.get(),
                (blockEntity, context) -> blockEntity
        );
    }
}