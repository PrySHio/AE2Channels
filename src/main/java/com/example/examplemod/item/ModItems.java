package com.example.examplemod.item;

import appeng.api.util.AEColor;
import appeng.items.parts.ColoredPartItem;

import com.example.examplemod.AE2Channels;
import com.example.examplemod.ae2.AmplifiedCablePart;
import com.example.examplemod.ae2.AmplifiedCablePart128;
import com.example.examplemod.ae2.AmplifiedCablePart256;
import com.example.examplemod.block.ModBlocks;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(AE2Channels.MODID);

    public static final DeferredItem<Item> CHANNEL_METER =
            ITEMS.register(
                    "channel_meter",
                    () -> new Item(
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<
            ColoredPartItem<AmplifiedCablePart>
            > AMPLIFIED_CABLE =
            ITEMS.register(
                    "amplified_cable",
                    () -> new ColoredPartItem<>(
                            new Item.Properties().component(
                                    DataComponents.LORE,
                                    new ItemLore(
                                            List.of(
                                                    Component.translatable(
                                                            "item.ae2channels.amplified_cable.description"
                                                    )
                                            )
                                    )
                            ),
                            AmplifiedCablePart.class,
                            AmplifiedCablePart::new,
                            AEColor.TRANSPARENT
                    )
            );

    public static final DeferredItem<
            ColoredPartItem<AmplifiedCablePart128>
            > AMPLIFIED_CABLE_128 =
            ITEMS.register(
                    "amplified_cable_128",
                    () -> new ColoredPartItem<>(
                            new Item.Properties().component(
                                    DataComponents.LORE,
                                    new ItemLore(
                                            List.of(
                                                    Component.translatable(
                                                            "item.ae2channels.amplified_cable_128.description"
                                                    )
                                            )
                                    )
                            ),
                            AmplifiedCablePart128.class,
                            AmplifiedCablePart128::new,
                            AEColor.TRANSPARENT
                    )
            );

    public static final DeferredItem<
            ColoredPartItem<AmplifiedCablePart256>
            > AMPLIFIED_CABLE_256 =
            ITEMS.register(
                    "amplified_cable_256",
                    () -> new ColoredPartItem<>(
                            new Item.Properties().component(
                                    DataComponents.LORE,
                                    new ItemLore(
                                            List.of(
                                                    Component.translatable(
                                                            "item.ae2channels.amplified_cable_256.description"
                                                    )
                                            )
                                    )
                            ),
                            AmplifiedCablePart256.class,
                            AmplifiedCablePart256::new,
                            AEColor.TRANSPARENT
                    )
            );

    public static final DeferredItem<BlockItem> WIRELESS_TRANSMITTER =
            ITEMS.register(
                    "wireless_transmitter",
                    () -> new BlockItem(
                            ModBlocks.WIRELESS_TRANSMITTER.get(),
                            new Item.Properties()
                    )
            );

    public static final DeferredItem<Item> LINKER =
            ITEMS.register(
                    "linker",
                    () -> new LinkerItem(
                            new Item.Properties()
                    )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}