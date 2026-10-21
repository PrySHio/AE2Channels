package io.github.pryshio.ae2channels.mixin;

import appeng.api.networking.IGridNode;

import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.me.GridConnection")
public class GridConnectionChannelsMixin {

    @Inject(
            method = "getMaxChannels",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ae2channels$getMaxChannels(
            CallbackInfoReturnable<Integer> cir
    ) {

        IGridNode first = ((appeng.api.networking.IGridConnection) this).a();
        IGridNode second = ((appeng.api.networking.IGridConnection) this).b();

        if (first.getOwner() instanceof WirelessTransmitterBlockEntity
                || second.getOwner() instanceof WirelessTransmitterBlockEntity) {

            cir.setReturnValue(256);
        }
    }
}