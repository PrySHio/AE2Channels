package com.example.examplemod.mixin;

import appeng.api.parts.IPart;
import appeng.parts.CableBusContainer;
import appeng.client.render.cablebus.CableBusRenderState;

import com.example.examplemod.ae2.AmplifiedCablePart;
import com.example.examplemod.ae2.AmplifiedCablePart128;
import com.example.examplemod.ae2.AmplifiedCablePart256;
import com.example.examplemod.render.AmplifiedCableRenderState;

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

        IPart center = container.getPart(null);

        int type = 0;

        if (center != null) {
            if (center.getClass() == AmplifiedCablePart.class) {
                type = 1;
            } else if (center.getClass() == AmplifiedCablePart128.class) {
                type = 2;
            } else if (center.getClass() == AmplifiedCablePart256.class) {
                type = 3;
            }
        }

        ((AmplifiedCableRenderState) (Object) renderState)
                .ae2channels$setAmplifiedCableType(type);
    }
}