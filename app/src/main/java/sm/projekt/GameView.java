package sm.projekt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
//import android.widget.LinearLayout;

public class GameView extends View {

    public interface OnBallReachEndListener {
        void onBallReachEnd();
        void onCollisionDetected(String ball);
    }
    private OnBallReachEndListener ballReachEndListener;

    private Paint ballPaint1, ballPaint2;
    private int ballRadius = 20; // size
    private int ball1X, ball1Y; // pos
    private int ball2X, ball2Y;
    private int rangeStart1, rangeStop1, rangeStart2, rangeStop2;
    private int screenHeight;
    private int screenWidth;
    private int ballSpeed = 7;
    private Random random = new Random();
    private GameActivity gameActivity = new GameActivity();
    //private ImageView imageView;
    private int playerIconX;
    private int playerIconY;
    private int playerIconSize;
    private boolean leftBallCorrect;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ballPaint1 = new Paint();
        ballPaint2 = new Paint();
        ballPaint1.setColor(Color.parseColor("#5C00FF"));
        ballPaint2.setColor(Color.parseColor("#FF9800"));
    }

    public void setOnBallReachEndListener(OnBallReachEndListener listener) {
        this.ballReachEndListener = listener;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw
        canvas.drawCircle(ball1X, ball1Y, ballRadius, ballPaint1);
        canvas.drawCircle(ball2X, ball2Y, ballRadius, ballPaint2);

        // update pos
        ball1Y += ballSpeed;
        ball2Y += ballSpeed;


        if (isCollisionWithBall(ball1X, ball1Y)) {
            if (ballReachEndListener != null) {
                ballReachEndListener.onCollisionDetected("left");
            }
        }
        if (isCollisionWithBall(ball2X, ball2Y)) {
            if (ballReachEndListener != null) {
                ballReachEndListener.onCollisionDetected("right");
            }
        }

        if (ball1Y > screenHeight || ball2Y > screenHeight) {
            if (ballReachEndListener != null) {
                ball1Y = 0;
                ball2Y = 0;
                ballReachEndListener.onBallReachEnd();
            }
        }

        // Continue to invalidate the view to redraw and animate the balls
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;

        // initialize
        ball1X = screenWidth / 4;
        ball1Y = 0;
        ball2X = 3 * screenWidth / 4;
        ball2Y = 0;

        rangeStart1 = ball1X;
        rangeStop1 = ball1X;
        rangeStart2 = ball2X;
        rangeStop2 = ball2X;
    }

    public int getBallSpeed() {
        return ballSpeed;
    }
    public void setBallSpeed(int newSpeed) {
        ballSpeed = newSpeed;
    }

    public void setBall1X(int i) {
        //rangeStart1 /=2;
        //rangeStop1 = rangeStop1 + rangeStop1/2;
        if (i >= 4) {
            rangeStop1 = screenWidth-9;
            rangeStart1 = 9;
        }
        ball1X = random.nextInt(rangeStop1) + rangeStart1;
    }
    public void setBall2X(int i) {
        //rangeStart2 = rangeStart2 - rangeStart2/2;
        //rangeStop2 += (screenWidth - rangeStop2)/2;
        if (i >= 4) {
            rangeStop2 = screenWidth-9;
            rangeStart2 = 9;
        }
        ball2X = random.nextInt(rangeStop2) + rangeStart2;
        checkX();
    }

    public void checkX() {
        if (Math.abs(ball1X-ball2X) < 100) {
            setBall2X(5);
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }


    private boolean isCollisionWithBall(int ballX, int ballY) {
//        playerIconSize = 100;
//        playerIconX = screenWidth/2;
//        playerIconY = screenHeight-50;
//        int playX = screenWidth/4;
//        int playY = screenHeight;
//        int playI = 100;

        return Math.sqrt(Math.pow(ballX - playerIconX, 2) + Math.pow(ballY - playerIconY, 2)) <= ballRadius + playerIconSize / 2;
    }

    public void setPlayerPosition(int x, int y) {
        playerIconX = x;
        playerIconY = 1620;//y;
    }

    public void setPlayerSize(int size) {
        playerIconSize = size;
    }

    public void setLeftBallCorrect(boolean is) {
        leftBallCorrect = is;
    }

    public boolean getLeftBallCorrect() {
        return leftBallCorrect;
    }
}