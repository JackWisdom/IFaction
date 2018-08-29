package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FHome extends BaseCmd {
    public FHome(JavaPlugin plugin) {
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 0);
    }

    @Override
    protected String getName() {
        return "fhome";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        QFaction qf=QFaction.instance;
        PlayerData data= QFaction.instance.getPlayerData(p);
        if(data.getFaction()==null){
            p.sendMessage("§c您没有派系");
            return true;
        }
        Location loc=qf.getFaction(data.getFaction()).getSpawn();
        if(loc==null){
            commandSender.sendMessage("§c您的帮派未设置出生点");
            return true;
        }
        p.teleport(loc);
        return true;
    }
}
