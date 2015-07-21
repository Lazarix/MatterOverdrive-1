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

package matteroverdrive.util;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.Reference;
import matteroverdrive.api.network.*;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetworkHelper
{
    public static boolean broadcastTaskInDirection(World world, MatterNetworkPacket taskPacket, IMatterNetworkConnection source, ForgeDirection direction) {
        //if the source connection can connect From Side
        if (source.canConnectFromSide(direction)) {
            BlockPosition position = source.getPosition().step(direction);
            ForgeDirection oppositeDirection = direction.getOpposite();
            TileEntity e = position.getTileEntity(world);
            //if there is any connection in that direction
            if (e instanceof IMatterNetworkConnection) {
                IMatterNetworkConnection connection = (IMatterNetworkConnection) e;
                //check if the packet has passed trough the connection or if it can connect from opposite source side
                if (!taskPacket.hasPassedTrough(connection) && connection.canConnectFromSide(oppositeDirection)) {
                    if (connection instanceof IMatterNetworkCable) {
                        //if the connection is a cable
                        IMatterNetworkCable cable = (IMatterNetworkCable) connection;
                        if (cable.isValid()) {
                            cable.broadcast(taskPacket, direction);
                            return true;
                        }
                    } else if (connection instanceof IMatterNetworkClient) {
                        //if the connection is a client
                        IMatterNetworkClient c = (IMatterNetworkClient) connection;
                        if (c.canPreform(taskPacket)) {
                            c.queuePacket(taskPacket, oppositeDirection);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean broadcastTaskInDirection(World world,byte queueID, MatterNetworkTask task, IMatterNetworkDispatcher dispatcher, ForgeDirection direction)
    {
        return broadcastTaskInDirection(world,queueID,task,dispatcher,direction,null);
    }
    public static boolean broadcastTaskInDirection(World world,byte queueID, MatterNetworkTask task, IMatterNetworkDispatcher dispatcher, ForgeDirection direction,BlockPosition receiver)
    {
        return broadcastTaskInDirection(world, new MatterNetworkTaskPacket(dispatcher, task,queueID,direction,receiver), dispatcher, direction);
    }

    public static void broadcastConnection(World world,IMatterNetworkConnection connection)
    {
        for (int i = 0;i < 6;i++)
        {
            MatterNetworkBroadcastPacket packet = new MatterNetworkBroadcastPacket(connection.getPosition(),Reference.PACKET_BROADCAST_CONNECTION,ForgeDirection.getOrientation(i));
            broadcastTaskInDirection(world, packet, connection, ForgeDirection.getOrientation(i));
        }
    }

    public static void requestNeighborConnections(World world,IMatterNetworkConnection connection)
    {
        for (int i = 0;i < 6;i++)
        {
            MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(connection,Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION,ForgeDirection.getOrientation(i),null);
            broadcastTaskInDirection(world, packet, connection, ForgeDirection.getOrientation(i));
        }
    }

    public static BlockPosition parseBlockPosition(IMatterNetworkBroadcaster broadcaster)
    {
        if (broadcaster.getDestinationFilter() != null) {
            String[] coords = broadcaster.getDestinationFilter().split(",");
            if (coords.length >= 3) {
                int x, y, z;
                try {
                    x = Integer.parseInt(coords[0]);
                    y = Integer.parseInt(coords[1]);
                    z = Integer.parseInt(coords[2]);
                } catch (NumberFormatException e) {
                    return null;
                }

                if (coords.length >= 4) {
                    int portOrdinal = Integer.parseInt(coords[3]);
                    if (portOrdinal >= 0 && portOrdinal < ForgeDirection.values().length) {
                        return new BlockPosition(x, y, z, ForgeDirection.getOrientation(portOrdinal));
                    }
                }
                return new BlockPosition(x, y, z);
            }
        }
        return null;
    }
}
