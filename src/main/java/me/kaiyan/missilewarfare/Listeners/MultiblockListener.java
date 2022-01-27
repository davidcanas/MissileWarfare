package me.kaiyan.missilewarfare.Listeners;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.kaiyan.missilewarfare.CustomItems;
import me.kaiyan.missilewarfare.Items.ManPad;
import me.kaiyan.missilewarfare.MissileWarfare;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Skull;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MultiblockListener implements Listener {
    @EventHandler
    public void MultiBlockInteractEvent(MultiBlockInteractEvent event){
        if (event.getMultiBlock().getSlimefunItem() instanceof EnhancedCraftingTable){
            Inventory inv = ((Dispenser) event.getClickedBlock().getRelative(BlockFace.DOWN).getState()).getInventory();

            if (isCraftable(inv, SlimefunItem.getById("MANPAD").getRecipe())){
                event.setCancelled(true);

                ItemStack item = SlimefunItem.getById("MANPAD").getItem().clone();

                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(MissileWarfare.getInstance(), "unstackablemissile"), PersistentDataType.STRING, UUID.randomUUID().toString());
                item.setItemMeta(meta);

                event.getPlayer().getInventory().addItem(item);
                event.getPlayer().updateInventory();
            }
        }
    }
    // definitly not borrowed code
    private boolean isCraftable(Inventory inv, ItemStack[] recipe) {
        for(int j = 0; j < inv.getContents().length; ++j) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                if (!(SlimefunItem.getByItem(recipe[j]) instanceof SlimefunBackpack)) {
                    return false;
                }

                if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], false)) {
                    return false;
                }
            }

            ItemUtils.consumeItem(inv.getContents()[j],1, false);
        }

        return true;
    }
}
