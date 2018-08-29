package git.jw.mcp.qfaction.cmd;

import git.jw.mcp.qfaction.QFaction;
import git.jw.mcp.qfaction.data.FactionData;
import git.jw.mcp.qfaction.data.PlayerData;
import git.jw.mcp.qwzd.cmd.BaseCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FCreate extends BaseCmd {
    public FCreate(JavaPlugin plugin){
        super(plugin);
        setAdminOnly(false);
        setPlayerOnly(true);
        setArgLength((short) 1);
    }
    @Override
    protected String getName() {
        return "fcreate";
    }

    @Override
    public boolean exec(CommandSender commandSender, String[] strings) {
        Player p= (Player) commandSender;
        QFaction qf=QFaction.instance;
       PlayerData data= QFaction.instance.getPlayerData(p);
        if(data.getFaction()!=null){
            p.sendMessage("§c您已拥有了派系");
            return true;
        }
       if(!data.hasEnoughPower(QFaction.createCost)){
           p.sendMessage("§c您拥有"+data.getPower()+"点能量 创建帮派需要"+QFaction.createCost+"点能量");
           return true;
       }
        data.addPower(-QFaction.createCost);
        FactionData fd=new FactionData(p.getName());
        data.setFaction(fd.getLeader());
        qf.loadFaction(fd);
        fd.setCustomName(strings[0]);
        p.sendMessage("§a派系创建成功");
        return true;
    }
}
