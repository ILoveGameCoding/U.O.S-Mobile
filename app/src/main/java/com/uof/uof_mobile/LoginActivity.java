package com.uof.uof_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivLoginRecognizeQR;
    private TextInputLayout tilLoginId;
    private TextInputLayout tilLoginPw;
    private Button btnLoginLogin;
    private TextView tvLoginRegister;
    private LinearLayoutCompat llLoginLoginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        ivLoginRecognizeQR = findViewById(R.id.iv_login_recognizeqr);
        tilLoginId = findViewById(R.id.til_login_id);
        tilLoginPw = findViewById(R.id.til_login_pw);
        btnLoginLogin = findViewById(R.id.btn_login_login);
        tvLoginRegister = findViewById(R.id.tv_login_register);
        llLoginLoginLayout = findViewById(R.id.ll_login_loginlayout);

        // 기본 데이터 설정


        // 기본 UI 상태 설정
        btnLoginLogin.setEnabled(false);
        llLoginLoginLayout.setVisibility(View.VISIBLE);

        // QR 코드 인식하기 ImageView가 눌렸을 경우
        ivLoginRecognizeQR.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, QRRecognitionActivity.class));
        });

        // 로그인 - 아이디 입력란이 수정되었을 경우
        tilLoginId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilLoginId.setErrorEnabled(false);
                tilLoginPw.setErrorEnabled(false);
                tilLoginId.setError(null);
                tilLoginPw.setError(null);
                btnLoginLogin.setEnabled(checkLogin());
            }
        });

        // 로그인 - 비밀번호 입력란이 수정되었을 경우
        tilLoginPw.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tilLoginId.setErrorEnabled(false);
                tilLoginPw.setErrorEnabled(false);
                tilLoginId.setError(null);
                tilLoginPw.setError(null);
                btnLoginLogin.setEnabled(checkLogin());
            }
        });

        // 로그인 버튼이 눌렸을 경우
        btnLoginLogin.setOnClickListener(view -> {
            btnLoginLogin.setEnabled(false);

            // 로그인 창일 경우
            try {
                JSONObject sendData = new JSONObject();
                sendData.put("request_code", "1002");

                JSONObject message = new JSONObject();
                message.accumulate("id", tilLoginId.getEditText().getText().toString());
                message.accumulate("pw", tilLoginPw.getEditText().getText().toString());

                sendData.accumulate("message", message);

                JSONObject recvData = new JSONObject(new HttpManager().execute(new String[]{"http://211.217.202.157:8080/post", sendData.toString()}).get());

                String requestCode = recvData.getString("request_code");

                if (requestCode.equals("0003")) {
                    // 로그인 성공 - LobbyActivity로 이동
                    Intent intent = new Intent(LoginActivity.this, LobbyActivity.class);
                    startActivity(intent);
                } else if (requestCode.equals("0004")) {
                    // 로그인 실패 - 아이디 없음
                    tilLoginId.setErrorEnabled(true);
                    tilLoginId.setError("아이디가 존재하지 않습니다");
                } else if (requestCode.equals("0005")) {
                    // 로그인 실패 - 비밀번호 틀림
                    tilLoginId.setErrorEnabled(true);
                    tilLoginId.setError("비밀번호가 일치하지 않습니다");
                } else {
                    // 로그인 실패 - 기타 오류
                    Toast.makeText(LoginActivity.this, "로그인 실패: " + recvData.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            btnLoginLogin.setEnabled(true);
        });

        // 회원가입 TextView가 눌렸을 경우
        tvLoginRegister.setOnClickListener(view -> {
            new RegisterTypeDialog(LoginActivity.this, true, true,
                    new RegisterTypeDialogListener() {
                        @Override
                        public void onCustomerClick() {
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 0);    //고객
                            startActivity(intent);  //다음 activity로 넘어가기
                        }

                        @Override
                        public void onUofPartnerClick() {
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("RegisterType", 1);    //파트너
                            startActivity(intent);  //다음 activity로 넘어가기
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    }).show();
        });
    }

    // 로그인 시 아이디, 비밀번호 입력란 확인
    private boolean checkLogin() {
        return tilLoginId.getEditText().getText().toString().length() > 0 && tilLoginPw.getEditText().getText().toString().length() > 0;
    }
}