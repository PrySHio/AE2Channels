package io.github.pryshio.ae2channels.block;

import io.github.pryshio.ae2channels.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class WirelessTransmitterBlock
        extends Block
        implements EntityBlock {

    public static final BooleanProperty LINKED =
            BooleanProperty.create("linked");

    public WirelessTransmitterBlock(
            Properties properties
    ) {
        super(properties);

        registerDefaultState(
                stateDefinition.any().setValue(
                        LINKED,
                        false
                )
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(LINKED);
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new WirelessTransmitterBlockEntity(
                pos,
                state
        );
    }

    private static final BlockEntityTicker<WirelessTransmitterBlockEntity> SERVER_TICKER =
            (level, pos, state, blockEntity) -> blockEntity.serverTick();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide
                || blockEntityType != ModBlockEntities.WIRELESS_TRANSMITTER.get()) {
            return null;
        }

        return (BlockEntityTicker<T>) SERVER_TICKER;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide
                && level.getBlockEntity(pos) instanceof WirelessTransmitterBlockEntity transmitter) {

            player.sendSystemMessage(
                    Component.translatable(
                            transmitter.getLinkedTransmitter() != null
                                    ? "message.ae2channels.transmitter.linked"
                                    : "message.ae2channels.transmitter.not_linked"
                    )
            );
        }

        return InteractionResult.SUCCESS;
    }
}
