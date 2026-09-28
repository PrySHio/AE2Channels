package com.example.examplemod.mixin;

import appeng.api.util.AEColor;
import com.example.examplemod.render.AmplifiedCableRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
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
        int cableType =
                AmplifiedCableRenderContext.getCableType();

        if (cableType == 0) {
            return map.get(key);
        }

        String texturePath;

        switch (cableType) {
            case 1 -> texturePath =
                    "part/cable/amplified";

            case 2 -> texturePath =
                    "part/cable/amplified_128";

            case 3 -> texturePath =
                    "part/cable/amplified_256";

            default -> {
                return map.get(key);
            }
        }

        TextureAtlasSprite sprite =
                Minecraft.getInstance()
                        .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(
                                ResourceLocation.fromNamespaceAndPath(
                                        "ae2channels",
                                        texturePath
                                )
                        );

        EnumMap<AEColor, TextureAtlasSprite> result =
                new EnumMap<>(AEColor.class);

        for (AEColor color : AEColor.values()) {
            result.put(color, sprite);
        }

        return result;
    }
}