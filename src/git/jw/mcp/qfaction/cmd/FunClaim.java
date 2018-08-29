package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qfaction.data.ReginData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FunClaim extends BaseCmd {
    public FunClaim(JavaPlugin plugin){
        super(plugin);
    setAdminOnly(false);
    setPlayerOnly(true);
    setArgLength((short) 0);
}
    @Override
    protected String getName() {
        return "funclaim";
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
        String world=p.getLocation().getWorld().getName();
        if(!QFaction.claimworlds.contains(world)){
            p.sendMessage("§c您不可以在这个世界领取领地");
            return true;
        }
        ReginData reginData=QFaction.instance.getReginData(world);
        Chunk c=p.getLocation().getChunk();
        if(reginData.getClaimed(c)==null||!reginData.getClaimed(c).equalsIgnoreCase(factionData.getLeader())){
            p.sendMessage("§c您不能操作这块地");
            return true;
        }
        p.sendMessage("§a成功取消了领地");
        reginData.removeClaimRegin(c);
        return true;
    }
}
