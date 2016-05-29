package com.sxd.gamewzq;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：Sun
 * 时间：2016/5/29 21:00
 * 邮箱：sun33919135@gmail.com
 */
public class MainActivity extends AppCompatActivity
{

    private WuZiQi mWQZ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWQZ = (WuZiQi) findViewById(R.id.wuqizi);

        mWQZ.setOnGameOverLinstener(new WuZiQi.GameOverLinstener()
        {
            @Override
            public void onGameOver(WuZiQi.Win win)
            {
                switch (win)
                {
                    case WHITEWIN:
                        showDialog("白棋胜利");
                        break;

                    case BLACKWIN:
                        showDialog("黑棋胜利");
                        break;

                    case NOWIN:
                        showDialog("和棋");
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void showDialog(String text)
    {
        new AlertDialog.Builder(MainActivity.this).setMessage(text)
                .setPositiveButton("再来一局", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mWQZ.reStart();
                    }
                }).setNegativeButton("退出游戏", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        }).create().show();
    }

}
