package git.jw.mcp.qfaction.data;

import git.jw.mcp.qfaction.QFaction;
import org.bukkit.Chunk;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReginData implements Serializable {
    public HashMap<String,String> reginData;
    //每一个世界都要有不同的 config里也要有可以领取的地
    //删除帮派的时候记得通过遍历values来删除
    public String world;
    public ReginData(String world){
        reginData=new HashMap<>();
        this.world=world;
    }
    public String getWorld(){
        return world;
    }
    public void addClaimRegin(String faction,Chunk chunk){
        reginData.put(generKey(chunk),faction);
    }
    public void removeClaimRegin(Chunk chunk){
        reginData.remove(generKey(chunk));
    }
    public boolean isClaimed(Chunk chunk){

    return reginData.get(generKey(chunk))!=null;
    }
    public String getClaimed(Chunk chunk){
        return reginData.get(generKey(chunk));
    }
    public String generKey(Chunk chunk){
        return chunk.getX()+":"+chunk.getZ();
    }
    public void removeClaims(String faction){
        ArrayList<String> list=new ArrayList<>();
        for(Map.Entry<String,String> s:reginData.entrySet()){
            if (!s.getValue().equalsIgnoreCase(faction)) {
                QFaction.debug("foundvusless" + s.getKey() + "|" + s.getValue());

                continue;
            }
                list.add(s.getKey());
                QFaction.debug("found" + s.getKey() + "|" + s.getValue());

        }
        for(String s:list){
            reginData.remove(s);
            QFaction.debug("removing"+s+"|"+(reginData.get(s)==null));
        }
    }
    public File getFile(){
        return new File(QFaction.instance.getDataFolder(),"regins/"+world+".json");
    }
}
