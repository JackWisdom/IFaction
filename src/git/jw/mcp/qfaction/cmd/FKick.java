package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FKick extends BaseCmd {
    public FKick(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 1);
    }
    @Override
    protected String getName() {
        return "fkick";
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
        if(!factionData.isAdmin(p.getName())){
            p.sendMessage("§c您并不是帮派管理员");
            return true;
        }
        if(qf.getPlayerData(strings[0])==null){
            p.sendMessage("§c对方不在线");
            return true;
        }
        if(qf.getPlayerData(strings[0]).getFaction()==null||!qf.getPlayerData(strings[0]).getFaction().equalsIgnoreCase(data.getFaction())){
            p.sendMessage("§c对方并不是你工会的成员");
            return true;
        }
        if(factionData.isAdmin(strings[0])){
            p.sendMessage("§c无法T出工会管理员 请将其撤职后重试");
            return true;
        }
        Bukkit.broadcastMessage("§c"+p.getName()+"把"+strings[0]+"T出了工会!");
        factionData.removeCoLeader(strings[0]);
        qf.getPlayerData(strings[0]).setFaction(null);
        return true;
    }
}
