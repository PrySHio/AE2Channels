package com.example.examplemod.ae2;

import appeng.api.networking.GridFlags;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.util.AECableType;
import appeng.items.parts.ColoredPartItem;
import appeng.parts.PartModel;
import appeng.parts.networking.CablePart;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

public class AmplifiedCablePart extends CablePart implements AmplifiedNode {

    public AmplifiedCablePart(ColoredPartItem<?> partItem) {
        super(partItem);
        this.getMainNode().setFlags(GridFlags.DENSE_CAPACITY);
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

    @Override
    public IPartModel getStaticModels() {
        return new PartModel(
                ResourceLocation.fromNamespaceAndPath(
                        "ae2channels",
                        "part/cable/amplified"
                )
        );
    }
}