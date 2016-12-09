package com.sxd.gamewzq;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        mWQZ.setIsWhite((Button) findViewById(R.id.isWhite));

        mRegret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWQZ.reGret(mRegret);
//                startGuodong(PACKAGE_NAME);
            }
        });

        mGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWQZ.isWhite())
                    showDialog("黑棋胜利");
                else
                    showDialog("白棋胜利");
            }
        });

        mWQZ.setOnGameOverLinstener(new WuZiQi.GameOverLinstener() {
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
        });
    }

    /**
     * 启动其他的App
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
                .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWQZ.reStart();
                    }
                }).setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}
