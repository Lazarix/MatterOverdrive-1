/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 5/10/2015.
 */
public class PacketChangeRedstoneMode extends TileEntityUpdatePacket
{
    private byte redstoneMode;

    public PacketChangeRedstoneMode(){super();}
    public PacketChangeRedstoneMode(MOTileEntityMachine machine,byte mode)
    {
        super(machine);
        redstoneMode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        redstoneMode = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeByte(redstoneMode);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketChangeRedstoneMode>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketChangeRedstoneMode message, MessageContext ctx) {

            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity instanceof MOTileEntityMachine)
            {
                ((MOTileEntityMachine) entity).setRedstoneMode(message.redstoneMode);
                ((MOTileEntityMachine) entity).ForceSync();
            }
            return null;
        }
    }
}
