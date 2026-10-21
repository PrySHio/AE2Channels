package io.github.pryshio.ae2channels.client.mixin;

import appeng.parts.CableBusContainer;
import appeng.client.render.cablebus.CableBusRenderState;

import io.github.pryshio.ae2channels.part.ChannelCapacity;
import io.github.pryshio.ae2channels.client.render.AmplifiedCableRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CableBusContainer.class)
public class CableBusContainerMixin {

    @Inject(
            method = "getRenderState",
            at = @At("RETURN")
    )
    private void ae2channels$markAmplifiedCable(
            CallbackInfoReturnable<CableBusRenderState> cir
    ) {
        CableBusRenderState renderState = cir.getReturnValue();

        CableBusContainer container =
                (CableBusContainer) (Object) this;

        int type = container.getPart(null) instanceof ChannelCapacity amplified
                ? amplified.getAmplifiedTier()
                : 0;

        ((AmplifiedCableRenderState) (Object) renderState)
                .ae2channels$setAmplifiedCableType(type);
    }
}