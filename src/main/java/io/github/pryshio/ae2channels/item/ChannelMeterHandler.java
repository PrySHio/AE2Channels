package io.github.pryshio.ae2channels.item;

import appeng.api.networking.IGridNode;
import appeng.api.networking.IInWorldGridNodeHost;

import io.github.pryshio.ae2channels.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ChannelMeterHandler {

    @SubscribeEvent
    public static void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()
                || !event.getItemStack().is(ModItems.CHANNEL_METER.get())) {
            return;
        }

        if (!(event.getLevel().getBlockEntity(event.getPos())
                instanceof IInWorldGridNodeHost host)) {
            return;
        }

        IGridNode node = host.getGridNode(event.getHitVec().getDirection());

        if (node == null) {
            return;
        }

        event.getEntity().displayClientMessage(
                Component.translatable(
                        "message.ae2channels.transmitter.channels",
                        node.getUsedChannels(),
                        node.getMaxChannels()
                ),
                true
        );

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
