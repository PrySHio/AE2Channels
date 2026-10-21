package io.github.pryshio.ae2channels.registry;

import io.github.pryshio.ae2channels.AE2Channels;

import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    AE2Channels.MODID
            );

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<WirelessTransmitterBlockEntity>
            > WIRELESS_TRANSMITTER =
            BLOCK_ENTITIES.register(
                    "wireless_transmitter",
                    () -> BlockEntityType.Builder.of(
                            WirelessTransmitterBlockEntity::new,
                            ModBlocks.WIRELESS_TRANSMITTER.get()
                    ).build(null)
            );
}