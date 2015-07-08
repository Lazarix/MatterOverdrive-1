package matteroverdrive.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.tile.TileEntityMachineSolarPanel;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

/**
 * Created by Simeon on 4/9/2015.
 */
public class ContainerSolarPanel extends ContainerMachine<TileEntityMachineSolarPanel>
{
    int lastChargeAmount;

    public ContainerSolarPanel(InventoryPlayer inventory, TileEntityMachineSolarPanel machine)
    {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.machine.getChargeAmount());
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for(int i = 0;i < this.crafters.size();i++)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if(this.lastChargeAmount != this.machine.getChargeAmount())
            {
                icrafting.sendProgressBarUpdate(this, 0, this.machine.getChargeAmount());
            }

            this.lastChargeAmount = this.machine.getChargeAmount();
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot,int newValue)
    {
        if(slot == 0)
            this.machine.setChargeAmount((byte)newValue);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }
}
