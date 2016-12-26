package com.sxd.gamewzq;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static com.sxd.gamewzq.WuZiQi.Winner.BLACKWINNER;
import static com.sxd.gamewzq.WuZiQi.Winner.NOWINNER;
import static com.sxd.gamewzq.WuZiQi.Winner.WHITEWINNER;

/**
 * 作者：Sun
 * 时间：2016/5/29 21:00
 * 邮箱：sun33919135@gmail.com
 */
public class MainActivity extends AppCompatActivity {

    private WuZiQi mWQZ;
    private Button mRegret;
    private Button mGiveUp;
    private Button mIsWhiteBtn;

    private static final String PACKAGE_NAME = "com.guodong.sun.guodong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWQZ = (WuZiQi) findViewById(R.id.wuqizi);
        mRegret = (Button) findViewById(R.id.regret);
        mGiveUp = (Button) findViewById(R.id.giveUp);
        mIsWhiteBtn = (Button) findViewById(R.id.isWhite);

        setRegretBtnEnable(false, 0.4f);

        mGiveUp.setOnClickListener(v -> {
            if (mWQZ.isWhite())
                showDialog("黑棋胜利");
            else
                showDialog("白棋胜利");
        });

        mWQZ.setOnGameLinstener(new WuZiQi.OnGameLinstener() {
            @Override
            public void onGameOver(WuZiQi.Winner win) {
                switch (win) {
                    case WHITEWINNER:
                        showDialog("白棋胜利");
                        break;

                    case BLACKWINNER:
                        showDialog("黑棋胜利");
                        break;

                    case NOWINNER:
                        showDialog("和棋");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRegret(boolean isWhite, ArrayList<Point> whiteArray, ArrayList<Point> blackArray) {
                mRegret.setOnClickListener(v -> {
                    if (isWhite) {
                        if (blackArray.size() > 0) {
                            blackArray.remove(blackArray.size() - 1);
                            mWQZ.setIsWhite(false);
                            mWQZ.invalidate();
                        }
                    } else {
                        if (whiteArray.size() > 0) {
                            whiteArray.remove(whiteArray.size() - 1);
                            mWQZ.setIsWhite(true);
                            mWQZ.invalidate();
                        }
                    }
                });
            }

            @Override
            public void updateIsWhite(String isWhite) {
                mIsWhiteBtn.setText(String.format(getResources().getString(R.string.isWhite), isWhite));
            }

            @Override
            public void updateRegretBtnEnable(boolean enable, float alpha) {
                setRegretBtnEnable(enable, alpha);
            }
        });
    }

    /**
     * 启动其他的App
     *
     * @param packageName 其他App的包名
     */
    private void startGuodong(String packageName) {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "赶紧去安装果冻App吧", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建一个对话框
     *
     * @param text 提示信息
     */
    private void showDialog(String text) {
        new AlertDialog.Builder(MainActivity.this).setMessage(text)
                .setCancelable(false)
                .setPositiveButton("再来一局", (dialog, which) -> mWQZ.reStart())
                .setNegativeButton("退出游戏", ((dialog, which) -> finish()))
                .create()
                .show();
    }

    /**
     * 设置悔棋按钮是否可用
     *
     * @param enable 是否可以点击
     * @param alpha  按钮的透明度
     */
    private void setRegretBtnEnable(boolean enable, float alpha) {
        if (this.mRegret != null) {
            this.mRegret.setEnabled(enable);
            this.mRegret.setAlpha(alpha);
        }
    }
}
