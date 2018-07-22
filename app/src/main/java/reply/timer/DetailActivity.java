package reply.timer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DetailActivity extends SingleFragmentActivity {

    public static final String COUNT_DOWN_ID = "reply.time.countdown.id";

    @Override
    protected Fragment createFragment() {
        int id =  getIntent().getIntExtra(COUNT_DOWN_ID, 1);
        return DetailFragment.newInstance(id);
    }

    public static Intent newIntent(Context packageContext, int countdown_id) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(COUNT_DOWN_ID, countdown_id);
        return intent;
    }


}
