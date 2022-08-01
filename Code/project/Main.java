//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Code.project;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    String path = new String();
    static String oneLine = new String();
    static String check = new String();
    static String playerline = new String();
    Thread thread = new Thread(new RunnableImpl());
    String httpUrl = "http://api.tianapi.com/ipquery/index";
    String jsonResult = null;
    Gson Result = null;
    JsonObject res;

    public Main() {
    }

    public void onEnable() {
        this.getLogger().info("Stone跨服聊天插件已启用");
        this.getLogger().info("onEnable has been invoked!");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void onLoad() {
        this.getLogger().info("Stone跨服聊天插件加载中");
        this.path = System.getProperty("user.dir");
        File file = new File((new File(this.path)).getParent() + "\\Message.txt");
        this.path = file.getPath();
        this.getLogger().info("你设置的检测文件是:" + this.path);
        if (!file.exists()) {
            this.getLogger().info("[WARN]你设置的检测文件不存在!自动为您创建");

            try {
                file.createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
                this.getLogger().info("[ERROR]创建失败！");
            }
        }

        this.thread.start();
    }

    public void onDisable() {
        this.getLogger().info("Stone跨服聊天插件已被卸载");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
        }

        return super.onCommand(sender, command, label, args);
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;

            while((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }

            reader.close();
            result = sbf.toString();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return result;
    }

    public static String beanToJSONString(Object bean) {
        return (new Gson()).toJson(bean);
    }

    public static Object JSONToObject(String json, Class beanClass) {
        Gson gson = new Gson();
        Object res = gson.fromJson(json, beanClass);
        return res;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player players = event.getPlayer();
        String messages = event.getMessage();

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.path, false)));
            out.write("<" + players.getName() + "> " + messages);
            playerline = "<" + players.getName() + "> " + messages;
            out.close();
        } catch (IOException var5) {
            System.out.print("文件错误");
        }

    }

    public class Person {
        private int code;
        private String msg;
        private Personlist newslist;
        Gson Result;

        public Person(int code, String msg, Personlist newslist) {
            this.code = code;
            this.msg = msg;
            this.newslist = newslist;
        }

        public int getCode() {
            return this.code;
        }

        public String getMsg() {
            return this.msg;
        }

        public Personlist getnewlist() {
            return this.newslist;
        }

        public String toString() {
            return "Person{code='" + this.code + '\'' + ", msg='" + this.msg + '\'' + ", newslist=" + this.newslist + '}';
        }
    }


    class RunnableImpl implements Runnable {
        RunnableImpl() {
        }

        public void run() {
            this.doSomething();
        }

        private void doSomething() {
            while(true) {
                try {
                    Thread.sleep(1000L);
                    File file = new File(Main.this.path);
                    FileInputStream readIn = new FileInputStream(file);
                    InputStreamReader read = new InputStreamReader(readIn, "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(read);
                    Main.oneLine = bufferedReader.readLine();
                    if (!Main.check.equals(Main.oneLine) && !Main.playerline.equals(Main.oneLine)) {
                        Main.check = Main.oneLine;
                        Main.playerline = Main.oneLine;
                        System.out.println(Main.oneLine);
                        Bukkit.broadcastMessage(Main.oneLine);
                    }

                    read.close();
                } catch (Exception var5) {
                    System.out.println("读取文件内容出错");
                    var5.printStackTrace();
                }
            }
        }
    }
}
