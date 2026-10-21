package io.github.pryshio.ae2channels.client.mixin;

import io.github.pryshio.ae2channels.client.render.AmplifiedCableRenderContext;
import io.github.pryshio.ae2channels.client.render.AmplifiedCableTextures;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.EnumMap;

@Mixin(targets = "appeng.client.render.cablebus.CableBuilder")
public class CableBuilderMixin {

    @Redirect(
            method = {
                    "addSmartConnection",
                    "addStraightSmartConnection",
                    "addConstrainedSmartConnection"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/EnumMap;get(Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private Object ae2channels$replaceSmartTexture(
            EnumMap<?, ?> map,
            Object key
    ) {
        EnumMap<?, ?> replacement = AmplifiedCableTextures.get(
                AmplifiedCableRenderContext.getCableType()
        );

        return replacement != null ? replacement : map.get(key);
    }
}
