package git.jw.mcp.qfaction.data;

import git.jw.mcp.qfaction.QFaction;

import javax.annotation.Nullable;
import java.io.File;
import java.io.Serializable;

public class PlayerData implements Serializable {
    public int power;
    public String faction;
    public String name;
    public int onlineCount;
    public PlayerData(String name){
        this.name=name;
        this.faction=null;
        this.power=0;
        onlineCount=0;
    }
    public void tickOnline(){
        onlineCount=onlineCount+1;
        if(onlineCount>=12){
            onlineCount=0;
            //没5分钟tick一次 一个小时添加一点能量
            addPower(+1);
        }
    }
    public String getName(){
        return name;
    }
    @Nullable
    public String getFaction(){
        return faction;
    }
    public void setFaction(String faction){
        this.faction=faction;
    }
    public int getPower(){
        return power;
    }
    public void setPower(int newp){
        this.power=newp;
    }
    public void addPower(int ammount){
        int power=getPower()+ammount;
        if(power<=0){
            power=0;
        }
        setPower(power);
    }
    public File getFile(){
        return new File(QFaction.instance.getDataFolder(),"players/"+getName()+".json");
    }
    public boolean hasEnoughPower(int ammount){
        return getPower()>=ammount;
    }
}
