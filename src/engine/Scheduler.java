package engine;

import engine.network.Network;

public class Scheduler {

    private static Thread loopThread;
    private static boolean running;

    public static void start() {
        if (running) return;  // Prevent multiple starts
        running = true;
        loopThread = new Thread(Scheduler::loop);
        loopThread.start();
    }

    private static void loop() {
        while (running) {
            try {
                Thread.sleep(1000/30);
                Network.sendPlayerPosition();
            } catch (InterruptedException e) {
                running = false;
                stop();
                Network.stop();
                System.out.println("Couldnt update server: " + e.getMessage());
            }
        }
    }

    public static void stop() {
        if (!running) return;  // Only stop if it's running
        running = false;
        if (loopThread != null) {
            try {
                loopThread.join();  // Wait for the loop to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
