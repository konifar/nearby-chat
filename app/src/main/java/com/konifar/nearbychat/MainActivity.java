package com.konifar.nearbychat;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.konifar.nearbychat.models.pojo.MessageModel;
import com.konifar.nearbychat.utils.AppUtils;
import com.konifar.nearbychat.views.CommentFooter;
import com.konifar.nearbychat.views.LoadAndErrorView;
import com.konifar.nearbychat.views.adapters.MessagesArrayAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_RESOLVE_ERROR = 10001;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.load_and_error_view)
    LoadAndErrorView loadAndErrorView;
    @Bind(R.id.comment_footer)
    CommentFooter commentFooter;

    private boolean isResolvingError;
    private MessagesArrayAdapter adapter;
    private GoogleApiClient googleApiClient;
    private Strategy strategy;

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(Message message) {
            if (message != null) Log.d(TAG, new String(message.getContent()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initApiClient();
        initListView();
        initCommentFooter();
    }

    private void initCommentFooter() {
        commentFooter.setOnSendButtonClickListener(text -> publish(text));
    }

    private void publish(String text) {
        Message message = new Message(text.getBytes());
        Nearby.Messages.publish(googleApiClient, message, strategy)
                .setResultCallback(new NearbyResultCallback("publish()", () -> addMessage(text)));
    }

    private void addMessage(String text) {
        adapter.add(new MessageModel(text));
    }

    private void initApiClient() {
        strategy = new Strategy.Builder()
                .setDiscoveryMode(Strategy.DISCOVERY_MODE_DEFAULT)
                .setDistanceType(Strategy.DISCOVERY_MODE_DEFAULT)
                .setTtlSeconds(Strategy.TTL_SECONDS_DEFAULT)
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(new NearbyConnectionCallbacks())
                .addOnConnectionFailedListener(connectionResult -> onApiConnectionFailded())
                .build();
    }

    private void onApiConnectionFailded() {
        Log.d(TAG, "onApiConnectionFailed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!googleApiClient.isConnected()) googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            Nearby.Messages.unsubscribe(googleApiClient, messageListener);
        }
        googleApiClient.disconnect();
    }

    private void loadData() {
        loadAndErrorView.showLoading();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            isResolvingError = false;
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Start subscribe");
                subscribe();
            } else {
                Log.d(TAG, "Failed to resolve error with code " + resultCode);
            }
        }
    }

    private void subscribe() {
        Nearby.Messages.subscribe(googleApiClient, messageListener)
                .setResultCallback(new NearbyResultCallback("subscribe()"));
    }

    private class NearbyConnectionCallbacks implements GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            Log.d(TAG, "onConnected: " + bundle);
            Nearby.Messages.getPermissionStatus(googleApiClient).setResultCallback(
                    new NearbyResultCallback("getPermissionStatus", () -> subscribe()));
        }

        @Override
        public void onConnectionSuspended(int i) {
            //
        }
    }

    private class NearbyResultCallback implements ResultCallback<Status> {

        private final String method;
        private final Runnable runOnSuccess;

        private NearbyResultCallback(String method) {
            this(method, null);
        }

        private NearbyResultCallback(String method, @Nullable Runnable runOnSuccess) {
            this.method = method;
            this.runOnSuccess = runOnSuccess;
        }

        @Override
        public void onResult(Status status) {
            if (status.isSuccess()) {
                Log.i(TAG, method + " succeeded.");
                if (runOnSuccess != null) {
                    runOnSuccess.run();
                }
            } else {
                if (status.hasResolution()) {
                    if (!isResolvingError) {
                        try {
                            status.startResolutionForResult(MainActivity.this, REQUEST_RESOLVE_ERROR);
                            isResolvingError = true;
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, e.getMessage());
                            AppUtils.showToast("Failed : " + e.getMessage(), getApplicationContext());
                            //
                        }
                    } else {
                        Log.d(TAG, method + " failed with status: " + status + " while resolving error.");
                    }
                } else {
                    Log.d(TAG, method + " failed with: " + status + " resolving error: " + isResolvingError);
                }
            }
        }
    }

}
