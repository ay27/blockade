package bitman.ay27.blockade.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/23.
 */
public class LockScreenDialog extends AlertDialog {
    protected LockScreenDialog(Context context) {
        super(context);
    }

    protected LockScreenDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected LockScreenDialog(Context context, int theme) {
        super(context, theme);
    }
}
