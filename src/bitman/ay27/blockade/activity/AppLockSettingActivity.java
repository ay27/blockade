package bitman.ay27.blockade.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.orm.DatabaseHelper;
import bitman.ay27.blockade.orm.module.AppLockItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/25.
 */
public class AppLockSettingActivity extends ActionBarActivity {

    @InjectView(R.id.app_lock_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.app_lock_list)
    ListView listView;


    static class AppListAdapter extends BaseAdapter {

        private RuntimeExceptionDao dao;
        private Context context;
        private ArrayList<AppLockItem> lockedApps;
        private List<ApplicationInfo> infos;

        public AppListAdapter(Context context) {
            this.context = context;

            this.dao = new DatabaseHelper(context).getRuntimeExceptionDao(AppLockItem.class);

            loadData();
        }

//        private ArrayList<PrivacyApplication> getAppList() {
//            ArrayList<database.entity.PrivacyApplication> appList = new ArrayList<PrivacyApplication>();
//            List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
//            for (int i = 0; i < packages.size(); i++) {
//                PackageInfo packageInfo = packages.get(i);
//                database.entity.PrivacyApplication application = new PrivacyApplication();
//                application.setApp_name(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
//                application.setPac_name(packageInfo.packageName);
//                application.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
//                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                    appList.add(application);
//                }
//            }
//            return appList;
//        }

        private void loadData() {



            infos = context.getPackageManager().getInstalledApplications(0);
            try {
                lockedApps = new ArrayList<AppLockItem>(dao.queryBuilder().orderBy("packageName", true)
                        .query());
            } catch (SQLException e) {
                e.printStackTrace();
                lockedApps = new ArrayList<AppLockItem>();
            }
        }


        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.app_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ApplicationInfo app = infos.get(position);
            holder.icon.setImageDrawable(app.loadIcon(context.getPackageManager()));
            holder.name.setText(app.loadLabel(context.getPackageManager()).toString());
            holder.aSwitch.setChecked(ifContains(app.packageName));
            holder.aSwitch.setOnCheckedChangeListener(generateListener(position));

            return convertView;
        }

        private boolean ifContains(String packageName) {
            for (AppLockItem item : lockedApps) {
                if (item.getPackageName().equals(packageName))
                    return true;
            }
            return false;
        }

        private CompoundButton.OnCheckedChangeListener generateListener(final int position) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        dao.create(new AppLockItem(infos.get(position).packageName));
                    } else {
                        dao.delete(new AppLockItem(infos.get(position).packageName));
                    }
                }
            };
        }

        class ViewHolder {
            @InjectView(R.id.app_list_item_icon)
            ImageView icon;
            @InjectView(R.id.app_list_item_name)
            TextView name;
            @InjectView(R.id.app_list_item_switch)
            Switch aSwitch;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_lock_settings);
        ButterKnife.inject(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("app lock");
        setSupportActionBar(toolbar);

        listView.setAdapter(new AppListAdapter(this));
    }

}
