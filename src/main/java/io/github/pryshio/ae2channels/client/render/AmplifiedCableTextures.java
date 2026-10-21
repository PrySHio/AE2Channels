package io.github.pryshio.ae2channels.client.render;

import appeng.api.util.AEColor;

import io.github.pryshio.ae2channels.AE2Channels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;

/**
 * Кэш карт «цвет -> спрайт» для усиленных кабелей. Раньше карта создавалась заново
 * при каждом вызове рендера кабеля; теперь пересоздаётся только если спрайт изменился
 * (например, после перезагрузки ресурсов).
 */
public final class AmplifiedCableTextures {

    /** Индекс = тир кабеля (1..3). */
    private static final ResourceLocation[] TEXTURES = {
            null,
            texture("part/cable/amplified"),
            texture("part/cable/amplified_128"),
            texture("part/cable/amplified_256")
    };

    @SuppressWarnings("unchecked")
    private static final EnumMap<AEColor, TextureAtlasSprite>[] CACHE =
            (EnumMap<AEColor, TextureAtlasSprite>[]) new EnumMap[TEXTURES.length];

    private AmplifiedCableTextures() {
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(AE2Channels.MODID, path);
    }

    /**
     * @return карта спрайтов для тира кабеля или null, если тир неизвестен
     */
    public static EnumMap<AEColor, TextureAtlasSprite> get(int cableType) {
        if (cableType < 1 || cableType >= TEXTURES.length) {
            return null;
        }

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(TEXTURES[cableType]);

        EnumMap<AEColor, TextureAtlasSprite> map = CACHE[cableType];

        if (map == null || map.get(AEColor.TRANSPARENT) != sprite) {
            map = new EnumMap<>(AEColor.class);

            for (AEColor color : AEColor.values()) {
                map.put(color, sprite);
            }

            CACHE[cableType] = map;
        }

        return map;
    }
}
