package io.github.pryshio.ae2channels.item;

import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;

import appeng.api.networking.GridHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class LinkerItem extends Item {

    private static final String TAG_FIRST = "FirstTransmitter";
    private static final String TAG_DIMENSION = "Dimension";

    public LinkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockPos secondPos = context.getClickedPos();

        if (!(level.getBlockEntity(secondPos)
                instanceof WirelessTransmitterBlockEntity secondTransmitter)) {
            message(player, "click_required");
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        CompoundTag tag = stack
                .getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag();
        String dimension = level.dimension().location().toString();

        // Первый клик: запоминаем передатчик.
        if (!tag.contains(TAG_FIRST)) {
            CompoundTag first = new CompoundTag();
            first.putInt("X", secondPos.getX());
            first.putInt("Y", secondPos.getY());
            first.putInt("Z", secondPos.getZ());

            tag.put(TAG_FIRST, first);
            tag.putString(TAG_DIMENSION, dimension);
            saveTag(stack, tag);

            message(player, "first_selected");
            return InteractionResult.SUCCESS;
        }

        CompoundTag first = tag.getCompound(TAG_FIRST);
        BlockPos firstPos = new BlockPos(
                first.getInt("X"),
                first.getInt("Y"),
                first.getInt("Z")
        );

        // Старые линкеры без сохранённого измерения считаем относящимися к текущему.
        boolean sameDimension = !tag.contains(TAG_DIMENSION)
                || dimension.equals(tag.getString(TAG_DIMENSION));

        if (sameDimension && firstPos.equals(secondPos)) {
            message(player, "same_transmitter");
            return InteractionResult.SUCCESS;
        }

        if (!sameDimension) {
            message(player, "other_dimension");
            clearSelection(stack, tag);
            return InteractionResult.SUCCESS;
        }

        if (!level.hasChunkAt(firstPos)
                || !(level.getBlockEntity(firstPos)
                instanceof WirelessTransmitterBlockEntity firstTransmitter)) {
            message(player, "first_not_exists");
            clearSelection(stack, tag);
            return InteractionResult.SUCCESS;
        }

        // Сбрасываем «битые» старые связи (партнёр пропал или не связан с нами).
        if (firstTransmitter.isLinkStale()) {
            firstTransmitter.clearLinkedTransmitter();
        }

        if (secondTransmitter.isLinkStale()) {
            secondTransmitter.clearLinkedTransmitter();
        }

        if (firstTransmitter.getLinkedTransmitter() != null) {
            message(player, "first_already_linked");
            clearSelection(stack, tag);
            return InteractionResult.SUCCESS;
        }

        if (secondTransmitter.getLinkedTransmitter() != null) {
            message(player, "second_already_linked");
            clearSelection(stack, tag);
            return InteractionResult.SUCCESS;
        }

        var firstNode = firstTransmitter.getMainNode().getNode();
        var secondNode = secondTransmitter.getMainNode().getNode();

        if (firstNode == null || secondNode == null) {
            // Выбор первого передатчика сохраняем, чтобы можно было повторить позже.
            message(player, "ae2_not_connected");
            return InteractionResult.SUCCESS;
        }

        try {
            GridHelper.createConnection(firstNode, secondNode);

            firstTransmitter.setLinkedTransmitter(secondPos);
            secondTransmitter.setLinkedTransmitter(firstPos);

            message(player, "linked_success");
        } catch (IllegalStateException e) {
            message(player, "link_failed");
        }

        clearSelection(stack, tag);
        return InteractionResult.SUCCESS;
    }

    private static void message(Player player, String key) {
        player.sendSystemMessage(
                Component.translatable("message.ae2channels.transmitter." + key)
        );
    }

    private static void clearSelection(ItemStack stack, CompoundTag tag) {
        tag.remove(TAG_FIRST);
        tag.remove(TAG_DIMENSION);
        saveTag(stack, tag);
    }

    /**
     * Пустой тег убираем целиком, иначе использованный линкер остаётся с пустым
     * компонентом custom_data и не стакается с новыми.
     */
    private static void saveTag(ItemStack stack, CompoundTag tag) {
        if (tag.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
    }
}
