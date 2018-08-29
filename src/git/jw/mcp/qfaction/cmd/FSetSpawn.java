package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FSetSpawn extends BaseCmd {
    public FSetSpawn(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 0);
    }
    @Override
    protected String getName() {
        return "fsetspawn";
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
        FactionData factionData=QFaction.instance.getFaction(data.getFaction());
        if(!factionData.getLeader().equalsIgnoreCase(p.getName())){
            p.sendMessage("§c您并不是帮派帮主");
            return true;
        }
        factionData.setSpawn(p.getLocation());
        p.setBedSpawnLocation(p.getLocation());
        return true;
    }
}
