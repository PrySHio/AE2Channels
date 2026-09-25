package com.example.examplemod.mixin;

import appeng.api.networking.IGridNode;

import com.example.examplemod.block.WirelessTransmitterBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.me.GridNode")
public class WirelessTransmitterGridNodeMixin {

    @Inject(
            method = "getMaxChannels",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ae2channels$getMaxChannels(
            CallbackInfoReturnable<Integer> cir) {

        IGridNode node = (IGridNode) (Object) this;

        if (!(node.getOwner()
                instanceof WirelessTransmitterBlockEntity)) {
            return;
        }

        cir.setReturnValue(256);
    }
}