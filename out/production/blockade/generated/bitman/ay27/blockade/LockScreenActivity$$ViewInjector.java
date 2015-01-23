// Generated code from Butter Knife. Do not modify!
package bitman.ay27.blockade;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LockScreenActivity$$ViewInjector {
  public static void inject(Finder finder, final bitman.ay27.blockade.LockScreenActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099658, "field 'errorPasswdTxv'");
    target.errorPasswdTxv = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131099648, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    view = finder.findRequiredView(source, 2131099649, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    view = finder.findRequiredView(source, 2131099650, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    view = finder.findRequiredView(source, 2131099651, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    view = finder.findRequiredView(source, 2131099652, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    view = finder.findRequiredView(source, 2131099653, "method 'numberBtnClick'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.numberBtnClick((android.widget.Button) p0);
        }
      });
    target.btns = Finder.listOf(
        (android.widget.Button) finder.findRequiredView(source, 2131099648, "btns"),
        (android.widget.Button) finder.findRequiredView(source, 2131099649, "btns"),
        (android.widget.Button) finder.findRequiredView(source, 2131099650, "btns"),
        (android.widget.Button) finder.findRequiredView(source, 2131099651, "btns"),
        (android.widget.Button) finder.findRequiredView(source, 2131099652, "btns"),
        (android.widget.Button) finder.findRequiredView(source, 2131099653, "btns")
    );    target.edts = Finder.listOf(
        (android.widget.EditText) finder.findRequiredView(source, 2131099654, "edts"),
        (android.widget.EditText) finder.findRequiredView(source, 2131099655, "edts"),
        (android.widget.EditText) finder.findRequiredView(source, 2131099656, "edts"),
        (android.widget.EditText) finder.findRequiredView(source, 2131099657, "edts")
    );  }

  public static void reset(bitman.ay27.blockade.LockScreenActivity target) {
    target.errorPasswdTxv = null;
    target.btns = null;
    target.edts = null;
  }
}
