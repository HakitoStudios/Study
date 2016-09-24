package hakito.trycatch.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import hakito.trycatch.Adapters.ShopAdapter;
import hakito.trycatch.Data.Content;
import hakito.trycatch.Data.Helpers.Buyer;
import hakito.trycatch.Framents.BonusesFragment;
import hakito.trycatch.Framents.PlayerStateFragment;
import hakito.trycatch.R;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    PlayerStateFragment playerStateFragment;
    BonusesFragment bonusesFragment;
    ListView listView;
    ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_shop);
        playerStateFragment = new PlayerStateFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, playerStateFragment).commit();

        bonusesFragment = new BonusesFragment();

        Bundle args = new Bundle();
        args.putBoolean(BonusesFragment.EXTRA_ENABLED, false);
        bonusesFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_bonuses, bonusesFragment).commit();

        listView = (ListView)findViewById(R.id.listView);
        adapter = new ShopAdapter(this);
        adapter.addAll(Content.goods);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Buyer.buy(adapter.getItem(position), ShopActivity.this);
                playerStateFragment.notifyChanged();
                bonusesFragment.loadBonuses();
            }
        });
        setVolume(0.2f);
    }



    @Override
    protected void onResume() {
        super.onResume();
        playerStateFragment.notifyChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {



        }
    }
}
