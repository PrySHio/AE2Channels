package io.github.pryshio.ae2channels.compat.jade;

import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum WirelessTransmitterProvider
        implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    private static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath("ae2channels", "wireless_transmitter");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    /** Сервер: состояние узла известно только здесь. */
    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof WirelessTransmitterBlockEntity transmitter)) {
            return;
        }

        var node = transmitter.getMainNode().getNode();

        data.putBoolean("Linked", transmitter.getLinkedTransmitter() != null);
        data.putBoolean("Online", transmitter.isLinkOnline());

        if (node != null) {
            data.putInt("Used", node.getUsedChannels());
            data.putInt("Max", node.getMaxChannels());
        }
    }

    /** Клиент: рисуем подсказку из полученных данных. */
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        if (!data.contains("Linked")) {
            return; // данные с сервера ещё не пришли
        }

        if (!data.getBoolean("Linked")) {
            tooltip.add(Component.translatable("jade.ae2channels.transmitter.not_linked")
                    .withStyle(ChatFormatting.GRAY));
        } else if (data.getBoolean("Online")) {
            tooltip.add(Component.translatable("jade.ae2channels.transmitter.online")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("jade.ae2channels.transmitter.offline")
                    .withStyle(ChatFormatting.RED));
        }

        if (data.contains("Max")) {
            tooltip.add(Component.translatable(
                    "jade.ae2channels.transmitter.channels",
                    data.getInt("Used"),
                    data.getInt("Max")
            ));
        }
    }
}