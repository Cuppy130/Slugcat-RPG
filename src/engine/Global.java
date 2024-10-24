package engine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.*;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import engine.actionListener.KeyListener;
import engine.entity.Player;
import engine.network.FileDownloader;
import gui.GLFWButton;

public class Global {
    public static final String APPDATA = getAppDataFolder();
    public static final String PROFILES = Paths.get(APPDATA, "profiles").toString();
    public static final String ASSETS = Paths.get(APPDATA, "assets").toString();
    public static final int PROTOCOl = 0;
    public static final int VERSION = 0;
    public static final int maxPlayersOnScreen = 1024;
    public static final float scale = 128;
    public static final boolean autosave = true;
    public static long window = -1;
    public static int whoAmI = -1;
    public static int partyID = 0;
    public static Player player;
    public static int saveint = 0;
    public static boolean DebugMenu = true;
    public static boolean development = false;
    public static boolean ignoreSelf = true;
    public static List<Player> players = new ArrayList<>();
    public static GLFWButton button;
    // plr is specific to this function because bitch cant even work properly without it
    public static ConcurrentLinkedQueue<Runnable> mainThreadTaskQueue = new ConcurrentLinkedQueue<>();
    public static PlayerCharacter playerCharacter;

    public static String getAppDataFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        String directory = ".rainywilds";
        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData != null) {
                return Paths.get(appData, directory).toString();
            } else {
                return Paths.get(System.getProperty("user.home"), "AppData", "Roaming", directory).toString();
            }
        }
        else {
            return Paths.get(System.getProperty("user.home"), directory).toString();
        }
    }

    public static void init(){
        player = new Player();
        playerCharacter = new PlayerCharacter();
    }
    
    public static void checkFolder(String folder){
        Path path = Paths.get(folder);
        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean checkFile(String file) {
        return Files.exists(Paths.get(file));
    }
    
    public static void update() {
        if(KeyListener.isKeyPressed(GLFW_KEY_A)){
            player.direction = 0;
            player.isMoving = true;
            playerCharacter.model.t = playerCharacter.left;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)){
            player.direction = 2;
            player.isMoving = true;
            playerCharacter.model.t = playerCharacter.right;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_W)){
            player.direction = 1;
            player.isMoving = true;
            playerCharacter.model.t = playerCharacter.up;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)){
            player.direction = 3;
            player.isMoving = true;
            playerCharacter.model.t = playerCharacter.down;
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_W)&&KeyListener.isKeyPressed(GLFW_KEY_A)){
            player.direction = .5f;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)&&KeyListener.isKeyPressed(GLFW_KEY_D)){
            player.direction = 2.5f;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_W)&&KeyListener.isKeyPressed(GLFW_KEY_D)){
            player.direction = 1.5f;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_S)&&KeyListener.isKeyPressed(GLFW_KEY_A)){
            player.direction = 3.5f;
        }
        if(!KeyListener.isKeyPressed(GLFW_KEY_W)&&!KeyListener.isKeyPressed(GLFW_KEY_S)&&!KeyListener.isKeyPressed(GLFW_KEY_A)&&!KeyListener.isKeyPressed(GLFW_KEY_D)){
            player.isMoving = false;
        }
    }

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    public static void enableResize(boolean t){
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, t ? GLFW_TRUE : GLFW_FALSE);
    }

    public static void downloadResources() {
        // check if these folders exist
        checkFolder(Global.APPDATA);
        checkFolder(Global.PROFILES);
        checkFolder(Global.ASSETS);
        //check if the folders exist, if not then create them
        checkFolder(Paths.get(ASSETS, "fonts").toString());
        checkFolder(Paths.get(ASSETS, "playerCharacter").toString());
        checkFolder(Paths.get(ASSETS, "sounds").toString());
        checkFolder(Paths.get(ASSETS, "tilesets").toString());

        //fonts
        if(!checkFile(Paths.get(ASSETS, "fonts", "andy.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298898163847135294/andy-1024x1024.png?ex=671b3cae&is=6719eb2e&hm=487bf39a77ae1341beaa9549eff569fd195a698d3044be5d85118607cc05c356&", Paths.get(ASSETS, "fonts", "andy.png").toString());
        }
        // if(!checkFile(Paths.get(ASSETS, "fonts", "andy.ini").toString())){
        //     FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298966347279241256/andy.ini?ex=671b7c2e&is=671a2aae&hm=cd1fe97f9e0707aefc8064e941d665733277c3d9defe1f5d4ac069ff29d56424&", Paths.get(ASSETS, "fonts", "andy.ini").toString());
        // }

        //playerCharacter
        if(!checkFile(Paths.get(ASSETS, "playerCharacter", "left.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298883947283091508/left.png?ex=671b2f71&is=6719ddf1&hm=f12418eb6cced3738acae0062bd0f319c9a701a2e89b23e082c937ac5ad42a93&", Paths.get(ASSETS, "playerCharacter", "left.png").toString());
        }
        if(!checkFile(Paths.get(ASSETS, "playerCharacter", "up.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298883946494562374/up.png?ex=671b2f70&is=6719ddf0&hm=658486e83f554892c438de870bebc960bb9275af3fa3ad71e5f4c041cadde264&", Paths.get(ASSETS, "playerCharacter", "up.png").toString());
        }
        if(!checkFile(Paths.get(ASSETS, "playerCharacter", "down.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298883946825777212/down.png?ex=671b2f71&is=6719ddf1&hm=f5c02c17d92f1dd06475e23a1f775a871f61d2c82aae4867d23d538c6beb8120&", Paths.get(ASSETS, "playerCharacter", "down.png").toString());
        }
        if(!checkFile(Paths.get(ASSETS, "playerCharacter", "right.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298883947467509780/right.png?ex=671b2f71&is=6719ddf1&hm=c399a307257d6de3c5399cf7f222ccebac52aee23ac79d694cb9e724c07231b6&", Paths.get(ASSETS, "playerCharacter", "right.png").toString());
        }
        
        //tilesets
        if(!checkFile(Paths.get(ASSETS, "tilesets", "0.png").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298883947870027786/0.png?ex=671b2f71&is=6719ddf1&hm=75188ff3ba4391636a845526e598c719ecf12f9a6c87c9c4154237e3f16ca3ae&", Paths.get(ASSETS, "tilesets", "0.png").toString());
        }

        //sounds
        if(!checkFile(Paths.get(ASSETS, "sounds", "WakeUp.ogg").toString())){
            FileDownloader.downloadFile("https://cdn.discordapp.com/attachments/1298883702700642344/1298889343968215081/WakeUp.ogg?ex=671b3477&is=6719e2f7&hm=453f075b15e8af9b57725ec5238aed84f6da635dff867e16d2cd2e22afd7612b&", Paths.get(ASSETS, "sounds", "WakeUp.ogg").toString());
        }
    }

    public static void openFolder(String path){
        File folder = new File(path);
        
        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            try {
                Desktop.getDesktop().open(folder);
                System.out.println("Folder opened: " + path);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Couldnt open folder \""+path+"\"");
            }
        } else {
            System.err.println("The folder does not exist or is not a directory: " + path);
        }
    }
}
