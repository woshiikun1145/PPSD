// src/main/java/kun/PingPlayerNameAndSprDisplay.java
package kun;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPlayerNameAndSprDisplay implements ModInitializer {
    public static final String MOD_ID = "ping-player-name-and-spr-display";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Ping/SPR Display Mod initialized");
    }
}