package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import git.jw.mcp.qwzd.qvote.VotePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FLeave extends BaseCmd {
    public FLeave(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 0);
    }
    @Override
    protected String getName() {
        return "fleave";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        PlayerData data= QFaction.instance.getPlayerData(p);
        if(data.getFaction()==null){
            p.sendMessage("§c您没有派系");
            return true;
        }
        FactionData fd=QFaction.instance.getFaction(data.getFaction());
        if(fd.getLeader().equalsIgnoreCase(p.getName())){
            if(VotePlugin.instance.spawnFactions.contains(fd.getLeader())){
                p.sendMessage("§c您的工会为主城工会 无法删除");
                return true;
            }
            data.setFaction(null);
            data.setPower(0);
            QFaction.instance.removeFaction(fd.getLeader());
            Bukkit.broadcastMessage("§c"+p.getName()+"解散了他的工会");
            return true;
        }
        FactionData data1=QFaction.instance.getFaction(data.getFaction());
        data1.removeCoLeader(p.getName());
        data.setFaction(null);
        data.setPower(0);
        Bukkit.broadcastMessage("§c"+p.getName()+"退出了"+data.getFaction()+"的工会,系统已经清空了他的能量");
        return true;
    }
}
