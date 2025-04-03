package net.elipsce.bundlebarrel.block.entity.custom;

import net.elipsce.bundlebarrel.block.custom.BundleBarrelBlock;
import net.elipsce.bundlebarrel.block.entity.ModBlockEntities;
import net.elipsce.bundlebarrel.util.TickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BundleBarrelBlockEntity extends LootableContainerBlockEntity implements TickableBlockEntity {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    private static final ItemStack[] bundles = {new ItemStack (Items.BUNDLE), new ItemStack (Items.WHITE_BUNDLE), new ItemStack (Items.LIGHT_GRAY_BUNDLE), new ItemStack (Items.GRAY_BUNDLE),
            new ItemStack (Items.BLACK_BUNDLE), new ItemStack (Items.BROWN_BUNDLE), new ItemStack (Items.RED_BUNDLE), new ItemStack (Items.ORANGE_BUNDLE), new ItemStack (Items.YELLOW_BUNDLE),
            new ItemStack (Items.LIME_BUNDLE), new ItemStack (Items.GREEN_BUNDLE), new ItemStack (Items.CYAN_BUNDLE), new ItemStack (Items.LIGHT_BLUE_BUNDLE),
            new ItemStack (Items.BLUE_BUNDLE), new ItemStack (Items.PURPLE_BUNDLE), new ItemStack (Items.MAGENTA_BUNDLE), new ItemStack (Items.PINK_BUNDLE)};

    public boolean UP = false;
    public boolean DOWN = true;
    public boolean WEST = false;
    public boolean EAST = false;
    public boolean NORTH = false;
    public boolean SOUTH = false;



    public void tick() {
        if(this.world == null || this.world.isClient) {
            return;
        }
        if(!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
        Inventory top = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(0, 1, 0));
        Inventory bottom = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(0, -1, 0));
        Inventory west = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(-1, 0, 0));
        Inventory east = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(1, 0, 0));
        Inventory north = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(0, 0, -1));
        Inventory south = getInventoryInDirection(this.getWorld(), this.getPos(), this.getCachedState(), new Vec3i(0, 0, 1));
        Inventory own = Objects.requireNonNull(getInventoryAt(this.getWorld(), this.getPos()));

        if(top != null) {
            int slot = -1;
            for(int i = 0; i < own.size(); i++) {
                slot = getFirstStackSlotAfter(top, slot);
                if(slot < 0) {
                    break;
                } else {
                    slot = transferTo(top, own, slot, this);
                }
            }
        }

        if(bottom != null) {
            transferFrom(own, bottom, this);
        }

        if(west != null) {
            if(this.WEST) {
                transferFrom(own, west, this);
            } else {
                int slot = -1;
                for(int i = 0; i < own.size(); i++) {
                    slot = getFirstStackSlotAfter(west, slot);
                    if(slot < 0) {
                        break;
                    } else {
                        slot = transferTo(west, own, slot, this);
                    }
                }
            }
        }

        if(east != null) {
            if(this.EAST) {
                transferFrom(own, east, this);
            } else {
                int slot = -1;
                for(int i = 0; i < own.size(); i++) {
                    slot = getFirstStackSlotAfter(east, slot);
                    if(slot < 0) {
                        break;
                    } else {
                        slot = transferTo(east, own, slot, this);
                    }
                }
            }
        }

        if(north != null) {
            if(this.NORTH) {
                transferFrom(own, north, this);
            } else {
                int slot = -1;
                for(int i = 0; i < own.size(); i++) {
                    slot = getFirstStackSlotAfter(north, slot);
                    if(slot < 0) {
                        break;
                    } else {
                        slot = transferTo(north, own, slot, this);
                    }
                }
            }
        }

        if(south != null) {
            if(this.SOUTH) {
                transferFrom(own, south, this);
            } else {
                int slot = -1;
                for(int i = 0; i < own.size(); i++) {
                    slot = getFirstStackSlotAfter(south, slot);
                    if(slot < 0) {
                        break;
                    } else {
                        slot = transferTo(south, own, slot, this);
                    }
                }
            }
        }
    }




    private static int getFirstStackSlotAfter(Inventory inventory, int after) {
        if(inventory.isEmpty() || after == inventory.size()) {
            return -1;
        } else if (after < -1) {
            return after;
        } else {
            ItemStack stack;
            for(int i = after + 1; i < inventory.size() - 1; i++) {
                stack = inventory.getStack(i);
                if(!stack.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean checkIfBundle(ItemStack stack) {
        for (ItemStack bundle : bundles) {
            if (stack.getItem() == bundle.getItem()) {
                return true;
            }
        }
        return false;
    }

    private final ViewerCountManager stateManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            BundleBarrelBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
            BundleBarrelBlockEntity.this.setOpen(state, true);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            BundleBarrelBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
            BundleBarrelBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == BundleBarrelBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public BundleBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BUNDLE_BARREL_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, registries);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory, registries);
        }
    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.bundle_barrel");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    private static int transferTo(@Nullable Inventory from, Inventory to, int slotFrom, BundleBarrelBlockEntity entity) {
        boolean isBundle = false;
        for(int i = 0; i < to.size(); i++) {
            ItemStack bundle = to.getStack(i);
            if(checkIfBundle(bundle)) {
                isBundle = true;
                ComponentMap bundleComponents = bundle.getComponents();
                BundleContentsComponent bundleContents = bundleComponents.get(DataComponentTypes.BUNDLE_CONTENTS);
                assert bundleContents != null;
                BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContents);
                assert from != null;
                int addResult = builder.add(from.getStack(slotFrom).copyWithCount(1));
                if(addResult != 0) {
                    bundle.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                    from.getStack(slotFrom).decrement(1);
                    from.markDirty();
                    to.markDirty();
                    return -2;
                }
            }
        }
        if(!isBundle) {
            return -3;
        } else {
            return slotFrom;
        }
    }

    private static void transferFrom(Inventory from, Inventory to, BundleBarrelBlockEntity entity) {
        boolean isBundle = false;
        for(int i = 0; i < from.size(); i++) {
            ItemStack bundle = from.getStack(i);
            if(checkIfBundle(bundle)) {
                isBundle = true;

                ComponentMap bundleComponents = bundle.getComponents();
                BundleContentsComponent bundleContents = bundleComponents.get(DataComponentTypes.BUNDLE_CONTENTS);
                if(BundleItem.getAmountFilled(bundle) != 0) {
                    BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContents);
                    ItemStack removed = builder.removeSelected();
                    int newCount;
                    ItemStack inserting;
                    if(removed == null){
                        newCount = 0;
                        inserting = null;
                    } else {
                        newCount = removed.getCount() - 1;
                        inserting = removed.copyWithCount(1);
                    }
                    for(int j = 0; j < to.size(); j++){
                        if(inserting != null) {
                            if(to.getStack(j).isEmpty()) {
                                to.setStack(j, inserting);
                                if(newCount > 0) {
                                    builder.add(removed.copyWithCount(newCount));
                                }
                                bundle.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                                return;
                            } else if(to.getStack(j).getItem() == inserting.getItem() && to.getStack(j).getCount() < to.getStack(j).getMaxCount()) {
                                to.getStack(j).increment(1);
                                if(newCount > 0) {
                                    builder.add(removed.copyWithCount(newCount));
                                }
                                bundle.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private static Inventory getInventoryInDirection(World world, BlockPos pos, BlockState state, Vec3i direction) {
        return getInventoryAt(world, pos.add(direction));
    }

    @Nullable
    private static Inventory getInventoryAtPrivate(World world, BlockPos pos, BlockState state, double x, double y, double z) {
        Inventory inventory = getBlockInventoryAt(world, pos, state);
        if (inventory == null) {
            inventory = getEntityInventoryAt(world, x, y, z);
        }

        return inventory;
    }

    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos pos) {
        return getInventoryAtPrivate(world, pos, world.getBlockState(pos), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Nullable
    private static Inventory getBlockInventoryAt(World world, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        if (block instanceof InventoryProvider) {
            return ((InventoryProvider)block).getInventory(state, world, pos);
        } else if (state.hasBlockEntity() && world.getBlockEntity(pos) instanceof Inventory inventory) {
            if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                inventory = ChestBlock.getInventory((ChestBlock)block, state, world, pos, true);
            }

            if(inventory instanceof HopperBlockEntity && block instanceof HopperBlock) {
                return null;
            } else {
                return inventory;
            }
        } else {
            return null;
        }
    }

    @Nullable
    private static Inventory getEntityInventoryAt(World world, double x, double y, double z) {
        List<Entity> list = world.getOtherEntities((Entity)null, new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntityPredicates.VALID_INVENTORIES);
        return !list.isEmpty() ? (Inventory)list.get(world.random.nextInt(list.size())) : null;
    }

    void setOpen(BlockState state, boolean open) {
        assert this.world != null;
        this.world.setBlockState(this.getPos(), state.with(BundleBarrelBlock.OPEN, open), Block.NOTIFY_ALL);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = ((Direction)state.get(BundleBarrelBlock.FACING)).getVector();
        double d = this.pos.getX() + 0.5 + vec3i.getX() / 2.0;
        double e = this.pos.getY() + 0.5 + vec3i.getY() / 2.0;
        double f = this.pos.getZ() + 0.5 + vec3i.getZ() / 2.0;
        assert this.world != null;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
}

