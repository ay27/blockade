package bitman.ay27.blockade;

import java.io.DataOutputStream;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/24.
 */
public class UpgradeSystemPermission {

    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd1="chmod 777 " + pkgCodePath;
            String cmd2 = "pm grant bitman.ay27.blockade  android.permission.WRITE_SECURE_SETTINGS";
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd1 + "\n");
            os.writeBytes(cmd2 + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

}
