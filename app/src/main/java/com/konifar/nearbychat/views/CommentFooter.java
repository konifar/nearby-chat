package com.konifar.nearbychat.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.konifar.nearbychat.R;
import com.konifar.nearbychat.utils.KeyboardUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentFooter extends LinearLayout implements TextWatcher {

    @Bind(R.id.btn_comment)
    Button btnComment;
    @Bind(R.id.edit_comment)
    EditText editComment;

    private OnSendButtonClickListener listener;

    public CommentFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ui_comment_footer, this, true);
        ButterKnife.bind(this);

        editComment.addTextChangedListener(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_comment)
    public void onClickSendComment() {
        if (listener != null && isCommentPresent()) {
            listener.onClick(editComment.getText().toString());
            clearText();
        }
    }

    private void clearText() {
        editComment.setText("");
        KeyboardUtils.hide(getContext(), editComment);
    }

    private boolean isCommentPresent() {
        return !TextUtils.isEmpty(editComment.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {
        btnComment.setEnabled(isCommentPresent());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
    }

    public void setOnSendButtonClickListener(OnSendButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnSendButtonClickListener {
        void onClick(String text);
    }

}
