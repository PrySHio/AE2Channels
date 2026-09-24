package com.example.examplemod.ae2;

import appeng.api.parts.IPartCollisionHelper;
import appeng.api.util.AECableType;
import appeng.items.parts.ColoredPartItem;
import appeng.parts.networking.CablePart;

import net.minecraft.core.Direction;

import java.util.function.Predicate;

public class AmplifiedCablePart extends CablePart implements AmplifiedNode {

    public AmplifiedCablePart(ColoredPartItem<?> partItem) {
        super(partItem);
    }

    @Override
    public AECableType getCableConnectionType() {
        return AECableType.SMART;
    }

    @Override
    public void getBoxes(
            IPartCollisionHelper bch,
            Predicate<Direction> filterConnections
    ) {
        updateConnections();

        addNonDenseBoxes(bch, filterConnections, 5.0, 11.0);
    }
}