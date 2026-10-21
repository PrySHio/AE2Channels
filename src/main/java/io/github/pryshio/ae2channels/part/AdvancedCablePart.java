package io.github.pryshio.ae2channels.part;

import appeng.items.parts.ColoredPartItem;

public class AdvancedCablePart extends AmplifiedCablePart {

    public AdvancedCablePart(ColoredPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public int getAmplifiedMaxChannels() {
        return 128;
    }

    @Override
    public int getAmplifiedTier() {
        return 2;
    }
}
