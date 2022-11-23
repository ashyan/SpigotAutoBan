package ashyan;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import static org.bukkit.BanList.Type.NAME;

public class BanManager {
    DataManager data;

    int timeLength;
    ChronoUnit timeType;
    Duration timeDuration;
    public BanManager(DataManager data) {

        this.data = data;
        timeLength = parseInt(data.getConfig().get("time-length").toString());
        switch(data.getConfig().get("time-type").toString()) {
            case "minute":
                timeType = ChronoUnit.MINUTES;
                timeDuration = Duration.ofMinutes(timeLength);
                break;
            case "second":
                timeType = ChronoUnit.SECONDS;
                timeDuration = Duration.ofSeconds(timeLength);
                break;
            case "hour":
                timeType = ChronoUnit.HOURS;
                timeDuration = Duration.ofHours(timeLength);
                break;
            case "day":
                timeType = ChronoUnit.DAYS;
                timeDuration = Duration.ofDays(timeLength);
                break;
            default:
                timeType = ChronoUnit.DAYS;
                timeDuration = Duration.ofDays(timeLength);
        }
    }
    public LocalDateTime getUnbanDate(LocalDateTime date) {
        return date.plus(timeDuration);
    }
    public LocalDateTime getUnbanDate(BanEntry playerEntry) {
        return this.getUnbanDate(playerEntry.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    public LocalDateTime getUnbanDate(String playerName) {
        BanList banned = Bukkit.getBanList(NAME);
        for(BanEntry playerEntry : banned.getBanEntries()) {
            if (playerEntry.getTarget().equals(playerName)) {
                return this.getUnbanDate(playerEntry.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
        }
        return LocalDateTime.MAX;
    }

    public void updateBanList(DiscordBot discordBot) {
        Logger logger = Bukkit.getLogger();
        BanList banned = Bukkit.getBanList(NAME);
        for(BanEntry player : banned.getBanEntries()) {
            String playerName = player.getTarget();
            LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime banDate = player.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if(timeType.between(banDate, now) >= timeLength) {
                banned.pardon(playerName);
                logger.info("Unbanned " + playerName);
                if(data.readNameLink(playerName) != "none"){
                    String message = "@" + data.readNameLink(playerName) + " you've been unbanned!";
                    discordBot.sendMesageToChannel(message);
                } else {
                    String message = playerName + " has been unbanned from the server!";
                    discordBot.sendMesageToChannel(message);
                }


            }
        }
    }

    public void unBan(String playerName) {
        BanList banned = Bukkit.getBanList(NAME);
        banned.pardon(playerName);
    }

    public String checkUnban(String username)
    {
        BanList banned = Bukkit.getBanList(NAME);
        for (BanEntry player : banned.getBanEntries()) {
            if(player.getTarget().equals(username)) {
                LocalDateTime unBanTime = this.getUnbanDate(player);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, KK:mma");
                String unbanStr = unBanTime.format(formatter);
                String message = username + " will be unbanned at " + unbanStr;
                return message;
            }
        }

        return "Could not find " + username + " in ban list";
    }

    public boolean isBanned(String username) {
        BanList banned = Bukkit.getBanList(NAME);
        for (BanEntry player : banned.getBanEntries()) {
            if(player.getTarget().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public void onDeath(PlayerDeathEvent event) {
        Logger logger = Bukkit.getLogger();
        BanList banned = Bukkit.getBanList(NAME);
        LocalDateTime unBanDate = this.getUnbanDate(LocalDateTime.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, KK:mma");
        String unbanStr = unBanDate.format(formatter);
        BanEntry entry = banned.addBan(event.getEntity().getName(), "You will be unnbanned at " + unbanStr, null, null);
        logger.info("Banned " + event.getEntity().getName());
        event.getEntity().kickPlayer("erm you died");
    }
}
