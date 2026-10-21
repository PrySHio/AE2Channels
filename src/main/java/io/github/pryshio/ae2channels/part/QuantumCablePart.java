package io.github.pryshio.ae2channels.part;

import appeng.items.parts.ColoredPartItem;

public class QuantumCablePart extends AmplifiedCablePart {

    public QuantumCablePart(ColoredPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public int getAmplifiedMaxChannels() {
        return 256;
    }

    @Override
    public int getAmplifiedTier() {
        return 3;
    }
}
