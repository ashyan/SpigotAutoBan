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

import static org.bukkit.BanList.Type.NAME;

public class AutoBan extends JavaPlugin {
    JDA jda;
    @Override
    public void onEnable() {
        String token = "TOKEN_HERE";
        try {
            jda = JDABuilder.createLight(token) // slash commands don't need any intents
                    .addEventListeners(new DiscordBot())
                    .build();
        } catch (javax.security.auth.login.LoginException exception) {

        }
        Logger logger = Bukkit.getLogger();
        MyListener serverListener = new MyListener();
        serverListener.jda = jda;
        getServer().getPluginManager().registerEvents(serverListener, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                BanList banned = Bukkit.getBanList(NAME);
                for(BanEntry player : banned.getBanEntries()) {
                    String playerName = player.getTarget();
                    LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime banDate = player.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if(ChronoUnit.DAYS.between(banDate, now) >= 1) {
                        banned.pardon(playerName);
                        logger.info("Unbanned " + playerName);
                        Guild guild = jda.getGuildsByName("locked armor's server", true).get(0);
                        TextChannel channel = guild.getTextChannelsByName("bot-chat", true).get(0);
                        channel.sendMessage(playerName + " has been unbanned from the server!").queue();
                    }
                }
            }
        }, 0, 20 * 5);
    }
}
