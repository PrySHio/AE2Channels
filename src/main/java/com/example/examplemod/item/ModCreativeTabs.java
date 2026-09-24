package com.example.examplemod.item;

import com.example.examplemod.AE2Channels;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(
                    net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
                    AE2Channels.MODID
            );

    public static final Supplier<CreativeModeTab> AE2_CHANNELS_TAB =
            CREATIVE_MODE_TABS.register("ae2_channels", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.ae2channels.ae2_channels"))
                            .icon(() -> new ItemStack(ModItems.AMPLIFIED_CABLE.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.CHANNEL_METER.get());
                                output.accept(ModItems.AMPLIFIED_CABLE.get());
                                output.accept(ModItems.AMPLIFIED_CABLE_128.get());
                                output.accept(ModItems.AMPLIFIED_CABLE_256.get());
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}