package com.lolpvp.perkbooks;

import com.lolpvp.core.Core;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lolpvp.core.Core.has;


public class PerkBookManager
{
    private static List<PerkBook> perkBooks;
    private static PerkBookManager instance;

    public static void setup() {
        perkBooks = new ArrayList<>();
        perkBooks.add(new PerkBook("ironman", PerkBookType.PERMISSION, "lolpvp.ironman"));
        perkBooks.add(new PerkBook("regen", PerkBookType.PERMISSION, "lolpvp.regen"));
        perkBooks.add(new PerkBook("nightvision", PerkBookType.PERMISSION, "lolpvp.nightvision"));
        perkBooks.add(new PerkBook("fireresistance", PerkBookType.PERMISSION, "lolpvp.fireresistance"));
        perkBooks.add(new PerkBook("vip", PerkBookType.GROUP, "Donator_Zero"));
        perkBooks.add(new PerkBook("vip+", PerkBookType.GROUP, "Donator_Two"));
        perkBooks.add(new PerkBook("thad", PerkBookType.GROUP, "Donator_Three"));
        perkBooks.add(new PerkBook("thad+", PerkBookType.GROUP, "Donator_Four"));
        perkBooks.add(new PerkBook("$$$$$", PerkBookType.GROUP, "Donator_Five"));
        perkBooks.add(new PerkBook("hat", PerkBookType.PERMISSION, "essentials.hat"));
        perkBooks.add(new PerkBook("ptime", PerkBookType.PERMISSION, "essentials.ptime"));
        perkBooks.add(new PerkBook("tptoggle", PerkBookType.PERMISSION, "essentials.tptoggle"));
        perkBooks.add(new PerkBook("heal", PerkBookType.PERMISSION, "essentials.heal"));
        perkBooks.add(new PerkBook("ignore", PerkBookType.PERMISSION, "essentials.ignore"));
        perkBooks.add(new PerkBook("repair", PerkBookType.MULTIPLE_PERMISSIONS, "essentials.repair", "essentials.repair.armor", "essentials.repair.enchanted", "essentials.repair.all"));
        perkBooks.add(new PerkBook("spinning", PerkBookType.PERMISSION, "lolpvp.setmagic"));
    }



    public PerkBook getPerkBookByName(String name) {
        for (PerkBook perkbook : perkBooks) {

            if (perkbook.getPerk().equalsIgnoreCase(name))
                return perkbook;
        }
        return null;
    }
    String page = "You can redeem this book for one PERK_NAME PERK_TYPE \n \n Hold the book and type /redeem if you want to redeem it! \n \n Visit www.LOLPVP.com to unlock more perks!";

    public void givePerkBook(Player player, PerkBook perkbook) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta)item.getItemMeta();
        meta.setTitle("Perk Book");
        meta.setAuthor("ohvals");
        meta.addPage(getPage(perkbook));




        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }


    public String getPage(PerkBook perk) {
        return "You can redeem this book for one " + perk.getPerk() + " " + (perk.getType().equals(PerkBookType.GROUP) ? "rank!" : "perk!") + "\n \n Hold the book and type /redeem if you want to redeem it! \n \n Visit www.LOLPVP.com to unlock more perks!";
    }



    public void redeemPerkBook(Player player, PerkBook perkbook) {
        try {
            if (isPerkBook(player.getItemInHand(), perkbook)) {

                switch (perkbook.getType()) {

                    case PERMISSION:
                        if (!(has(Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())), (perkbook.getPermission())))) {

                            Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())).data().add(Node.builder(perkbook.getPermission()).build());
                            player.sendMessage(ChatColor.GREEN + "You have redeemed a " + perkbook.getPerk().toUpperCase() + " book!");
                            if (player.getItemInHand().getAmount() > 1) {

                                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

                                break;
                            }

                            player.setItemInHand(null);


                            break;
                        }

                        player.sendMessage(ChatColor.RED + "You already have that perk!");
                        break;

                    case MULTIPLE_PERMISSIONS:
                        if (!(has(Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())), perkbook.getPermissions()[0]))) {
                            byte b; int i; String[] arrayOfString;
                            for (i = (arrayOfString = perkbook.getPermissions()).length, b = 0; b < i; ) { String permission = arrayOfString[b];

                                Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())).data().add(Node.builder(permission).build()); b++; }

                            player.sendMessage(ChatColor.GREEN + "You have redeemed a " + perkbook.getPerk().toUpperCase() + " book!");
                            if (player.getItemInHand().getAmount() > 1) {

                                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

                                break;
                            }

                            player.setItemInHand(null);


                            break;
                        }

                        player.sendMessage(ChatColor.RED + "You already have that perk!");
                        break;

                    case GROUP:
                        for (PerkBook pbook : getPerkBooks()) {

                            if (pbook.getType().equals(PerkBookType.GROUP)) {

                                if (!Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())).getPrimaryGroup().equalsIgnoreCase(pbook.getGroup())) {

                                    Objects.requireNonNull(Core.getInstance().getLuckPermsAPI().getUserManager().getUser(player.getUniqueId())).setPrimaryGroup(perkbook.getGroup());
                                    player.sendMessage(ChatColor.GREEN + "You have redeemed a " + perkbook.getPerk().toUpperCase() + " book!");
                                    if (player.getItemInHand().getAmount() > 1) {

                                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);

                                        break;
                                    }

                                    player.setItemInHand(null);


                                    break;
                                }

                                player.sendMessage(ChatColor.RED + "You are already in a group!");
                                break;
                            }
                        }
                        break;
                }


            } else {
                player.sendMessage(ChatColor.RED + "That is not a perkbook!");
            }

        } catch (IllegalArgumentException e) {

            player.sendMessage(ChatColor.RED + "That is not a perkbook!");
        }
    }


    public boolean isPerkBook(ItemStack item, PerkBook perkbook) {
        return (item != null && item.getType().equals(Material.WRITTEN_BOOK) && item.hasItemMeta() &&
                item.getItemMeta() instanceof BookMeta && (
                (BookMeta)item.getItemMeta()).hasTitle() && ((BookMeta)item.getItemMeta()).getTitle().equals("Perk Book") && (
                (BookMeta)item.getItemMeta()).hasAuthor() && ((BookMeta)item.getItemMeta()).getAuthor().equals(perkbook.getAuthor()) && (
                (BookMeta)item.getItemMeta()).hasPages() && ((BookMeta)item.getItemMeta()).getPage(1).contains(getPage(perkbook)));
    }


    public boolean isPerkBook(ItemStack item) {
        return (item != null && item.getType().equals(Material.WRITTEN_BOOK) && item.hasItemMeta() &&
                item.getItemMeta() instanceof BookMeta && (
                (BookMeta)item.getItemMeta()).hasTitle() && ((BookMeta)item.getItemMeta()).getTitle().equals("Perk Book") && (
                (BookMeta)item.getItemMeta()).hasAuthor() && ((BookMeta)item.getItemMeta()).getAuthor().equals("lolitsthad") && (
                (BookMeta)item.getItemMeta()).hasPages());
    }


    public static List<PerkBook> getPerkBooks() {
        return perkBooks;
    }


    public static PerkBookManager getInstance() {
        if (instance == null)
            instance = new PerkBookManager();
        return instance;
    }
}
