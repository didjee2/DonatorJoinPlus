package com.dbsoftwares.djp.spigot.commands.subcommands.toggle;

import com.dbsoftwares.djp.spigot.DonatorJoinPlus;
import com.dbsoftwares.djp.spigot.commands.DJSubCommand;
import com.dbsoftwares.djp.spigot.utils.SpigotUtils;
import com.dbsoftwares.djp.user.User;
import com.dbsoftwares.djp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ToggleSoundSubCommand extends DJSubCommand
{

    public ToggleSoundSubCommand()
    {
        super( "togglesound", 0, 1 );
    }

    @Override
    public String getUsage()
    {
        return "/djp togglesound [player]";
    }

    @Override
    public String getPermission()
    {
        return "donatorjoinplus.togglesound";
    }

    @Override
    public void onExecute( Player player, String[] args )
    {
        if ( args.length > 1 )
        {
            player.sendMessage( Utils.getMessage( "sound.toggle.usage" ) );
            return;
        }
        if ( args.length == 1 )
        {
            onExecute( (CommandSender) player, args );
            return;
        }
        final User user = DonatorJoinPlus.i().getUser( player.getUniqueId() );
        final boolean toggled = !user.isSoundToggled();

        user.setSoundToggled( toggled );
        DonatorJoinPlus.i().getStorage().toggleSound( player.getUniqueId(), toggled );
        player.sendMessage( Utils.getMessage( "sound.toggle." + (toggled ? "disabled" : "enabled") ) );
    }

    @Override
    public void onExecute( CommandSender sender, String[] args )
    {
        if ( args.length != 1 )
        {
            sender.sendMessage( Utils.getMessage( "sound.toggle.console-usage" ) );
            return;
        }
        if ( !sender.hasPermission( "donatorjoinplus.togglesound.other" ) )
        {
            sender.sendMessage( Utils.getMessage( "no-perm" ) );
            return;
        }
        final String playerName = args[0];
        final UUID uuid = SpigotUtils.getUuid( args[0] );
        if ( uuid == null )
        {
            sender.sendMessage( Utils.getMessage( "never-joined" ) );
            return;
        }
        final Player target = Bukkit.getPlayer( uuid );
        final boolean toggled;

        if ( target == null )
        {
            toggled = !DonatorJoinPlus.i().getStorage().isSoundToggled( uuid );
        }
        else
        {
            final User user = DonatorJoinPlus.i().getUser( uuid );

            if ( user != null )
            {
                toggled = !user.isSoundToggled();
                user.setSoundToggled( toggled );
            }
            else
            {
                toggled = !DonatorJoinPlus.i().getStorage().isSoundToggled( uuid );
            }
            target.sendMessage( Utils.getMessage( "sound.toggle." + (toggled ? "disabled" : "enabled") ) );
        }
        DonatorJoinPlus.i().getStorage().toggleSound( uuid, toggled );

        sender.sendMessage(
                Utils.getMessage( "sound.toggle." + (toggled ? "disabled" : "enabled") + "-other" )
                        .replace( "{player}", args[0] )
        );
    }

    @Override
    public List<String> getCompletions( CommandSender sender, String[] args )
    {
        return getPlayerCompletions( args );
    }

    @Override
    public List<String> getCompletions( Player player, String[] args )
    {
        return getCompletions( (CommandSender) player, args );
    }
}
