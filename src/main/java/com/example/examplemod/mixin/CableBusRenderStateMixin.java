package com.example.examplemod.mixin;

import appeng.client.render.cablebus.CableBusRenderState;
import com.example.examplemod.render.AmplifiedCableRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CableBusRenderState.class)
public class CableBusRenderStateMixin
        implements AmplifiedCableRenderState {

    @Unique
    private int ae2channels$amplifiedCableType;

    @Override
    public void ae2channels$setAmplifiedCableType(int type) {
        this.ae2channels$amplifiedCableType = type;
    }

    @Override
    public int ae2channels$getAmplifiedCableType() {
        return ae2channels$amplifiedCableType;
    }

    @Inject(
            method = "hashCode",
            at = @At("RETURN"),
            cancellable = true
    )
    private void ae2channels$includeAmplifiedTypeInHash(
            CallbackInfoReturnable<Integer> cir
    ) {
        cir.setReturnValue(
                31 * cir.getReturnValue()
                        + Integer.hashCode(ae2channels$amplifiedCableType)
        );
    }

    @Inject(
            method = "equals",
            at = @At("RETURN"),
            cancellable = true
    )
    private void ae2channels$includeAmplifiedTypeInEquals(
            Object obj,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!cir.getReturnValue()) {
            return;
        }

        if (!(obj instanceof AmplifiedCableRenderState other)) {
            cir.setReturnValue(false);
            return;
        }

        cir.setReturnValue(
                ae2channels$amplifiedCableType
                        == other.ae2channels$getAmplifiedCableType()
        );
    }
}