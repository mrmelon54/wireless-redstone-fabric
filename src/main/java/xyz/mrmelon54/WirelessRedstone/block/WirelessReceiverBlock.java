package xyz.mrmelon54.WirelessRedstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.mrmelon54.WirelessRedstone.MyComponents;
import xyz.mrmelon54.WirelessRedstone.WirelessRedstone;
import xyz.mrmelon54.WirelessRedstone.block.entity.WirelessReceiverBlockEntity;

public class WirelessReceiverBlock extends WirelessFrequencyBlock {
    public WirelessReceiverBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, WirelessRedstone.WIRELESS_RECEIVER_BLOCK_ENTITY, WirelessReceiverBlockEntity::tick);
    }

    @Deprecated
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        MyComponents.FrequencyStorage.get(world).getReceivers().add(pos);
        WirelessRedstone.sendTickScheduleToReceivers(world);
    }

    @Deprecated
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            world.removeBlockEntity(pos);
            MyComponents.FrequencyStorage.get(world).getReceivers().remove(pos);
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.LIT) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessReceiverBlockEntity(pos, state);
    }
}
