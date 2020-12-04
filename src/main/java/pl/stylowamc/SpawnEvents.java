package pl.stylowamc;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpawnEvents implements Listener {

    HashMap<String, String> WitherKillers = new HashMap<>();
    HashMap<String, String> DragonKillers = new HashMap<>();

    @EventHandler
    public void WitherSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.WITHER) {
            e.getEntity().setHealth(3024);
            e.getEntity().damage(12);
        }
    }

    @EventHandler
    public void DragonSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.ENDER_DRAGON) {
            e.getEntity().setHealth(3024);
            e.getEntity().damage(10);
        }
    }

    @EventHandler
    public void WitherKill2(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            //Bukkit.broadcastMessage("jestes graczem");
            if (e.getEntity() instanceof Wither) {
                //Bukkit.broadcastMessage("Bijesz withera i jestes graczem");
                WitherKillers.put(p.getName(), p.getName());
            }
        }
    }

    @EventHandler
    public void WitherKill(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.WITHER) {
            Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + WitherKillers.values().toString() + " zabili Withera!");
            WitherKillers.clear();
        }
    }

    @EventHandler
    public void DragonKill2(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            //Bukkit.broadcastMessage("jestes graczem");
            if (e.getEntity() instanceof EnderDragon) {
                //Bukkit.broadcastMessage("Bijesz smoka i jestes graczem");
                DragonKillers.put(p.getName(), p.getName());
            }
        }
    }

    @EventHandler
    public void DragonKill(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.ENDER_DRAGON) {
            Bukkit.broadcastMessage(ChatColor.BLUE + "" + ChatColor.BOLD + DragonKillers.values().toString() + " zabili EnderDragona!");
            DragonKillers.clear();
        }
    }

    @EventHandler
    public void onVillagerInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Villager)) return;

        Villager villager = (Villager) e.getRightClicked();

        List<MerchantRecipe> recipes = Lists.newArrayList(villager.getRecipes());

        Iterator<MerchantRecipe> recipeIterator;
        for (recipeIterator = recipes.iterator(); recipeIterator.hasNext(); ) {
            MerchantRecipe recipe = recipeIterator.next();

            if (recipe.getResult().getType().equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) recipe.getResult().getItemMeta();

                assert meta != null;
                if (meta.hasStoredEnchant(Enchantment.MENDING)) {
                    recipeIterator.remove();
                }
            }
        }

        villager.setRecipes(recipes);

        if (villager.getRecipes().size() == 0) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FishingReplacement(PlayerFishEvent e) {

        if (!e.isCancelled()) {
            if (e.getCaught() instanceof Player) {
                e.setCancelled(true);
            }
            if (e.getCaught() != null)
                if (e.getCaught() instanceof Item) {
                    if(e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY){
                        e.setCancelled(true);
                    }

                }
        }
    }
    @EventHandler
    public void entitytarget(EntityTargetLivingEntityEvent e){
        if(e.isCancelled()) return;
        if(e.getTarget() != null) {
            if (e.getEntity().getType() == EntityType.ENDERMAN && e.getTarget().getType() == EntityType.ENDERMITE) {
                e.setCancelled(true);
            }
        }
    }


}


