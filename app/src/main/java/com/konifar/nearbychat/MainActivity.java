package com.konifar.nearbychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.konifar.nearbychat.utils.AppUtils;
import com.konifar.nearbychat.views.adapters.MessagesArrayAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.txt_empty)
    TextView txtEmpty;
    @Bind(R.id.loading)
    View loading;
    @Bind(R.id.txt_error)
    TextView txtError;

    private MessagesArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initListView();
    }

    private void loadData() {
        loading.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void initListView() {
        adapter = new MessagesArrayAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_link:
                AppUtils.showWebPage(Constants.REPOGITORY_URL, this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
