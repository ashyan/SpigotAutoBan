package ashyan;

import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.time.temporal.ChronoUnit;
public class AutoBan extends JavaPlugin {
    JDA jda;
    DataManager data;
    ChronoUnit timeType;
    int timeLength;

    DiscordBot discordBot;
    @Override
    public void onEnable() {
        data = new DataManager(this);

        BanManager banManager = new BanManager(data);
        discordBot = new DiscordBot(data, banManager);

        MyListener serverListener = new MyListener(discordBot, banManager);
        getServer().getPluginManager().registerEvents(serverListener, this);

        this.getCommand("revive").setExecutor(new CommandRevive(banManager, data, discordBot));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                banManager.updateBanList(discordBot);
            }
        }, 0, 20 * 5);
    }
}
