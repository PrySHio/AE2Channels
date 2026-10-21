package io.github.pryshio.ae2channels.registry;

import appeng.api.util.AEColor;
import appeng.items.parts.ColoredPartItem;

import io.github.pryshio.ae2channels.AE2Channels;
import io.github.pryshio.ae2channels.item.LinkerItem;
import io.github.pryshio.ae2channels.part.AmplifiedCablePart;
import io.github.pryshio.ae2channels.part.AdvancedCablePart;
import io.github.pryshio.ae2channels.part.QuantumCablePart;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Function;

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

    public static final DeferredItem<ColoredPartItem<AmplifiedCablePart>> AMPLIFIED_CABLE =
            cable("amplified_cable", AmplifiedCablePart.class, AmplifiedCablePart::new);

    public static final DeferredItem<ColoredPartItem<AdvancedCablePart>> AMPLIFIED_CABLE_128 =
            cable("amplified_cable_128", AdvancedCablePart.class, AdvancedCablePart::new);

    public static final DeferredItem<ColoredPartItem<QuantumCablePart>> AMPLIFIED_CABLE_256 =
            cable("amplified_cable_256", QuantumCablePart.class, QuantumCablePart::new);

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

    /**
     * Регистрирует усиленный кабель с описанием из ключа
     * {@code item.ae2channels.<id>.description}.
     */
    private static <T extends AmplifiedCablePart> DeferredItem<ColoredPartItem<T>> cable(
            String id,
            Class<T> partClass,
            Function<ColoredPartItem<T>, T> factory
    ) {
        return ITEMS.register(
                id,
                () -> new ColoredPartItem<T>(
                        new Item.Properties().component(
                                DataComponents.LORE,
                                new ItemLore(
                                        List.of(
                                                Component.translatable(
                                                        "item.ae2channels." + id + ".description"
                                                )
                                        )
                                )
                        ),
                        partClass,
                        factory,
                        AEColor.TRANSPARENT
                )
        );
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}