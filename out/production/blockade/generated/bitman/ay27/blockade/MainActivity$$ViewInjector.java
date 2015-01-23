// Generated code from Butter Knife. Do not modify!
package bitman.ay27.blockade;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final bitman.ay27.blockade.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099659, "field 'togBtn'");
    target.togBtn = (android.widget.ToggleButton) view;
  }

  public static void reset(bitman.ay27.blockade.MainActivity target) {
    target.togBtn = null;
  }
}
