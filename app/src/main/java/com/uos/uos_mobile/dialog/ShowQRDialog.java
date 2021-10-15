package com.uos.uos_mobile.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.uos.uos_mobile.manager.SharedPreferencesManager;
import com.uos.uos_mobile.manager.UsefulFuncManager;
import com.uos.uos_mobile.other.Global;

public class ShowQRDialog extends UosDialog {
    private final Context context;
    private AppCompatTextView tvDlgShowQrMessage;
    private AppCompatImageButton ibtnDlgShowQrClose;
    private AppCompatImageView ivDlgShowQrImage;

    public ShowQRDialog(@NonNull Context context, boolean canceledOnTouchOutside, boolean cancelable) {
        super(context, com.uos.uos_mobile.R.style.DialogTheme_FullScreenDialog);
        this.context = context;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.uos.uos_mobile.R.layout.dialog_showqr);

        init();
    }

    private void init() {
        ibtnDlgShowQrClose = findViewById(com.uos.uos_mobile.R.id.ibtn_dlgshowqr_close);
        tvDlgShowQrMessage = findViewById(com.uos.uos_mobile.R.id.tv_dlgshowqr_message);
        ivDlgShowQrImage = findViewById(com.uos.uos_mobile.R.id.iv_dlgshowqr_image);

        ibtnDlgShowQrClose.setOnClickListener(view -> {
            dismiss();
        });

        // QR 코드 Resource 변경하는 부분
        SharedPreferencesManager.open(context, Global.SharedPreference.APP_DATA);
        String strQrImage = (String) SharedPreferencesManager.load(Global.SharedPreference.QR_IMAGE, "");

        if (strQrImage.length() == 0) {
            tvDlgShowQrMessage.setText("저장된 QR 코드가 없습니다");
            ivDlgShowQrImage.setImageDrawable(context.getDrawable(com.uos.uos_mobile.R.drawable.icon_btnclose));
        } else {
            tvDlgShowQrMessage.setText("QR 코드를 인식하여 주문하세요");
            ivDlgShowQrImage.setImageBitmap(UsefulFuncManager.convertStringToBitmap(strQrImage));
        }
    }
}
