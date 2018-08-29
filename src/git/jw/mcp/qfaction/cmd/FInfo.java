package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FInfo extends BaseCmd {
    public FInfo(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 0);
    }//显示同盟 帮派管理员等
    @Override
    protected String getName() {
        return "finfo";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        QFaction qf=QFaction.instance;
        PlayerData data= QFaction.instance.getPlayerData(p);
        QFaction.debug((data==null)+"");
        if(data.getFaction()==null){
            p.sendMessage("§a您没有派系");
            p.sendMessage("§a您当前的能量:"+data.getPower());
            return true;
        }
        FactionData factionData=qf.getFaction(data.getFaction());
        QFaction.debug((factionData==null)+"");
        p.sendMessage("§a您当前的能量:"+data.getPower());
        p.sendMessage("§a您的帮主"+factionData.getLeader());
        p.sendMessage("§a您的副帮主"+factionData.getCoLeaders().toString());
        p.sendMessage("§a您的盟友"+factionData.getAllies().toString());
        return true;
    }
}
