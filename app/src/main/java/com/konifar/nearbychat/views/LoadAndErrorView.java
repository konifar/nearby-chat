package com.konifar.nearbychat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konifar.nearbychat.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoadAndErrorView extends RelativeLayout {

    @Bind(R.id.txt_error)
    TextView txtError;
    @Bind(R.id.txt_empty)
    TextView txtEmpty;
    @Bind(R.id.loading)
    View loading;

    public LoadAndErrorView(Context context) {
        super(context);
    }

    public LoadAndErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.ui_load_and_error, this);
        ButterKnife.bind(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadAndErrorView);
        try {
            String errorText = a.getString(R.styleable.LoadAndErrorView_errorText);
            setErrorText(errorText);
            String emptyText = a.getString(R.styleable.LoadAndErrorView_emptyText);
            setEmptyText(emptyText);
        } finally {
            a.recycle();
        }
    }

    public void setErrorText(@StringRes int txtResId) {
        txtError.setText(txtResId);
    }

    public void setErrorText(String text) {
        if (text != null) txtError.setText(text);
    }

    public void setEmptyText(@StringRes int txtResId) {
        txtEmpty.setText(txtResId);
    }

    public void setEmptyText(String text) {
        if (text != null) txtEmpty.setText(text);
    }

    public void showEmpty() {
        txtEmpty.setVisibility(VISIBLE);
        txtError.setVisibility(GONE);
        loading.setVisibility(GONE);
    }

    public void showError(@StringRes String textResId) {
        setEmptyText(textResId);
        showError();
    }

    public void showError() {
        txtEmpty.setVisibility(GONE);
        txtError.setVisibility(VISIBLE);
        loading.setVisibility(GONE);
    }

    public void showLoading() {
        txtEmpty.setVisibility(GONE);
        txtError.setVisibility(GONE);
        loading.setVisibility(VISIBLE);
    }

}
