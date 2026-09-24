package com.example.examplemod.mixin;

import appeng.api.networking.IGridNode;

import com.example.examplemod.ae2.AmplifiedCablePart;
import com.example.examplemod.ae2.AmplifiedCablePart128;
import com.example.examplemod.ae2.AmplifiedCablePart256;
import com.example.examplemod.ae2.AmplifiedNode;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.me.GridNode")
public class AmplifiedGridNodeMixin {

    @Inject(
            method = "getMaxChannels",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ae2channels$getMaxChannels(
            CallbackInfoReturnable<Integer> cir) {

        IGridNode node = (IGridNode) (Object) this;

        if (!(node.getOwner() instanceof AmplifiedNode)) {
            return;
        }

        if (node.getOwner() instanceof AmplifiedCablePart256) {
            cir.setReturnValue(256);
            return;
        }

        if (node.getOwner() instanceof AmplifiedCablePart128) {
            cir.setReturnValue(128);
            return;
        }

        if (node.getOwner() instanceof AmplifiedCablePart) {
            cir.setReturnValue(64);
        }
    }
}