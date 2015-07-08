package matteroverdrive.blocks;

import cofh.lib.util.helpers.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.client.render.block.MOBlockRenderer;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.tile.TileEntityMachineDecomposer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDecomposer extends MOMatterEnergyStorageBlock
{
	public IIcon iconTop;
	
	public BlockDecomposer(Material material, String name)
	{
		super(material, name, true, true);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe",2);
        setHasGui(true);
	}

	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.iconTop = iconRegister.registerIcon(Reference.MOD_ID + ":" + "decomposer_top");
	}


    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if(side == metadata)
        {
            return GetIconBasedOnMatter(world,x,y,z);
        }

        return getIcon(side,metadata);
    }

    @Override
    public IIcon getIcon(int side,int metadata)
    {
        if(side == BlockHelper.getAboveSide(metadata))
        {
            return this.iconTop;
        }
        else if(side == metadata)
        {
            return MatterOverdriveIcons.Matter_tank_empty;
        }

        return MatterOverdriveIcons.YellowStripes;
    }
    
    @Override
	 public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	 {
		 return true;
	 }
	 
	 public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	 {
		 return true;
	 }
	 
	 @Override
		public TileEntity createNewTileEntity(World world, int meta)
		{
			return new TileEntityMachineDecomposer();
		}

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

}
