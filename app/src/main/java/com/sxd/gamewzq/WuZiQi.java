package com.sxd.gamewzq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * 作者：Sun
 * 时间：2016/5/29 21:01
 * 邮箱：sun33919135@gmail.com
 */
public class WuZiQi extends View
{

    private int mPanelWidth;
    private float mLineHeight;

    private int MAX_LINE = 10;
    private int MAX_COUNT_IN_LINE = 5;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    /**
     * 白棋先手，或当前轮到白棋
     */
    private boolean mIsWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver;

    public enum Winner
    {
        INIT_WINNER,
        WHITEWINNER,
        BLACKWINNER,
        NOWINNER
    }

    public interface GameOverLinstener
    {
        /**
         * @param win win == true, 白棋胜, 反之则，黑棋胜
         */
        void onGameOver(Winner win);
    }

    private Winner mWin = Winner.INIT_WINNER;

    private GameOverLinstener mGameOverLinstener;

    public void setOnGameOverLinstener(GameOverLinstener linstener)
    {
        this.mGameOverLinstener = linstener;
    }

    public WuZiQi(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setBackgroundColor(0x44FF0000);
        init();
    }

    private void init()
    {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖动
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED)
            width = heightSize;
        else if (heightMode == MeasureSpec.UNSPECIFIED)
            width = widthSize;

        setMeasuredDimension(width, width);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver()
    {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        boolean noWin = checkNoWin(whiteWin, blackWin);

        if (whiteWin)
            mWin = Winner.WHITEWINNER;
        else if (blackWin)
            mWin = Winner.BLACKWINNER;
        else if (noWin)
            mWin = Winner.NOWINNER;

        if (whiteWin || blackWin || noWin)
        {
            mIsGameOver = true;
            if (mGameOverLinstener != null)
                mGameOverLinstener.onGameOver(mWin);
        }
    }

    private boolean checkFiveInLine(ArrayList<Point> points)
    {
        for (Point p : points)
        {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win)
                return true;
            win = checkVertical(x, y, points);
            if (win)
                return true;
            win = checkLeftDiagonal(x, y, points);
            if (win)
                return true;
            win = checkRightDiagonal(x, y, points);
            if (win)
                return true;
        }
        return false;
    }

    private boolean checkHorizontal(int x, int y, ArrayList<Point> points)
    {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
        {
            // 判断棋子左边是否5个相连
            if (points.contains(new Point(x - i, y)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;

            if (points.contains(new Point(x + i, y)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;
        }
        return false;
    }

    private boolean checkVertical(int x, int y, ArrayList<Point> points)
    {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
        {
            // 判断棋子上边是否5个相连
            if (points.contains(new Point(x, y - i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;

            // 判断棋子下边是否5个相连
            if (points.contains(new Point(x, y + i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;
        }
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, ArrayList<Point> points)
    {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
        {
            // 判断棋子左下边是否5个相连
            if (points.contains(new Point(x - i, y + i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;

            // 判断棋子右上边是否5个相连
            if (points.contains(new Point(x + i, y - i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, ArrayList<Point> points)
    {
        int count = 1;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++)
        {
            // 判断棋子右下边是否5个相连
            if (points.contains(new Point(x + i, y + i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;

            // 判断棋子左上边是否5个相连
            if (points.contains(new Point(x - i, y - i)))
                count++;
            else
                break;

            if (count == MAX_COUNT_IN_LINE)
                return true;
        }

        if (count == MAX_COUNT_IN_LINE)
            return true;
        return false;
    }

    private boolean checkNoWin(boolean whiteWin, boolean blackWin) {
        if (whiteWin || blackWin) {
            return false;
        }
        int maxPieces = MAX_LINE * MAX_LINE;
        //如果白棋和黑棋的总数等于棋盘格子数,说明和棋
        if (mWhiteArray.size() + mBlackArray.size() == maxPieces) {
            return true;
        }
        return false;
    }

    private void drawPiece(Canvas canvas)
    {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++)
        {
            Point whitPoint = mWhiteArray.get(i);
            float leftWhite = (whitPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight;
            float topWhite = (whitPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight;
            canvas.drawBitmap(mWhitePiece, leftWhite, topWhite, null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++)
        {
            Point blackPoint = mBlackArray.get(i);
            float leftBlack = (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight;
            float topBlack = (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight;
            canvas.drawBitmap(mBlackPiece, leftBlack, topBlack, null);
        }
    }

    private void drawBoard(Canvas canvas)
    {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++)
        {
            int startX = (int) (lineHeight / 2);
            int stopX = (int) (w - lineHeight / 2);
            int startY = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, startY, stopX, startY, mPaint); // 绘制横线
            canvas.drawLine(startY, startX, startY, stopX, mPaint); // 绘制纵线
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mIsGameOver)
            return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = new Point((int) (x / mLineHeight), (int) (y / mLineHeight));

            if (mWhiteArray.contains(p) || mBlackArray.contains(p))
                return false;

            if (mIsWhite)
                mWhiteArray.add(p);
            else
                mBlackArray.add(p);

            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        return true;
    }

    public void reStart()
    {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhite = true;
        mWin = Winner.INIT_WINNER;
        invalidate();
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_IS_WHITE = "instance_is_white";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
        bundle.putBoolean(INSTANCE_IS_WHITE, mIsWhite);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            mIsWhite = bundle.getBoolean(INSTANCE_IS_WHITE);
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
