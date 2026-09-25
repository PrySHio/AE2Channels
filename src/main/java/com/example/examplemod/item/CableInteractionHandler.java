package com.example.examplemod.item;

import appeng.api.networking.IGridNode;
import appeng.api.networking.IInWorldGridNodeHost;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.neoforged.bus.api.SubscribeEvent;

public class CableInteractionHandler {

    @SubscribeEvent
    public static void onBlockClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        ItemStack stack = player.getMainHandItem();

        if (!stack.is(ModItems.CHANNEL_METER.get())) {
            return;
        }

        if (level.isClientSide()) {
            return;
        }

        var blockEntity = level.getBlockEntity(event.getPos());

        if (blockEntity instanceof IInWorldGridNodeHost host) {

            IGridNode node = host.getGridNode(event.getHitVec().getDirection());

            if (node != null) {

                int maxChannels = node.getMaxChannels();
                int usedChannels = node.getUsedChannels();

                player.displayClientMessage(
                        Component.literal(
                                "Channels: " + usedChannels + " / " + maxChannels
                        ),
                        true
                );

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}