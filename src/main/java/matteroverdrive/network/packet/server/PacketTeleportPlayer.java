package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.data.biostats.BioticStatTeleport;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.PacketSpawnParticle;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 6/1/2015.
 */
public class PacketTeleportPlayer extends PacketAbstract {

    double x,y,z;

    public PacketTeleportPlayer()
    {

    }

    public PacketTeleportPlayer(Vec3 vec3)
    {
        x = vec3.xCoord;
        y = vec3.yCoord;
        z = vec3.zCoord;
    }

    public PacketTeleportPlayer(double x,double y,double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketTeleportPlayer>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketTeleportPlayer message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketSpawnParticle("teleport",player.posX,player.posY + 1,player.posZ,1, 0),player,64);
                player.worldObj.playSoundToNearExcept(player, Reference.MOD_ID + ":" + "android_teleport",0.2f,0.8f + 0.4f * player.worldObj.rand.nextFloat());
                player.setPositionAndUpdate(message.x, message.y, message.z);
                player.worldObj.playSoundEffect(message.x,message.y,message.z, Reference.MOD_ID + ":" + "android_teleport",0.2f,0.8f + 0.4f * player.worldObj.rand.nextFloat());
                androidPlayer.getEffects().setLong("LastTeleport", player.worldObj.getTotalWorldTime() + BioticStatTeleport.TELEPORT_DELAY);
                androidPlayer.getEffects().setInteger("GlitchTime",5);
                androidPlayer.extractEnergy(BioticStatTeleport.ENERGY_PER_TELEPORT,false);
                androidPlayer.sync(PacketSyncAndroid.SYNC_ALL);
                androidPlayer.getPlayer().fallDistance = 0;
            }
            return null;
        }
    }
}
