package bitman.ay27.blockade.orm.module;

import com.j256.ormlite.field.DatabaseField;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/25.
 */

/**
 * save the lock app list.
 */
public class AppLockItem {
    @DatabaseField(canBeNull = false)
    String packageName;

    public AppLockItem() {
    }

    public AppLockItem(String packageName) {
        this.packageName = packageName;
    }

    public String toString() {
        return packageName+" locked";
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
