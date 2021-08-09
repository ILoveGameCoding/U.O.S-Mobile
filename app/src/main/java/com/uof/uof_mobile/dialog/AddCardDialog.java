package com.uof.uof_mobile.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.uof.uof_mobile.Constants;
import com.uof.uof_mobile.R;
import com.uof.uof_mobile.manager.HttpManager;

import org.json.JSONObject;

public class AddCardDialog extends AppCompatDialog {
    private final Context context;
    private AppCompatImageButton ibtnAddCardClose;
    private TextInputLayout tilAddCardNum;
    private TextInputLayout tilAddCardMonth;
    private TextInputLayout tilAddCardYear;
    private TextInputLayout tilAddCardCvc;
    private TextInputLayout tilAddCardPw;
    private AppCompatButton btnAddCardApply;

    public AddCardDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_addcard);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    private void init() {
        ibtnAddCardClose = findViewById(R.id.ibtn_addcard_close);
        tilAddCardNum = findViewById(R.id.til_addcard_num);
        tilAddCardMonth = findViewById(R.id.til_addcard_month);
        tilAddCardYear = findViewById(R.id.til_addcard_year);
        tilAddCardCvc = findViewById(R.id.til_addcard_cvc);
        tilAddCardPw = findViewById(R.id.til_addcard_pw);
        btnAddCardApply = findViewById(R.id.btn_addcard_apply);

        ibtnAddCardClose.setOnClickListener(view -> {
            dismiss();
        });

        btnAddCardApply.setOnClickListener(view -> {
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", Constants.Network.Request.CARD_ADD);

                JSONObject message = new JSONObject();
                message.accumulate("id", Constants.User.id);

                JSONObject card = new JSONObject();
                card.accumulate("num", tilAddCardNum.getEditText().getText().toString());
                card.accumulate("due_date", tilAddCardMonth.getEditText().getText().toString() + "/" + tilAddCardYear.getEditText().getText().toString());
                card.accumulate("cvc", tilAddCardCvc.getEditText().getText().toString());
                card.accumulate("pw", tilAddCardPw.getEditText().getText().toString());

                message.accumulate("card", card);

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String responseCode = recvData.getString("response_code");

                if (responseCode.equals(Constants.Network.Response.CARD_ADD_SUCCESS)) {
                    // 카드등록 성공
                } else if (responseCode.equals(Constants.Network.Response.CARD_ADD_FAILED)) {
                    // 카드등록 실패
                } else {
                    // 카드등록 실패 - 기타 오류
                    Toast.makeText(context, "카드등록 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
