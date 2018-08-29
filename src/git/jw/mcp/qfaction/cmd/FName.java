package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FName extends BaseCmd {
    public FName(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 1);
    }
    @Override
    protected String getName() {
        return "fname";
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
        if(!data.hasEnoughPower(QFaction.namecost)){
            p.sendMessage("§c您需要"+QFaction.namecost+"点能量\n当前余额:"+data.getPower());
            return true;
        }
        factionData.setCustomName(strings[0]);
        p.sendMessage("§a修改成功");
        return true;
    }
}
