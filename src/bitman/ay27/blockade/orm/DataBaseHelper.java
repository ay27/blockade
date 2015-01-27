package bitman.ay27.blockade.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import bitman.ay27.blockade.utils.ClassUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/26.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "bitman.ay27.blockade.db";
    public static final int DB_VERSION = 1;

    public static final String MODULE_PACKAGE_NAME = "bitman.ay27.blockade.orm.module";
    private static List<Class> allModules = ClassUtils.getClasssFromPackage(MODULE_PACKAGE_NAME);


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            for (Class oneModule : allModules) {
                TableUtils.createTable(connectionSource, oneModule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            for (Class oneModule : allModules) {
                TableUtils.dropTable(connectionSource, oneModule, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }
}
