package net.elipsce.bundlebarrel.block.custom;


import com.mojang.serialization.MapCodec;
import net.elipsce.bundlebarrel.block.entity.custom.BundleBarrelBlockEntity;
import net.elipsce.bundlebarrel.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class BundleBarrelBlock extends BlockWithEntity {
    public static final MapCodec<BundleBarrelBlock> CODEC = createCodec(BundleBarrelBlock::new);
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;



    @Override
    public MapCodec<BundleBarrelBlock> getCodec() {
        return CODEC;
    }

    public BundleBarrelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof BundleBarrelBlockEntity blockEntity) {
            if (player.isSneaking() && hit.getSide() == Direction.UP) {
                world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if (player.isSneaking() && hit.getSide() == Direction.DOWN) {
                world.playSound(player, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if (player.isSneaking() && hit.getSide() == Direction.EAST) {
                blockEntity.EAST = !blockEntity.EAST;
                world.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if (player.isSneaking() && hit.getSide() == Direction.WEST) {
                blockEntity.WEST = !blockEntity.WEST;
                world.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if (player.isSneaking() && hit.getSide() == Direction.NORTH) {
                blockEntity.NORTH = !blockEntity.NORTH;
                world.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if (player.isSneaking() && hit.getSide() == Direction.SOUTH) {
                blockEntity.SOUTH = !blockEntity.SOUTH;
                world.playSound(player, pos, SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value(), SoundCategory.BLOCKS, 1f, ThreadLocalRandom.current().nextFloat(0.75f, 1.25f));
            } else if(world instanceof ServerWorld serverWorld) {
                player.openHandledScreen(blockEntity);
                player.incrementStat(Stats.OPEN_BARREL);
                PiglinBrain.onGuardedBlockInteracted(serverWorld, player, true);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BundleBarrelBlockEntity(pos, state);
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }
}
