package ashyan;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
public class CommandRevive implements CommandExecutor {
    BanManager banManager;
    int removalAmount;
    DiscordBot discordBot;
    public CommandRevive(BanManager banManager, DataManager data, DiscordBot discordBot) {
        this.banManager = banManager;
        this.discordBot = discordBot;
        this.removalAmount = parseInt(data.getConfig().get("diamond-removal-amount").toString());
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if(args.length != 1) {
                player.sendMessage("Revive takes 1 argument: playerName. Okay?");
                return false;
            }
            String playerToUnban = args[0];
            if(banManager.isBanned(playerToUnban) == false) {
                player.sendMessage("Player " + playerToUnban + " is not found on the banned list");
                return false;
            }

            PlayerInventory inventory = player.getInventory();
            for(ItemStack is: inventory) {
                if(is != null && is.getType() == Material.DIAMOND) {
                    if(is.getAmount() >= removalAmount) {
                        inventory.removeItem(new ItemStack(Material.DIAMOND, removalAmount));
                        banManager.unBan(playerToUnban);
                        this.discordBot.sendMesageToChannel(player.getDisplayName() + " has sacrificed " + removalAmount + " diamonds to unban " + playerToUnban + "!");
                        return true;
                    } else {
                        player.sendMessage("A stack of " + removalAmount + " diamond(s) was not found in your inventory!");
                        return false;
                    }
                }
            }
        }

        return false;
    }
}
