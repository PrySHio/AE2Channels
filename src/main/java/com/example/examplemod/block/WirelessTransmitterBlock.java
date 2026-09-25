package com.example.examplemod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

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
            net.minecraft.world.level.block.state.StateDefinition.Builder<Block, BlockState> builder
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

    @Override
    public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            net.minecraft.world.level.block.entity.BlockEntityType<T> blockEntityType
    ) {
        return level.isClientSide
                ? null
                : (level2, pos, state2, blockEntity) -> {
            if (blockEntity instanceof WirelessTransmitterBlockEntity transmitter) {
                transmitter.serverTick();
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            net.minecraft.world.phys.BlockHitResult hit
    ) {

        if (!level.isClientSide) {

            if (level.getBlockEntity(pos)
                    instanceof WirelessTransmitterBlockEntity transmitter) {

                if (transmitter.getLinkedTransmitter() != null) {

                    player.sendSystemMessage(
                            Component.literal(
                                    "Передатчик связан."
                            )
                    );

                } else {

                    player.sendSystemMessage(
                            Component.literal(
                                    "Передатчик не связан."
                            )
                    );
                }
            }
        }

        return InteractionResult.SUCCESS;
    }
}