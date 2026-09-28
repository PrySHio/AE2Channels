package com.example.examplemod.mixin;

import appeng.client.render.cablebus.CableBusBakedModel;
import appeng.client.render.cablebus.CableBusRenderState;
import com.example.examplemod.render.AmplifiedCableRenderContext;
import com.example.examplemod.render.AmplifiedCableRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CableBusBakedModel.class)
public class CableBusBakedModelMixin {

    @Inject(
            method = "addCableQuads",
            at = @At("HEAD")
    )
    private void ae2channels$beginAmplifiedRender(
            CableBusRenderState renderState,
            List<?> quadsOut,
            CallbackInfo ci
    ) {
        int cableType =
                ((AmplifiedCableRenderState) (Object) renderState)
                        .ae2channels$getAmplifiedCableType();

        AmplifiedCableRenderContext.setCableType(cableType);
    }

    @Inject(
            method = "addCableQuads",
            at = @At("RETURN")
    )
    private void ae2channels$endAmplifiedRender(
            CableBusRenderState renderState,
            List<?> quadsOut,
            CallbackInfo ci
    ) {
        AmplifiedCableRenderContext.clear();
    }
}