package ashyan;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.time.LocalDateTime;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static org.bukkit.BanList.Type.NAME;

public class DiscordBot extends ListenerAdapter {
    JDA jda;
    DataManager data;

    TextChannel channel;
    BanManager banManager;
    public DiscordBot(DataManager data, BanManager banManager) {
        this.data = data;
        this.banManager = banManager;
        String token = data.getConfig().get("bot-token").toString();

        try {
            jda = JDABuilder.createLight(token) // slash commands don't need any intents
                    .addEventListeners(this)
                    .build().awaitReady();
        } catch (javax.security.auth.login.LoginException | InterruptedException exception) {

        }

        String serverId = data.getConfig().get("server-id").toString();
        String chatName = data.getConfig().get("chat-name").toString();

        Guild guild = jda.getGuildById(serverId);
        this.channel = guild.getTextChannelsByName(chatName, true).get(0);


        CommandListUpdateAction commands = jda.updateCommands();
        // Simple reply commands
        commands.addCommands(
                Commands.slash("check", "Checks unban date for inputted username")
                        .addOption(STRING, "username", "MC username", true)
        );

        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName())
        {
            case "check":
                checkUnban(event, event.getOption("username").getAsString()); // content is required so no null-check here
                break;

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    public void checkUnban(SlashCommandInteractionEvent event, String username)
    {
        String message = banManager.checkUnban(username);

        event.reply(message).queue();
    }

    public void sendMesageToChannel(String message) {
        channel.sendMessage(message).queue();
    }

}
