package com.example.examplemod.block;

import appeng.api.networking.GridHelper;
import appeng.blockentity.grid.AENetworkedBlockEntity;

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
    }

    public void serverTick() {

        if (restoreDelay < 0) {
            return;
        }

        if (restoreDelay > 0) {
            restoreDelay--;
            return;
        }

        restoreDelay = -1;

        restoreConnection();
        updatePoweredState();
    }

    @Override
    public void onReady() {
        super.onReady();

        GridHelper.onFirstTick(
                this,
                transmitter -> {
                    transmitter.restoreDelay = 20;
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

        updatePoweredState();
    }

    public void clearLinkedTransmitter() {
        this.linkedTransmitter = null;
        setChanged();

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