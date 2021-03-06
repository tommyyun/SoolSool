package com.example.q.soolsool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Tab3 extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab3, container, false);
        if(MainActivity.loggedIn)
            onLoggedIn();
        else
            onLoggedOut();

        view.findViewById(R.id.developer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DeveloperActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.advertising).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-4162-7271"));
                startActivity(callIntent);
            }
        });

        view.findViewById(R.id.helper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "여러분, 감사합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.premium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PremiumDialog(getContext()).show();
            }
        });

        return view;
    }

    private void onLoggedIn() {
        view.findViewById(R.id.tab3_before_login).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tab3_after_login).setVisibility(View.VISIBLE);
        ((TextView)view.findViewById(R.id.user_id)).setText(MainActivity.id);
        view.findViewById(R.id.tab3_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                onLoggedOut();
            }
        });
    }

    private void onLoggedOut() {

        MainActivity.id = null;
        MainActivity.loggedIn = false;
        view.findViewById(R.id.tab3_before_login).setVisibility(View.VISIBLE);
        view.findViewById(R.id.tab3_after_login).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.tab3_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                login(((TextView)view.findViewById(R.id.tab3_id_field)).getText().toString(), ((TextView)view.findViewById(R.id.tab3_pw_field)).getText().toString());
            }
        });
        view.findViewById(R.id.tab3_register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterDialog(getContext()).show();
            }
        });
    }

    private void login(final String id, final String pw) {
        Volley.newRequestQueue(getActivity()).add(new StringRequest("http://52.231.70.8:8080/user/login/" + id + "/" + pw, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("VALID")) {
                    MainActivity.id = id;
                    MainActivity.loggedIn = true;
                    Toast.makeText(getContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    onLoggedIn();
                } else if (response.equals("INVALID"))
                    Toast.makeText(getContext(), "비밀번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    class RegisterDialog extends Dialog {

        public RegisterDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.tab3_register_dialog);
            setTitle("회원가입");
            findViewById(R.id.tab3_register_request_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = ((TextView)findViewById(R.id.tab3_register_id_field)).getText().toString();
                    String pw = ((TextView)findViewById(R.id.tab3_register_pw_field)).getText().toString();
                    Volley.newRequestQueue(RegisterDialog.this.getContext()).add(new StringRequest("http://52.231.70.8:8080/user/register/" + id + "/" + pw, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("REGISTERED")) {
                                Toast.makeText(RegisterDialog.this.getContext(), "가입되었습니다.\n로그인해주세요.",Toast.LENGTH_SHORT).show();
                                RegisterDialog.this.cancel();
                            }
                            if(response.equals("DUPLICATE"))
                                Toast.makeText(RegisterDialog.this.getContext(), "아이디가 이미 존재합니다.",Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterDialog.this.getContext(), "껐다가 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }));
                }
            });
            findViewById(R.id.tab3_register_cancel_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegisterDialog.this.cancel();
                }
            });
        }
    }

    class PremiumDialog extends Dialog {

        public PremiumDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tab3_premium_dialog);
            findViewById(R.id.tab3_premium_accept_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MainActivity.pro) {
                        Toast.makeText(getContext(), "이미 Pro 버전입니다.", Toast.LENGTH_SHORT).show();
                        PremiumDialog.this.cancel();
                        return;
                    }
                    MainActivity.pro = true;
                    Toast.makeText(getContext(), "감사합니다.\n결제되었습니다.", Toast.LENGTH_SHORT).show();
                    PremiumDialog.this.cancel();
                }
            });
            findViewById(R.id.tab3_premium_cancel_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PremiumDialog.this.cancel();
                }
            });
        }
    }
}
