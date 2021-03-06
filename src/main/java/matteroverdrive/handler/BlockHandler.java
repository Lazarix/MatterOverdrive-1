package matteroverdrive.handler;

import matteroverdrive.Reference;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Simeon on 12/24/2015.
 */
public class BlockHandler {
    @SubscribeEvent
    public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() != null) {
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getHarvester());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlaceEvent(BlockEvent.PlaceEvent event) {
        if (event.getPlayer() != null) {
            ResourceLocation blockName = event.getState().getBlock().getRegistryName();
            if (blockName.getResourceDomain().equals(Reference.MOD_ID)) {
            }
            OverdriveExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getPlayer());
            if (extendedProperties != null) {
                extendedProperties.onEvent(event);
            }
        }
    }
}
