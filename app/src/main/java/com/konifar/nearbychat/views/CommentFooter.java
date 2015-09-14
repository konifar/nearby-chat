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
    Button mBtnComment;
    @Bind(R.id.edit_comment)
    EditText mEditComment;

    private Context context;

    public CommentFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ui_comment_footer, this, true);
        ButterKnife.bind(this);

        mEditComment.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_comment)
    public void onClickSendComment() {
//        SendBtnClickedEvent event = new SendBtnClickedEvent(mEditComment.getText().toString());
//        EventBus.getDefault().post(event);
    }

    public void clearText() {
        mEditComment.setText("");
        KeyboardUtils.hide(context, mEditComment);
    }

    public boolean hasCommentInInput() {
        return !TextUtils.isEmpty(mEditComment.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {
        mBtnComment.setEnabled(!TextUtils.isEmpty(mEditComment.getText()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
    }

}
