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
    @Override
    public void onEnable() {
        data = new DataManager(this);
        String token = data.getConfig().get("bot-token").toString();
        String serverName = data.getConfig().get("server-name").toString();
        String chatName = data.getConfig().get("chat-name").toString();
        switch(data.getConfig().get("time-type").toString()) {
            case "day":
                timeType = ChronoUnit.DAYS;
                break;
            case "second":
                timeType = ChronoUnit.SECONDS;
                break;
            case "hour":
                timeType = ChronoUnit.HOURS;
                break;
            default:
                timeType = ChronoUnit.DAYS;
        }
        timeLength = parseInt(data.getConfig().get("time-length").toString());


        try {
            jda = JDABuilder.createLight(token) // slash commands don't need any intents
                    .addEventListeners(new DiscordBot())
                    .build().awaitReady();
        } catch (javax.security.auth.login.LoginException | InterruptedException exception) {

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
                    if(timeType.between(banDate, now) >= timeLength) {
                        banned.pardon(playerName);
                        logger.info("Unbanned " + playerName);
                        Guild guild = jda.getGuildsByName(serverName, true).get(0);
                        TextChannel channel = guild.getTextChannelsByName(chatName, true).get(0);
                        channel.sendMessage(playerName + " has been unbanned from the server!").queue();
                    }
                }
            }
        }, 0, 20 * 5);
    }
}
