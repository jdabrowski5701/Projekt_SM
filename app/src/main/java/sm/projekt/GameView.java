package sm.projekt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Random;
//import android.widget.LinearLayout;

public class GameView extends View {

    public interface OnBallReachEndListener {
        void onBallReachEnd();
    }
    private OnBallReachEndListener ballReachEndListener;

    private Paint ballPaint1, ballPaint2;
    private int ballRadius = 20; // Adjust the size as needed
    private int ball1X, ball1Y; // Position of the first ball
    private int ball2X, ball2Y; // Position of the second ball
    private int rangeStart1, rangeStop1, rangeStart2, rangeStop2;
    private int screenHeight;
    private int screenWidth;
    private int ballSpeed = 5; // Adjust the speed as needed
    private Random random = new Random();
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
        //ballPaint1.setColor(Color.RED);
        //ballPaint2.setColor(Color.RED);
        ballPaint1.setColor(Color.parseColor("#5C00FF"));
        ballPaint2.setColor(Color.parseColor("#FF9800"));
        // Initialize the initial positions of the balls
//        ball1X = screenWidth/3;//screenWidth / 3; // Adjust the starting positions as needed
//        ball1Y = 0;
//        ball2X = 2*screenWidth/3;//2 * screenWidth / 3;
//        ball2Y = 0;
    }

    public void setOnBallReachEndListener(OnBallReachEndListener listener) {
        this.ballReachEndListener = listener;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the first ball
        canvas.drawCircle(ball1X, ball1Y, ballRadius, ballPaint1);

        // Draw the second ball
        canvas.drawCircle(ball2X, ball2Y, ballRadius, ballPaint2);

        // Update the positions of the balls
        ball1Y += ballSpeed;
        ball2Y += ballSpeed;

        // Check if the balls have gone off the screen
//        if (ball1Y > screenHeight) {
//            ball1Y = 0; // Reset the first ball's position
//
//        }
//        if (ball2Y > screenHeight) {
//            ball2Y = 0; // Reset the second ball's position
//        }

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

        // Initialize the initial positions of the balls
        ball1X = screenWidth / 4; // Adjust the starting positions as needed
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
        if (Math.abs(ball1X-ball2X) < 20) {
            setBall2X(5);
        }
    }
}