package io.github.pryshio.ae2channels.mixin;

import appeng.api.networking.IGridNode;

import io.github.pryshio.ae2channels.part.ChannelCapacity;
import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Лимит каналов узлов: усиленные кабели (64/128/256) и беспроводной передатчик (256).
 */
@Mixin(targets = "appeng.me.GridNode")
public class GridNodeChannelsMixin {

    @Inject(
            method = "getMaxChannels",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ae2channels$getMaxChannels(
            CallbackInfoReturnable<Integer> cir
    ) {
        Object owner = ((IGridNode) (Object) this).getOwner();

        if (owner instanceof ChannelCapacity amplified) {
            cir.setReturnValue(amplified.getAmplifiedMaxChannels());
        } else if (owner instanceof WirelessTransmitterBlockEntity) {
            cir.setReturnValue(256);
        }
    }
}
