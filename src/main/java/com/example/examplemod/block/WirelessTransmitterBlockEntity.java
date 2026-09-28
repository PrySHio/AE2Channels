package com.example.examplemod.block;

import appeng.api.networking.GridFlags;
import appeng.api.networking.GridHelper;
import appeng.api.stacks.AEItemKey;
import appeng.blockentity.grid.AENetworkedBlockEntity;

import com.example.examplemod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessTransmitterBlockEntity
        extends AENetworkedBlockEntity {

    private BlockPos linkedTransmitter;
    private int restoreDelay = -1;

    public WirelessTransmitterBlockEntity(
            BlockPos pos,
            BlockState blockState
    ) {
        super(
                ModBlockEntities.WIRELESS_TRANSMITTER.get(),
                pos,
                blockState
        );

        getMainNode().setFlags(GridFlags.DENSE_CAPACITY);

        getMainNode().setIdlePowerUsage(16.0);

        getMainNode().setVisualRepresentation(
                AEItemKey.of(ModItems.WIRELESS_TRANSMITTER.get())
        );
    }

    public void serverTick() {

        if (linkedTransmitter != null
                && getLevel() != null
                && !(getLevel().getBlockEntity(linkedTransmitter)
                instanceof WirelessTransmitterBlockEntity)) {

            clearLinkedTransmitter();
            return;
        }

        if (restoreDelay < 0) {
            return;
        }

        if (restoreDelay > 0) {
            restoreDelay--;
            return;
        }

        restoreDelay = -1;

        restoreConnection();
        updatePowerUsage();
        updatePoweredState();
    }

    @Override
    public void onReady() {
        super.onReady();

        GridHelper.onFirstTick(
                this,
                transmitter -> {
                    transmitter.restoreDelay = 20;
                    transmitter.updatePowerUsage();
                    transmitter.updatePoweredState();
                }
        );
    }

    private void restoreConnection() {

        if (linkedTransmitter == null) {
            return;
        }

        if (getLevel() == null) {
            return;
        }

        if (!(getLevel().getBlockEntity(linkedTransmitter)
                instanceof WirelessTransmitterBlockEntity other)) {
            return;
        }

        var firstNode =
                getMainNode().getNode();

        var secondNode =
                other.getMainNode().getNode();

        if (firstNode == null || secondNode == null) {
            return;
        }

        try {
            GridHelper.createConnection(
                    firstNode,
                    secondNode
            );
        } catch (IllegalStateException ignored) {
        }
    }

    private void updatePowerUsage() {

        double powerUsage = 16.0;

        if (linkedTransmitter != null) {

            double distance =
                    Math.sqrt(
                            linkedTransmitter.distSqr(getBlockPos())
                    );

            powerUsage =
                    16.0 * (1.0 + distance * 1.1);
        }

        getMainNode().setIdlePowerUsage(powerUsage);
    }

    @Override
    public void onMainNodeStateChanged(
            appeng.api.networking.IGridNodeListener.State reason
    ) {
        updatePoweredState();
    }

    private void updatePoweredState() {

        boolean powered = false;

        var node =
                getMainNode().getNode();

        if (linkedTransmitter != null
                && node != null
                && node.isActive()) {

            powered = true;
        }

        BlockState currentState =
                getBlockState();

        if (currentState.getValue(
                WirelessTransmitterBlock.LINKED
        ) != powered) {

            getLevel().setBlock(
                    getBlockPos(),
                    currentState.setValue(
                            WirelessTransmitterBlock.LINKED,
                            powered
                    ),
                    3
            );
        }
    }

    public BlockPos getLinkedTransmitter() {
        return linkedTransmitter;
    }

    public void setLinkedTransmitter(BlockPos pos) {
        this.linkedTransmitter = pos;
        setChanged();

        updatePowerUsage();
        updatePoweredState();
    }

    public void clearLinkedTransmitter() {
        this.linkedTransmitter = null;
        setChanged();

        updatePowerUsage();
        updatePoweredState();
    }

    @Override
    public void loadTag(
            CompoundTag data,
            HolderLookup.Provider registries
    ) {
        super.loadTag(data, registries);

        if (data.contains("LinkedTransmitter")) {

            CompoundTag linked =
                    data.getCompound("LinkedTransmitter");

            linkedTransmitter = new BlockPos(
                    linked.getInt("X"),
                    linked.getInt("Y"),
                    linked.getInt("Z")
            );

        } else {

            linkedTransmitter = null;
        }
    }

    @Override
    public void saveAdditional(
            CompoundTag data,
            HolderLookup.Provider registries
    ) {
        super.saveAdditional(data, registries);

        if (linkedTransmitter != null) {

            CompoundTag linked =
                    new CompoundTag();

            linked.putInt(
                    "X",
                    linkedTransmitter.getX()
            );

            linked.putInt(
                    "Y",
                    linkedTransmitter.getY()
            );

            linked.putInt(
                    "Z",
                    linkedTransmitter.getZ()
            );

            data.put(
                    "LinkedTransmitter",
                    linked
            );
        }
    }
}