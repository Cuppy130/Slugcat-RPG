package main;

import engine.Global;
import engine.Model;
import engine.SaveFile;
import engine.Scheduler;
import engine.TextureUtils;

import org.lwjgl.opengl.GL;
import engine.actionListener.KeyListener;
import engine.actionListener.MouseListener;
import engine.actionListener.WindowListener;
import engine.entity.Player;
import engine.network.Network;
import gui.Color;
import gui.FontManager;
import gui.GameSave;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static boolean closeWindow = false;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static FontManager font;
    private static int tileset;
    private Model chunk;

    //delta frametime
    private long frameTimeA = System.currentTimeMillis();
    private long frameTimeB = System.currentTimeMillis();
    private float delta = 0;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-debug")) {
            Global.development = true;
            Global.DebugMenu = true;
        }
        Global.downloadResources();
        new Main().run();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        Global.player = SaveFile.unpackData(Global.saveint);
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        long window = glfwCreateWindow(WIDTH, HEIGHT, "Rainy Wilds", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        Global.window = window;
        
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        System.out.println("OpenGL version: " + glGetString(GL_VERSION));
        
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
    
        glViewport(0, 0, WIDTH, HEIGHT);
    
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
        glEnable(GL_TEXTURE_2D);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - WIDTH) / 2, (videoMode.height() - HEIGHT) / 2);
        
        font = new FontManager();
        Network.start();

        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetKeyCallback(window, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(window, WindowListener::resizeCallback);
        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);

        tileset = TextureUtils.loadTexture(Global.ASSETS + "\\tilesets\\0.png");

        Global.init();
        chunk = new Model(new float[]{
            0, 0,
            Global.scale * 16, 0,
            0, Global.scale * 16,
            Global.scale * 16, Global.scale * 16
        }, new float[]{0, 0, 0, 1, 1, 0, 1, 1}, tileset);

        Network.connect();
    }

    private float getGlobalPos(float pos, float chunk){
        return chunk * 16 + pos;
    }

    private void loop() {
        Scheduler.start();
        Global.player = SaveFile.unpackData(Global.saveint);
        Player player = Global.player;
        glClearColor(.5f, .75f, 1, 1);
        while (!glfwWindowShouldClose(Global.window)) {
            if(closeWindow)break;

            while (!Global.mainThreadTaskQueue.isEmpty()) {
                Runnable task = Global.mainThreadTaskQueue.poll();
                if (task != null) {
                    task.run();
                    System.out.println("Ran task, "+Global.mainThreadTaskQueue.size() +" left!");
                    System.out.println(Global.players.size());
                }
            }

            frameTimeB = frameTimeA;
            frameTimeA = System.currentTimeMillis();

            delta = (float) (frameTimeA - frameTimeB) / 60;

            Global.update();
            player.update(delta);
            
            glClear(GL_COLOR_BUFFER_BIT);

            // background
            for(int x = 0; x < 3; x++){
                for(int y = 0; y < 3; y++){
                    glPushMatrix();
                    glTranslatef(-getGlobalPos(player.x, player.chunkX) * Global.scale+WIDTH/2, -getGlobalPos(player.y, player.chunkY)*Global.scale+HEIGHT/2, 0);
                    glTranslatef(x * Global.scale * 16 - 8 * 8, y * Global.scale * 16 - 8 * 8, 0);
                    chunk.render();
                    glPopMatrix();
                }
            }

            //foreground
            synchronized(Global.players){
                for (Player plr : Global.players) {
                    glPushMatrix();
                    glTranslatef(-getGlobalPos(player.x, player.chunkX) * Global.scale+WIDTH/2, -getGlobalPos(player.y, player.chunkY)*Global.scale+HEIGHT/2, 0);
                    glTranslatef(getGlobalPos(plr.x, plr.chunkX) * Global.scale, getGlobalPos(plr.y, plr.chunkY) * Global.scale, 0);
                    plr.update(delta);
                    Global.playerCharacter.draw(plr);
                    font.write((float)((-plr.name.length()/2)*24), (float)(-24*4), plr.name, 24, new Color(0, 0, 0));
                    glPopMatrix();
                }
            }


            //gui
            if(Global.DebugMenu == true){
                font.write(0, 0, "Position: (" + getGlobalPos(player.x, player.chunkX) + ", " + getGlobalPos(player.y, player.chunkY) + ")", 12, new Color(0, 0, 0));
                font.write(0,12, "Version: "+Global.VERSION, 12, new Color(0, 0, 0));
                font.write(0,24, "PlayerColor: "+Global.player.color, 12, new Color(0, 0, 0));
                font.write(0, 36, "Players: "+(Global.players.size()+1), 12, new Color(0, 0, 0));
                font.write(0, 48, "ID: "+Global.whoAmI, 12, new Color(0, 0, 0));
            }

            glPushMatrix();
            glTranslatef(WIDTH / 2,HEIGHT / 2, 0);
            Global.playerCharacter.draw(player);
            font.write((-player.name.length()/2)*24, -24*5, player.name, 24, new Color(255, 0, 255));
            font.write((-(player.health + "/" + player.maxHealth).length()/2)*24, -24*5+24, player.health + "/" + player.maxHealth, 24, new Color(255, 0, 255));

            glPopMatrix();

            glfwSwapBuffers(Global.window);
            glfwPollEvents();
        }
    }

    public static void cleanup() {
        closeWindow = true;
        Scheduler.stop();
        Network.stop();
        glfwDestroyWindow(Global.window);
        glfwTerminate();
        GameSave.Show();
    }
}