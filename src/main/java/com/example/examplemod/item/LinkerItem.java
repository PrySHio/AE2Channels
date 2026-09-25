package com.example.examplemod.item;

import com.example.examplemod.block.WirelessTransmitterBlockEntity;

import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridNode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

public class LinkerItem extends Item {

    public LinkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(context.getLevel().getBlockEntity(context.getClickedPos())
                instanceof WirelessTransmitterBlockEntity secondTransmitter)) {

            context.getPlayer().sendSystemMessage(
                    Component.literal(
                            "Нужно нажать по беспроводному передатчику."
                    )
            );

            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();

        CustomData.update(
                DataComponents.CUSTOM_DATA,
                stack,
                tag -> {

                    if (!tag.contains("FirstTransmitter")) {

                        BlockPos pos = context.getClickedPos();

                        CompoundTag first = new CompoundTag();

                        first.putInt("X", pos.getX());
                        first.putInt("Y", pos.getY());
                        first.putInt("Z", pos.getZ());

                        tag.put("FirstTransmitter", first);

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Первый передатчик выбран."
                                )
                        );

                        return;
                    }

                    CompoundTag first =
                            tag.getCompound("FirstTransmitter");

                    BlockPos firstPos = new BlockPos(
                            first.getInt("X"),
                            first.getInt("Y"),
                            first.getInt("Z")
                    );

                    BlockPos secondPos =
                            context.getClickedPos();

                    if (firstPos.equals(secondPos)) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Это тот же самый передатчик."
                                )
                        );

                        return;
                    }

                    if (!(context.getLevel()
                            .getBlockEntity(firstPos)
                            instanceof WirelessTransmitterBlockEntity firstTransmitter)) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Первый передатчик больше не существует."
                                )
                        );

                        tag.remove("FirstTransmitter");
                        return;
                    }

                    /*
                     * Проверяем старую связь первого передатчика.
                     */

                    if (firstTransmitter.getLinkedTransmitter() != null) {

                        BlockPos oldPartner =
                                firstTransmitter.getLinkedTransmitter();

                        if (!(context.getLevel()
                                .getBlockEntity(oldPartner)
                                instanceof WirelessTransmitterBlockEntity oldTransmitter)
                                || !firstPos.equals(
                                oldTransmitter.getLinkedTransmitter())) {

                            firstTransmitter.clearLinkedTransmitter();
                        }
                    }

                    /*
                     * Проверяем старую связь второго передатчика.
                     */

                    if (secondTransmitter.getLinkedTransmitter() != null) {

                        BlockPos oldPartner =
                                secondTransmitter.getLinkedTransmitter();

                        if (!(context.getLevel()
                                .getBlockEntity(oldPartner)
                                instanceof WirelessTransmitterBlockEntity oldTransmitter)
                                || !secondPos.equals(
                                oldTransmitter.getLinkedTransmitter())) {

                            secondTransmitter.clearLinkedTransmitter();
                        }
                    }

                    if (firstTransmitter.getLinkedTransmitter() != null) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Первый передатчик уже связан."
                                )
                        );

                        tag.remove("FirstTransmitter");
                        return;
                    }

                    if (secondTransmitter.getLinkedTransmitter() != null) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Второй передатчик уже связан."
                                )
                        );

                        tag.remove("FirstTransmitter");
                        return;
                    }

                    IGridNode firstNode =
                            firstTransmitter.getMainNode().getNode();

                    IGridNode secondNode =
                            secondTransmitter.getMainNode().getNode();

                    if (firstNode == null || secondNode == null) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Один из передатчиков ещё не подключён к AE2."
                                )
                        );

                        return;
                    }

                    try {

                        IGridConnection connection =
                                GridHelper.createConnection(
                                        firstNode,
                                        secondNode
                                );

                        firstTransmitter.setLinkedTransmitter(
                                secondPos
                        );

                        secondTransmitter.setLinkedTransmitter(
                                firstPos
                        );

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Передатчики связаны!"
                                )
                        );

                    } catch (IllegalStateException e) {

                        context.getPlayer().sendSystemMessage(
                                Component.literal(
                                        "Не удалось создать соединение."
                                )
                        );
                    }

                    tag.remove("FirstTransmitter");
                }
        );

        return InteractionResult.SUCCESS;
    }
}