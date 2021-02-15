package com.lolpvp.core;

import com.lolpvp.chat.ChatFix;
import com.lolpvp.chat.ChatMethod;
import com.lolpvp.chat.ChatMethod2;
import com.lolpvp.chests.Chest;
import com.lolpvp.chests.ChestListener;
import com.lolpvp.chests.ChestManager;
import com.lolpvp.commands.LOLPVPCommand;
import com.lolpvp.commands.classes.*;
import com.lolpvp.commands.kits.Kits;
import com.lolpvp.commands.trade.TradeCommand;
import com.lolpvp.commands.trade.TradeManager;
import com.lolpvp.perkbooks.PerkBookCommand;
import com.lolpvp.perkbooks.PerkBookManager;
import com.lolpvp.signs.SignsCommand;
import com.lolpvp.signs.SignsListener;
import com.lolpvp.signs.SignsManager;
import com.lolpvp.utils.AntiSpamBot;
import com.lolpvp.virtualchest.VirtualChest;
import com.lolpvp.virtualchest.VirtualChestListener;
import com.lolpvp.virtualchest.VirtualChestManager;
import com.lolpvp.votifier.VotesCommand;
import com.lolpvp.votifier.VotesManager;
import com.lolpvp.votifier.VotifierListener;
import com.lolpvp.weapons.ItemManager;
import com.lolpvp.weapons.classes.InvisRing;
import com.lolpvp.weapons.classes.LOLSword;
import com.lolpvp.weapons.classes.MoneyBag;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Core extends JavaPlugin implements Listener {

    public static Chat chat = null;
    public static Permission permission = null;
    //Old code
    private static Economy econ = null;
    private static Core instance;
    public MuteAll muteAll = null;
    //Votes
    @Getter
    private VotesManager votesManager = null;
    //Command signs
    @Getter
    private SignsManager signsManager = null;
    //Perkbooks
    @Getter
    private PerkBookManager perkBookManager = null;
    @Getter
    private LuckPerms luckPermsAPI;
    private ChatMethod chatmethod;
    private ChatMethod2 chatmethod2;
    private ChatFix chatFix;
    private File customConfigFile = null;
    private FileConfiguration customConfig = null;

    public static Core getInstance() {
        return instance;
    }

    private static WorldGuardPlugin getWorldGuard() {
        final Plugin plugin = Bukkit.getServer().getPluginManager()
                .getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public static boolean isPlayerInPVP(Player player) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        return set.testState(null, Flags.PVP);
    }

    public static boolean isEntityInPVP(Entity entity) {
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(entity.getLocation());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        return set.testState(null, Flags.PVP);
    }

    public static boolean canBuildHere(Player player, Location location) {
        if (location == null) {
            return true;
        }

        final WorldGuardPlugin wg = getWorldGuard();
        return wg == null || wg.createProtectionQuery().testBlockPlace(player, location, (location.getBlock().getType() == Material.AIR ? player.getItemInHand().getType() : location.getBlock().getType()));
    }

    public static boolean canBuildHere(Player player, Block block) {
        if (block == null) {
            return true;
        }

        final WorldGuardPlugin wg = getWorldGuard();
        final Location location = block.getLocation();
        return wg == null || wg.createProtectionQuery().testBlockPlace(player, location, (location.getBlock().getType() == Material.AIR ? player.getItemInHand().getType() : location.getBlock().getType()));
    }

    public static boolean safeSetBlock(Player player, Block block, Material type) {
        if (!canBuildHere(player, block)) {
            return false;
        }

        block.setType(type);

        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean has(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    @Override
    public void onEnable() {
        instance = this;

        //Votes
        votesManager = new VotesManager(this);
        getServer().getPluginManager().registerEvents(new VotifierListener(this), this);

        //Command signs
        signsManager = new SignsManager(this);
        getServer().getPluginManager().registerEvents(new SignsListener(this), this);
        signsManager.loadSigns();

        //Perkbooks
        perkBookManager = new PerkBookManager();

        //Old Stuff
        ItemManager.setup(this);
        setupChat();
        muteAll = new MuteAll(this);
        chatFix = new ChatFix(this);
        chatmethod = new ChatMethod(this);
        chatmethod2 = new ChatMethod2(this);

        setupPermissions();
        PerkBookManager.setup();
        TradeManager.getInstance().setupTrades(this);
        ItemManager.getInstance().registerItems(this);
        ChestManager.setup(this);
        Chest.setup(this);
        VirtualChestManager.setup(this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        PerkBookCommand perkBookCommand = new PerkBookCommand();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        getServer().getPluginManager().registerEvents(new VirtualChestListener(), this);
        getServer().getPluginManager().registerEvents(new PowerTool(this), this);
        getServer().getPluginManager().registerEvents(chatFix, this);

        getServer().getPluginManager().registerEvents(new AntiSpamBot(this), this);
        getServer().getPluginManager().registerEvents(muteAll, this);
        getCommand("lol").setExecutor(new LOLPVPCommand());
        getCommand("regen").setExecutor(new Kits());
        getCommand("ironman").setExecutor(new Kits());
        getCommand("i2").setExecutor(new Kits());
        getCommand("nightvision").setExecutor(new Kits());
        getCommand("fireresistance").setExecutor(new Kits());
        getCommand("invis").setExecutor(new Kits());
        getCommand("chest2").setExecutor(new VirtualChest(this));
        getCommand("clearchest2").setExecutor(new VirtualChest(this));
        getCommand("pt").setExecutor(new PowerTool(this));
        getCommand("clearpt").setExecutor(new PowerTool(this));
        getCommand("lolt").setExecutor(chatFix);
        getCommand("loltd").setExecutor(chatFix);
        getCommand("loltr").setExecutor(chatFix);
        getCommand("lolto").setExecutor(chatFix);
        getCommand("lolnt").setExecutor(chatFix);
        getCommand("lolrnt").setExecutor(chatFix);
        getCommand("lolm").setExecutor(chatFix);
        getCommand("who").setExecutor(new Who(this));
        getCommand("clearchat").setExecutor(new ClearChat(this));
        getCommand("muteall").setExecutor(muteAll);
        getCommand("dispose").setExecutor(new Disposal());
        getCommand("loltrade").setExecutor(new TradeCommand());
        getCommand("lolsword").setExecutor(new LOLSword(this));
        getCommand("resetmoneybag").setExecutor(new MoneyBag(this));
        getCommand("invisring").setExecutor(new InvisRing(this));
        getCommand("votes").setExecutor(new VotesCommand(this));
        getCommand("commandsign").setExecutor(new SignsCommand(this));
        getCommand("redeem").setExecutor(perkBookCommand);
        getCommand("pgive").setExecutor(perkBookCommand);
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        ItemManager.getInstance().getItems().clear();
    }

    public ChatMethod getChatMethod() {
        return chatmethod;
    }

    public ChatMethod2 getChatMethod2() {
        return chatmethod2;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    public FileConfiguration pvpFile() {
        File playerDir = new File(getDataFolder() + File.separator + "pvpchest.yml");
        return YamlConfiguration.loadConfiguration(playerDir);
    }

    public File pvpData() {
        return new File(getDataFolder() + File.separator + "pvpchest.yml");
    }

    public FileConfiguration playerData(UUID uuid) {
        return YamlConfiguration.loadConfiguration(playerFile(uuid));
    }

    public FileConfiguration playerData(OfflinePlayer player) {
        return playerData(player.getUniqueId());
    }

    public File playerFile(UUID uuid) {
        return new File(getDataFolder() + File.separator + "userdata" + File.separator + uuid.toString() + ".yml");
    }

    public File playerFile(Player player) {
        return new File(getDataFolder() + File.separator + "userdata" + File.separator + player.getUniqueId().toString() + ".yml");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            permission = rsp.getProvider();
        }

        RegisteredServiceProvider<LuckPerms> rsp2 = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp != null) {
            luckPermsAPI = rsp2.getProvider();
            return true;
        }
        return false;
    }

    public void reloadCustomConfig() {
        File mainDirectory = new File(getDataFolder().getPath());;
        customConfigFile = new File(mainDirectory + "/MoneyBagConfig.yml");

        if (!mainDirectory.exists()) {
            mainDirectory.mkdirs();
        }

        if (!customConfigFile.exists()) {
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
            customConfig.addDefault("Main.Items.example.Percentage", "100");
            customConfig.addDefault("Main.Items.example.Name", "Example Name");
            customConfig.createSection("Main.Items.example.Commands");
            List<String> commands = getCustomConfig().getStringList("Main.Items.example.Commands");
            commands.add("/EXAMPLECOMMAND {player}");
            commands.add("/EXAMPLECOMMAND {player}");
            customConfig.set("Main.Items.example.Commands", commands);
            customConfig.addDefault("Main.Broadcast Message", "&a{player} just looked in their Money Bag and received a random prize. A {item}! Money Bags are available for top donators, if you think you have what it takes visit buy.lolpvp.com and work your way up to the top spot now!");
            customConfig.options().copyDefaults(true);
            try {
                customConfig.save(customConfigFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }

    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

}