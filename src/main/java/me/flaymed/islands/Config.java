package me.flaymed.islands;

import java.util.logging.Level;

public class Config {

    public void loadMapData() {

    }

    private void log(String msg) {
        Islands.getInstance().getLogger().log(Level.INFO, msg);
    }

    private void error(String msg) {
        Islands.getInstance().getLogger().log(Level.SEVERE, msg);
    }

}
