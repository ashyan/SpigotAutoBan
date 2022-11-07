package ashyan;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.BanList;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.EnumSet;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static org.bukkit.BanList.Type.NAME;

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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                banManager.updateBanList(discordBot);
            }
        }, 0, 20 * 5);
    }
}
