package test_tracker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VSphereUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());

    private VSphereUtil() {}

    public static void openVSphereHere(String ip) {
        try {
            Runtime runTime = Runtime.getRuntime();
            String vpxClientLoc = "C:\\Program Files (x86)\\VMware\\Infrastructure\\Virtual Infrastructure Client\\Launcher\\VpxClient.exe";

            // resource here: https://communities.vmware.com/thread/500884
            runTime.exec(vpxClientLoc +  " -locale en_US -s " + ip + " -u root -p R))Tr0x");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "vSphere connection refused: ", e);
        }
    }
}
