package shadows.apotheosis.ench.table;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import shadows.placebo.util.IReplacementBlock;

public class ApothEnchantBlock extends EnchantingTableBlock implements IReplacementBlock {

	public ApothEnchantBlock() {
		super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_RED).strength(5.0F, 1200.0F));
		this.setRegistryName("minecraft:enchanting_table");
	}

	@Override
	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		TileEntity tileentity = world.getBlockEntity(pos);
		if (tileentity instanceof ApothEnchantTile) {
			ITextComponent itextcomponent = ((INameable) tileentity).getDisplayName();
			return new SimpleNamedContainerProvider((id, inventory, player) -> new ApothEnchantContainer(id, inventory, IWorldPosCallable.create(world, pos), (ApothEnchantTile) tileentity), itextcomponent);
		} else {
			return null;
		}
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new ApothEnchantTile();
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getBlockEntity(pos);
			if (tileentity instanceof ApothEnchantTile) {
				Block.popResource(world, pos, ((ApothEnchantTile) tileentity).inv.getStackInSlot(0));
				world.removeBlockEntity(pos);
			}
		}
	}

	@Override
	public void _setDefaultState(BlockState state) {
		this.registerDefaultState(state);
	}

	protected StateContainer<Block, BlockState> container;

	@Override
	public void setStateContainer(StateContainer<Block, BlockState> container) {
		this.container = container;
	}

	@Override
	public StateContainer<Block, BlockState> getStateDefinition() {
		return this.container == null ? super.getStateDefinition() : this.container;
	}

}