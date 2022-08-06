package Code.project;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.io.InputStreamReader;

public class Main extends JavaPlugin implements Listener {
    String servername = new String();
    String path = new String();
    int sleeptime = 0;
    static long time;
    static long playertime;
    static long check_time;
    static String oneLine = new String();
    static String outLine = new String();
    public Server server = Bukkit.getServer();
    Thread thread = new Thread(new RunnableImpl());
    public void readfile(){
        if(getConfig().getString("servername")==null) {
            servername="[Minecraft Server]";
            getConfig().set("servername",servername);
            saveConfig();
        }
        servername=getConfig().getString("servername");
        System.out.println("你设置的ServerName是:" + servername);
        if(getConfig().getString("path")==null) {
            path = System.getProperty("user.dir");
            File file = new File(new File(path).getParent() + "\\Message.txt");
            path = file.getPath();
            getConfig().set("path",path);
            saveConfig();
        }
        path=getConfig().getString("path");
        System.out.println("你设置的Path路径是:" + path);
        if(getConfig().getInt("sleeptime")==0) {
            sleeptime = 1000;
            getConfig().set("sleeptime",sleeptime);
            saveConfig();
        }
        sleeptime=getConfig().getInt("sleeptime");
        System.out.println("你设置的SleepTime是:" + sleeptime);
        saveConfig();
        System.out.println("Config.yml已保存");
        File file = new File(getConfig().getString("path"));
        if(!file.exists()){
            System.out.println("[WARN]你设置的检测文件不存在!自动为您创建");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[ERROR]创建失败！");
            }
        }
    }
    class RunnableImpl implements Runnable {
        @Override
        public void run() {
            doSomething();
        }
        private void doSomething(){
            while (true){
                try {
                    Thread.sleep(sleeptime);
                    File file = new File(path);
                    FileInputStream readIn = new FileInputStream(file);
                    InputStreamReader read = new InputStreamReader(readIn, "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(read);
                    oneLine = bufferedReader.readLine();
                    playertime = Long.parseLong(oneLine.substring(0, oneLine.lastIndexOf("|")));
                    if (playertime != check_time) {
                        check_time = playertime;
                        outLine = oneLine.substring(oneLine.lastIndexOf("|")+1);
                        System.out.println(outLine);
                        Bukkit.broadcastMessage(outLine);
                    }
                    read.close();
                }
                catch (IOException e) {
                    System.out.println("读取文件内容出错");
                    e.printStackTrace();
                }
                catch (Exception e) {
                }
            }
        }
    }
    @Override
    public void onEnable() {
        System.out.println("StoneChat已启用");
        System.out.println("onEnable has been invoked!");
    }

    @Override
    public void onLoad() {
        System.out.println("StoneChat加载中");
        readfile();
        thread.start();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        System.out.println("StoneChat已被卸载");
        thread.interrupt();
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (args[0].equalsIgnoreCase("reload")) {
                onDisable();
                Bukkit.getPluginManager().enablePlugin(this);
                onEnable();
                sender.sendMessage("StoneChat已重启");
                return true;
            }else if((args[0].equalsIgnoreCase("status"))){
                sender.sendMessage("StoneChat正常运行中");
                return true;
            }
        }
        else{
            sender.sendMessage("对不起,您没有使用此命令的权限.");
        }
        return false;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player players=event.getPlayer();
        String messages = event.getMessage();
        try {
            BufferedWriter out = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(path, false)));
            time = System.currentTimeMillis();
            out.write(String.valueOf(time)+"|"+servername+"<"+players.getName()+"> "+messages);
            out.close();
            check_time=time;
        }
        catch(IOException e){
            System.out.print("文件错误");
        }
    }
}
