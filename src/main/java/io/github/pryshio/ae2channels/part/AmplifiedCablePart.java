package io.github.pryshio.ae2channels.part;

import appeng.api.networking.GridFlags;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.util.AECableType;
import appeng.items.parts.ColoredPartItem;
import appeng.parts.PartModel;
import appeng.parts.networking.CablePart;
import appeng.parts.networking.IUsedChannelProvider;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

public class AmplifiedCablePart extends CablePart implements ChannelCapacity, IUsedChannelProvider {

    public AmplifiedCablePart(ColoredPartItem<?> partItem) {
        super(partItem);
        this.getMainNode().setFlags(GridFlags.DENSE_CAPACITY);
    }

    @Override
    public int getAmplifiedMaxChannels() {
        return 64;
    }

    @Override
    public int getAmplifiedTier() {
        return 1;
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