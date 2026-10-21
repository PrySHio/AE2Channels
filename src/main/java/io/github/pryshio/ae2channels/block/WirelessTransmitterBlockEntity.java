package io.github.pryshio.ae2channels.block;

import appeng.api.networking.GridFlags;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNodeListener;
import appeng.api.stacks.AEItemKey;
import appeng.blockentity.grid.AENetworkedBlockEntity;

import io.github.pryshio.ae2channels.registry.ModItems;
import io.github.pryshio.ae2channels.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WirelessTransmitterBlockEntity
        extends AENetworkedBlockEntity {

    private static final double BASE_POWER_USAGE = 16.0;

    /** Как часто (в тиках) проверяется связь с партнёром и восстанавливается соединение. */
    private static final int CHECK_INTERVAL = 20;

    private BlockPos linkedTransmitter;
    private int checkTimer;

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
        getMainNode().setIdlePowerUsage(BASE_POWER_USAGE);
        getMainNode().setVisualRepresentation(
                AEItemKey.of(ModItems.WIRELESS_TRANSMITTER.get())
        );
    }

    /**
     * Вызывается каждый тик на сервере, но работу делает раз в {@link #CHECK_INTERVAL} тиков:
     * сбрасывает «битую» связь и (пере)создаёт AE2-соединение с партнёром.
     */
    public void serverTick() {
        if (linkedTransmitter == null || ++checkTimer < CHECK_INTERVAL) {
            return;
        }
        checkTimer = 0;

        Level level = getLevel();

        // Партнёр в незагруженном чанке: связь не трогаем, проверим позже.
        if (level == null || !level.hasChunkAt(linkedTransmitter)) {
            return;
        }

        WirelessTransmitterBlockEntity partner = findPartner(level);

        if (partner == null) {
            clearLinkedTransmitter();
            return;
        }

        connectTo(partner);
    }

    @Override
    public void onReady() {
        super.onReady();

        GridHelper.onFirstTick(
                this,
                transmitter -> {
                    transmitter.updatePowerUsage();
                    transmitter.updatePoweredState();
                }
        );
    }

    /**
     * Возвращает партнёра, только если он существует и ссылается обратно на нас;
     * иначе null (партнёр сломан, заменён другим блоком или связь односторонняя).
     * Чанк партнёра должен быть загружен.
     */
    private WirelessTransmitterBlockEntity findPartner(Level level) {
        if (level.getBlockEntity(linkedTransmitter)
                instanceof WirelessTransmitterBlockEntity other
                && getBlockPos().equals(other.linkedTransmitter)) {
            return other;
        }

        return null;
    }

    /**
     * true, если связь «битая»: партнёр загружен, но его нет или он не связан с нами.
     * Партнёр в незагруженном чанке считается валидным.
     */
    public boolean isLinkStale() {
        Level level = getLevel();

        return linkedTransmitter != null
                && level != null
                && level.hasChunkAt(linkedTransmitter)
                && findPartner(level) == null;
    }

    /** Связь работает: оба узла активны в сети AE2, партнёр загружен и ссылается на нас. */
    public boolean isLinkOnline() {
        Level level = getLevel();
        var node = getMainNode().getNode();

        if (linkedTransmitter == null || level == null
                || node == null || !node.isActive()
                || !level.hasChunkAt(linkedTransmitter)) {
            return false;
        }

        WirelessTransmitterBlockEntity partner = findPartner(level);
        var partnerNode = partner == null ? null : partner.getMainNode().getNode();

        return partnerNode != null && partnerNode.isActive();
    }

    private void connectTo(WirelessTransmitterBlockEntity other) {
        var firstNode = getMainNode().getNode();
        var secondNode = other.getMainNode().getNode();

        if (firstNode == null || secondNode == null) {
            return;
        }

        try {
            GridHelper.createConnection(firstNode, secondNode);
        } catch (IllegalStateException alreadyConnected) {
            return;
        }

        updatePoweredState();
    }

    private void updatePowerUsage() {
        double powerUsage = BASE_POWER_USAGE;

        if (linkedTransmitter != null) {
            double distance = Math.sqrt(linkedTransmitter.distSqr(getBlockPos()));
            powerUsage = BASE_POWER_USAGE * (1.0 + distance * 1.1);
        }

        getMainNode().setIdlePowerUsage(powerUsage);
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        updatePoweredState();
    }

    private void updatePoweredState() {
        Level level = getLevel();

        if (level == null || level.isClientSide) {
            return;
        }

        var node = getMainNode().getNode();
        boolean powered = linkedTransmitter != null
                && node != null
                && node.isActive();

        BlockState currentState = getBlockState();

        if (currentState.getValue(WirelessTransmitterBlock.LINKED) != powered) {
            level.setBlock(
                    getBlockPos(),
                    currentState.setValue(WirelessTransmitterBlock.LINKED, powered),
                    3
            );
        }
    }

    public BlockPos getLinkedTransmitter() {
        return linkedTransmitter;
    }

    public void setLinkedTransmitter(BlockPos pos) {
        this.linkedTransmitter = pos;
        this.checkTimer = 0;
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
            CompoundTag linked = data.getCompound("LinkedTransmitter");

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
            CompoundTag linked = new CompoundTag();

            linked.putInt("X", linkedTransmitter.getX());
            linked.putInt("Y", linkedTransmitter.getY());
            linked.putInt("Z", linkedTransmitter.getZ());

            data.put("LinkedTransmitter", linked);
        }
    }
}
