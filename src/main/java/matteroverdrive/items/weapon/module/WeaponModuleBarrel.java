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

package matteroverdrive.items.weapon.module;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.IAdvancedModelProvider;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Simeon on 4/15/2015.
 */
public class WeaponModuleBarrel extends MOBaseItem implements IWeaponModule, IAdvancedModelProvider {
    public static final int DAMAGE_BARREL_ID = 0;
    public static final int FIRE_BARREL_ID = 1;
    public static final int EXPLOSION_BARREL_ID = 2;
    public static final int HEAL_BARREL_ID = 3;
    public static final int DOOMSDAY_BARREL_ID = 4;
    public static final String[] names = {"damage", "fire", "explosion", "heal", "doomsday"};

    public WeaponModuleBarrel(String name) {
        super(name);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_MODULES);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    @Override
    public String[] getSubNames() {
        return names;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public boolean hasDetails(ItemStack itemStack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        super.addDetails(itemstack, player, worldIn, infos);
        int damage = itemstack.getItemDamage();
        switch (damage) {
            case DAMAGE_BARREL_ID:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 1.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EFFECT, 0.5f));
                break;
            case FIRE_BARREL_ID:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 0.75f));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_FIRE_DAMAGE, 1));
                break;
            case EXPLOSION_BARREL_ID:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EXPLOSION_DAMAGE, 1));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.2));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EFFECT, 0.5));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_FIRE_RATE, 0.15));
                break;
            case HEAL_BARREL_ID:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_DAMAGE, 0));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.5));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_HEAL, 0.1));
                break;
            case DOOMSDAY_BARREL_ID:
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EXPLOSION_DAMAGE, 3.0));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_AMMO, 0.2));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_EFFECT, 0.3));
                infos.add(MOStringHelper.weaponStatToInfo(Reference.WS_FIRE_RATE, 0.05));
                break;
        }
    }

    @Override
    public int getSlot(ItemStack module) {
        return Reference.MODULE_BARREL;
    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return null;
    }

    @Override
    public String getModelName(ItemStack module) {
        return null;
    }

    @Override
    public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon, float originalStat) {
        int damage = module.getItemDamage();
        switch (damage) {
            case DAMAGE_BARREL_ID:
                if (statID == Reference.WS_DAMAGE) {
                    return originalStat * 1.5f;
                } else if (statID == Reference.WS_AMMO) {
                    return originalStat * 0.5f;
                } else if (statID == Reference.WS_EFFECT) {
                    return originalStat * 0.5f;
                }
                break;
            case FIRE_BARREL_ID:
                if (statID == Reference.WS_AMMO) {
                    return originalStat * 0.5f;
                }
                if (statID == Reference.WS_DAMAGE) {
                    return originalStat * 0.75f;
                }
                if (statID == Reference.WS_FIRE_DAMAGE) {
                    return originalStat + 1;
                }
                break;
            case EXPLOSION_BARREL_ID:
                if (statID == Reference.WS_EXPLOSION_DAMAGE) {
                    return originalStat + 1f;
                } else if (statID == Reference.WS_AMMO) {
                    return originalStat * 0.2f;
                } else if (statID == Reference.WS_EFFECT) {
                    return originalStat * 0.5f;
                } else if (statID == Reference.WS_FIRE_RATE) {
                    return originalStat * 2f;
                }
                break;
            case HEAL_BARREL_ID:
                if (statID == Reference.WS_DAMAGE) {
                    return 0;
                } else if (statID == Reference.WS_AMMO) {
                    return originalStat * 0.5f;
                } else if (statID == Reference.WS_HEAL) {
                    return originalStat + 0.1f;
                }
                break;
            case DOOMSDAY_BARREL_ID:
                if (statID == Reference.WS_EXPLOSION_DAMAGE) {
                    return originalStat + 3.0f;
                } else if (statID == Reference.WS_AMMO) {
                    return originalStat * 0.2f;
                } else if (statID == Reference.WS_EFFECT) {
                    return originalStat * 0.3f;
                } else if (statID == Reference.WS_FIRE_RATE) {
                    return originalStat + 0.05f;
                }
                break;
        }
        return originalStat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs))
            for (int i = 0; i < names.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        if (damage >= 0 && damage < icons.length)
        {
            return icons[damage];
        }
        return null;
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        return this.getUnlocalizedName() + "." + names[damage];
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        icons = new IIcon[names.length];

        for (int i = 0; i < names.length;i++)
        {
            icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":barrel_" + names[i]);
        }
    }*/
}
