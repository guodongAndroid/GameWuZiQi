package com.sxd.gamewzq;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/13.
 */

public class MyArrayList<T> extends ArrayList<T> {
    private WuZiQi.OnGameLinstener mOnGameLinstener;

    public void setOnGameLinstener(WuZiQi.OnGameLinstener linstener) {
        this.mOnGameLinstener = linstener;
    }

    /**
     * 悔棋的时候回调
     *
     * @param index
     * @return
     */
    @Override
    public T remove(int index) {
        if (mOnGameLinstener != null) {
            mOnGameLinstener.updateRegretBtnEnable(false, 0.4f);
        }
        return super.remove(index);
    }
}
