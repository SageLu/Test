package com.handstudio.android.hzgrapherlib.graphview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.canvas.GraphCanvasWrapper;
import com.handstudio.android.hzgrapherlib.error.ErrorCode;
import com.handstudio.android.hzgrapherlib.error.ErrorDetector;
import com.handstudio.android.hzgrapherlib.path.GraphPath;
import com.handstudio.android.hzgrapherlib.util.Spline;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraphVO;

/**
 * Created by loo on 16-8-4.
 * 继承普通ｖｉｅｗ的曲线图
 */
public class NYCurveGraphView extends View {

    public static final String TAG = "CurveGraphView";
    private SurfaceHolder mHolder;
    public DrawThread mDrawThread;

    private CurveGraphVO mCurveGraphVO = null;
    private Spline spline = null;
    private Bitmap content;

    //Constructor
    public NYCurveGraphView(Context context, CurveGraphVO vo) {
        super(context);
        mCurveGraphVO = vo;
        initView(context, vo);
    }

    private void initView(Context context, CurveGraphVO vo) {
        ErrorCode ec = ErrorDetector.checkGraphObject(vo);
        ec.printError();
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mDrawThread == null) {
            mDrawThread = new DrawThread(mHolder, getContext());
            mDrawThread.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawThread == null) {
            mDrawThread = new DrawThread(mHolder, getContext());
            mDrawThread.start();
        }
    }

    private static final Object touchLock = new Object(); // touch synchronize

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (mDrawThread == null) {
            return false;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            synchronized (touchLock) {
                mDrawThread.isDirty = true;
            }
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            synchronized (touchLock) {
                mDrawThread.isDirty = true;
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            synchronized (touchLock) {
                mDrawThread.isDirty = true;
            }
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        if (content != null) {
            super.draw(canvas);
            canvas.drawBitmap(content, 0, 0, null);
        } else
            super.draw(canvas);
    }

    class DrawThread extends Thread {
        //final SurfaceHolder mHolder;
        Context mCtx;
        boolean finishDraw;

        boolean isRun = true;
        boolean isDirty = true;


        int height = getHeight();
        int width = getWidth();

        //graph length
        int xLength = width - (mCurveGraphVO.getPaddingLeft() + mCurveGraphVO.getPaddingRight() + mCurveGraphVO.getMarginRight());
        int yLength = height - (mCurveGraphVO.getPaddingBottom() + mCurveGraphVO.getPaddingTop() + mCurveGraphVO.getMarginTop());

        //chart length
        int chartXLength = width - (mCurveGraphVO.getPaddingLeft() + mCurveGraphVO.getPaddingRight());

        Paint p = new Paint();
        Paint pCircle = new Paint();
        Paint pCurve = new Paint();
        Paint pBaseLine = new Paint();
        Paint pBaseLineX = new Paint();
        TextPaint pMarkText = new TextPaint();

        //animation
        float anim = 0.0f;
        boolean isAnimation = false;
        boolean isDrawRegion = false;
        long animStartTime = -1;
        int animationType = 0;

        Bitmap bg = null;

        public DrawThread(SurfaceHolder holder, Context context) {
            mHolder = holder;
            mCtx = context;
            content = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }

        public void setRunFlag(boolean bool) {
            isRun = bool;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            GraphCanvasWrapper graphCanvasWrapper = null;
            Log.e(TAG, "height = " + height);
            Log.e(TAG, "width = " + width);

            setPaint();
            isAnimation();
            isDrawRegion();
            finishDraw = false;
            canvas = new Canvas(content);
            canvas.setBitmap(content);
            animStartTime = System.currentTimeMillis();
            canvas.drawColor(Color.WHITE);
            while (isRun) {

                //draw only on dirty mode
                if (!isDirty) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                }

                graphCanvasWrapper = new GraphCanvasWrapper(canvas, width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());

                calcTimePass();

                synchronized (touchLock) {

                    try {
                        //bg color
                        finishDraw = true;
                        // x coord dot Line
                        drawBaseLine(graphCanvasWrapper);

                        // x coord
                        graphCanvasWrapper.drawLine(-50, 0, chartXLength, 0, pCircle);

                        drawAL((int) graphCanvasWrapper.mMt.calcX(0), (int) graphCanvasWrapper.mMt.calcY(0)
                                , (int) graphCanvasWrapper.mMt.calcX(chartXLength), (int) graphCanvasWrapper.mMt.calcY(0), canvas);

                        // x, y coord mark
                        //drawXMark(graphCanvasWrapper);

                        // x, y coord text
                        drawXText(graphCanvasWrapper);

                        // Draw Graph
                        drawGraphRegion(graphCanvasWrapper);
                        drawGraph(graphCanvasWrapper);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (graphCanvasWrapper.getCanvas() != null) {
                            postInvalidate();
                        }
                    }
                }
            }
        }

        /**
         * 画箭头
         *
         * @param sx
         * @param sy
         * @param ex
         * @param ey
         */
        public void drawAL(int sx, int sy, int ex, int ey, Canvas canvas) {
            double H = 8; // 箭头高度
            double L = 3.5; // 底边的一半
            int x3 = 0;
            int y3 = 0;
            int x4 = 0;
            int y4 = 0;
            double awrad = Math.atan(L / H); // 箭头角度
            double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
            double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
            double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
            double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
            double y_3 = ey - arrXY_1[1];
            double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
            double y_4 = ey - arrXY_2[1];
            Double X3 = new Double(x_3);
            x3 = X3.intValue();
            Double Y3 = new Double(y_3);
            y3 = Y3.intValue();
            Double X4 = new Double(x_4);
            x4 = X4.intValue();
            Double Y4 = new Double(y_4);
            y4 = Y4.intValue();
            Path triangle = new Path();
            triangle.moveTo(ex, ey);
            triangle.lineTo(x3, y3);
            triangle.lineTo(x4, y4);
            triangle.close();
            canvas.drawPath(triangle, pCircle);

        }

        // 计算
        public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {
            double mathstr[] = new double[2];
            // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
            double vx = px * Math.cos(ang) - py * Math.sin(ang);
            double vy = px * Math.sin(ang) + py * Math.cos(ang);
            if (isChLen) {
                double d = Math.sqrt(vx * vx + vy * vy);
                vx = vx / d * newLen;
                vy = vy / d * newLen;
                mathstr[0] = vx;
                mathstr[1] = vy;
            }
            return mathstr;
        }

        /**
         * time calculate
         */
        private void calcTimePass() {
            if (isAnimation) {
                long curTime = System.currentTimeMillis();
                long gapTime = curTime - animStartTime;
                long animDuration = mCurveGraphVO.getAnimation().getDuration();
                if (gapTime >= animDuration)
                    gapTime = animDuration;

                anim = (float) gapTime / (float) animDuration;
            } else {
                isDirty = false;
            }
        }

        /**
         * check graph Curve animation
         */
        private void isAnimation() {
            if (mCurveGraphVO.getAnimation() != null) {
                isAnimation = true;
            } else {
                isAnimation = false;
            }
            animationType = mCurveGraphVO.getAnimation().getAnimation();
        }

        /**
         * check graph Curve region animation
         */
        private void isDrawRegion() {
            if (mCurveGraphVO.isDrawRegion()) {
                isDrawRegion = true;
            } else {
                isDrawRegion = false;
            }
        }

        /**
         * draw Base Line
         */
        private void drawBaseLine(GraphCanvasWrapper graphCanvas) {
            for (int i = 1; mCurveGraphVO.getIncrement() * i <= mCurveGraphVO.getMaxValue(); i++) {

                float y = yLength * mCurveGraphVO.getIncrement() * i / mCurveGraphVO.getMaxValue();

                graphCanvas.drawLine(-50, y, chartXLength, y, pBaseLineX);
            }
        }

        /**
         * set graph Curve color
         */
        private void setPaint() {
            p = new Paint();
            p.setFlags(Paint.ANTI_ALIAS_FLAG);
            p.setAntiAlias(true); //text anti alias
            p.setFilterBitmap(true); // bitmap anti alias
            p.setColor(Color.BLUE);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);

            pCircle = new Paint();
            pCircle.setFlags(Paint.ANTI_ALIAS_FLAG);
            pCircle.setAntiAlias(true); //text anti alias
            pCircle.setFilterBitmap(true); // bitmap anti alias
            pCircle.setColor(0xfffb5f5b);
            pCircle.setStrokeWidth(3);
            pCircle.setStyle(Paint.Style.FILL_AND_STROKE);

            pCurve = new Paint();
            pCurve.setFlags(Paint.ANTI_ALIAS_FLAG);
            pCurve.setAntiAlias(true); //text anti alias
            pCurve.setFilterBitmap(true); // bitmap anti alias
            pCurve.setShader(new LinearGradient(0, 300f, 0, 0f, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

            pBaseLine = new Paint();
            pBaseLine.setFlags(Paint.ANTI_ALIAS_FLAG);
            pBaseLine.setAntiAlias(true); //text anti alias
            pBaseLine.setFilterBitmap(true); // bitmap anti alias
            pBaseLine.setColor(0xfff0f0f0);
            pBaseLine.setStrokeWidth(3);

            pBaseLineX = new Paint();
            pBaseLineX.setFlags(Paint.ANTI_ALIAS_FLAG);
            pBaseLineX.setAntiAlias(true); //text anti alias
            pBaseLineX.setFilterBitmap(true); // bitmap anti alias
            pBaseLineX.setColor(0xfff0f0f0);
            pBaseLineX.setStyle(Paint.Style.FILL);

            pMarkText = new TextPaint();
            pMarkText.setFlags(Paint.ANTI_ALIAS_FLAG);
            pMarkText.setAntiAlias(true); //text anti alias
            pMarkText.setColor(0xff646464);
        }

        /**
         * draw Graph Region
         */
        private void drawGraphRegion(GraphCanvasWrapper graphCanvas) {
            if (isDrawRegion) {
                if (isAnimation) {
                    drawGraphRegionWithAnimation(graphCanvas);
                } else {
                    drawGraphRegionWithoutAnimation(graphCanvas);
                }
            }
        }

        /**
         * draw Graph
         */
        private void drawGraph(GraphCanvasWrapper graphCanvas) {
            if (isAnimation) {
                drawGraphWithAnimation(graphCanvas);
            } else {
                drawGraphWithoutAnimation(graphCanvas);
            }
        }

        /**
         * draw graph without animation
         */
        private void drawGraphRegionWithoutAnimation(GraphCanvasWrapper graphCanvas) {
            boolean isDrawRegion = mCurveGraphVO.isDrawRegion();

            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                GraphPath regionPath = new GraphPath(width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());

                boolean firstSet = false;
                float xGap = xLength / (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);

                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);
                // Creates a monotone cubic spline from a given set of control points.
                spline = Spline.createMonotoneCubicSpline(x, y);

                p.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                pCircle.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());

                spline = Spline.createMonotoneCubicSpline(x, y);

                // draw Region
                for (float j = x[0]; j < x[x.length - 1]; j++) {
                    if (!firstSet) {
                        regionPath.moveTo(j, spline.interpolate(j));
                        firstSet = true;
                    } else
                        regionPath.lineTo((j + 1), spline.interpolate((j + 1)));
                }

                if (isDrawRegion) {
                    regionPath.lineTo(x[x.length - 1], 0);
                    regionPath.lineTo(0, 0);

                    Paint pBg = new Paint();
                    pBg.setFlags(Paint.ANTI_ALIAS_FLAG);
                    pBg.setAntiAlias(true); //text anti alias
                    pBg.setFilterBitmap(true); // bitmap anti alias
                    pBg.setStyle(Paint.Style.FILL);
                    pBg.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                    graphCanvas.getCanvas().drawPath(regionPath, pBg);
                }
            }
        }

        /**
         * draw graph with animation
         */
        private void drawGraphRegionWithAnimation(GraphCanvasWrapper graphCanvas) {
            //for draw animation
            boolean isDrawRegion = mCurveGraphVO.isDrawRegion();

            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                GraphPath regionPath = new GraphPath(width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());

                boolean firstSet = false;
                int xl = (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);
                if (xl == 0)
                    xl = 1;
                float xGap = xLength / xl;

                float moveX = 0;
                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);
                // Creates a monotone cubic spline from a given set of control points.
                if (x == null || y == null || x.length != y.length || x.length < 2)
                    continue;
                spline = Spline.createMonotoneCubicSpline(x, y);

                p.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                pCircle.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());

                // draw line
                for (float j = x[0]; j <= x[x.length - 1]; j++) {
                    if (!firstSet) {
                        regionPath.moveTo(j, spline.interpolate(j));
                        firstSet = true;

                    } else {
                        moveX = j * anim;
                        regionPath.lineTo(moveX, spline.interpolate(moveX));
                    }
                }

                if (isDrawRegion) {
                    if (animationType == GraphAnimation.CURVE_REGION_ANIMATION_1) {
                        moveX += xGap * anim;
                        if (moveX >= xLength) {
                            moveX = xLength;
                        }
                    }

                    regionPath.lineTo(moveX, 0);
                    regionPath.lineTo(0, 0);

                    Paint pBg = new Paint();
                    pBg.setFlags(Paint.ANTI_ALIAS_FLAG);
                    pBg.setAntiAlias(true); //text anti alias
                    pBg.setFilterBitmap(true); // bitmap anti alias
                    pBg.setStyle(Paint.Style.FILL);
                    pBg.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                    graphCanvas.getCanvas().drawPath(regionPath, pBg);
                }
                if (anim == 1)
                    isDirty = false;
            }
        }

        /**
         * draw graph without animation
         */
        private void drawGraphWithoutAnimation(GraphCanvasWrapper graphCanvas) {
            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                GraphPath curvePath = new GraphPath(width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());

                boolean firstSet = false;
                float xGap = xLength / (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);

                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);
                // Creates a monotone cubic spline from a given set of control points.
                spline = Spline.createMonotoneCubicSpline(x, y);

                p.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                pCircle.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());


                // draw line
                for (float j = x[0]; j < x[x.length - 1]; j++) {
                    if (!firstSet) {
                        curvePath.moveTo(j, spline.interpolate(j));
                        firstSet = true;
                    } else
                        curvePath.lineTo((j + 1), spline.interpolate((j + 1)));
                }

                // draw point
                TextPaint tP = new TextPaint(pMarkText);
                tP.setColor(pCircle.getColor());
                for (int j = 0; j < mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length; j++) {
                    float pointX = xGap * j;
                    float pointY = yLength * mCurveGraphVO.getArrGraph().get(i).getCoordinateArr()[j] / mCurveGraphVO.getMaxValue();
                    graphCanvas.drawCircle(pointX, pointY, 4, pCircle);
                    String txt = mCurveGraphVO.getArrGraph().get(i).getCoordinateTitleArr()[j];
                    int tW = (int) pMarkText.measureText(txt);
                    tW = tW >> 1;
                    int tH = getFontHeight(pMarkText.getTextSize());

                    graphCanvas.drawText(txt, x[j] - tW, y[j] + tH, tP);
                }
            }
        }

        /**
         * draw graph with animation
         */
        private void drawGraphWithAnimation(GraphCanvasWrapper graphCanvas) {
            //for draw animation
            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                GraphPath curvePath = new GraphPath(width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());

                boolean firstSet = false;
                int xl = (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);
                if (xl == 0)
                    xl = 1;
                float xGap = xLength / xl;
                float pointNum = (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length * anim) / 1;

                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);
                // Creates a monotone cubic spline from a given set of control points.
                if (x == null || y == null || x.length != y.length || x.length < 2)
                    continue;
                spline = Spline.createMonotoneCubicSpline(x, y);

                p.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());
                pCircle.setColor(mCurveGraphVO.getArrGraph().get(i).getColor());

                // draw line
                for (float j = x[0]; j <= x[x.length - 1]; j++) {
                    if (!firstSet) {
                        curvePath.moveTo(j, spline.interpolate(j));
                        firstSet = true;

                    } else {
                        curvePath.lineTo(((j) * anim), spline.interpolate(((j) * anim)));
                    }
                }
                graphCanvas.getCanvas().drawPath(curvePath, p);

                // draw point
                TextPaint tP = new TextPaint(pMarkText);
                tP.setColor(pCircle.getColor());
                for (int j = 0; j < pointNum + 1; j++) {
                    if (j < mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length) {
                        String txt = mCurveGraphVO.getArrGraph().get(i).getCoordinateTitleArr()[j];
                        int tW = (int) pMarkText.measureText(txt);
                        tW = tW >> 1;
                        int tH = getFontHeight(pMarkText.getTextSize());
                        float tY = y[j] + tH;
                        if (graphCanvas.mMt.calcY(tY)<=0)
                            tY = y[j] - tH;
                        graphCanvas.drawText(txt, x[j] - tW, tY, tP);
                        graphCanvas.drawCircle(x[j], y[j], 4, pCircle);
                    }
                }
                if (anim == 1)
                    isDirty = false;
            }
        }

        public int getFontHeight(float fontSize) {
            Paint paint = new Paint();
            paint.setTextSize(fontSize);
            Paint.FontMetrics fm = paint.getFontMetrics();
            return (int) Math.ceil(fm.descent - fm.ascent);
        }

        /**
         * draw X Mark
         */
        private void drawXMark(GraphCanvasWrapper graphCanvas) {
            float x = 0;
            float xGap = 0;
            x = (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length - 1);
            if (x != 0)
                xGap = xLength / x;
            else
                xGap = xLength;

            for (int i = 0; i < mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length; i++) {
                x = xGap * i;

                graphCanvas.drawLine(x, 0, x, -10, pBaseLine);
            }
        }

        /**
         * draw Y Mark
         */
        private void drawYMark(GraphCanvasWrapper canvas) {
            for (int i = 0; mCurveGraphVO.getIncrement() * i <= mCurveGraphVO.getMaxValue(); i++) {

                float y = yLength * mCurveGraphVO.getIncrement() * i / mCurveGraphVO.getMaxValue();

                canvas.drawLine(0, y, -10, y, pBaseLine);
            }
        }

        /**
         * draw X Text
         */
        private void drawXText(GraphCanvasWrapper graphCanvas) {
            float x = 0;
            float xGap = 0;
            x = (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length - 1);
            if (x != 0)
                xGap = xLength / x;
            else
                xGap = xLength;

            pMarkText.setColor(0xff646464);
            for (int i = 0; i < mCurveGraphVO.getLegendArr().length; i++) {
                x = xGap * i;

                String text = mCurveGraphVO.getLegendArr()[i];
                pMarkText.measureText(text);
                pMarkText.setTextSize(20);
                Rect rect = new Rect();
                pMarkText.getTextBounds(text, 0, text.length(), rect);
                StaticLayout layout = new StaticLayout(text, pMarkText, text.trim().length() > 9 ? rect.width() - 15 : rect.width() + 15,
                        Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
                graphCanvas.getCanvas().save();
                graphCanvas.getCanvas().translate(graphCanvas.mMt.calcX(x - (rect.width() / 2)), graphCanvas.mMt.calcY(-(20 + rect.height())));
                layout.draw(graphCanvas.getCanvas());
                graphCanvas.getCanvas().restore();
            }
        }

        /**
         * draw Y Text
         */
        private void drawYText(GraphCanvasWrapper graphCanvas) {
            for (int i = 0; mCurveGraphVO.getIncrement() * i <= mCurveGraphVO.getMaxValue(); i++) {

                String mark = Float.toString(mCurveGraphVO.getIncrement() * i);
                float y = yLength * mCurveGraphVO.getIncrement() * i / mCurveGraphVO.getMaxValue();
                pMarkText.measureText(mark);
                pMarkText.setTextSize(20);
                Rect rect = new Rect();
                pMarkText.getTextBounds(mark, 0, mark.length(), rect);
//				Log.e(TAG, "rect = height()" + rect.height());
//				Log.e(TAG, "rect = width()" + rect.width());
                graphCanvas.drawText(mark, -(rect.width() + 20), y - rect.height() / 2, pMarkText);
            }
        }

        /**
         * set point X Coordinate
         */
        private float[] setAxisX(float xGap, int graphNum) {
            float[] axisX = new float[mCurveGraphVO.getArrGraph().get(graphNum).getCoordinateArr().length];

            for (int i = 0; i < mCurveGraphVO.getArrGraph().get(graphNum).getCoordinateArr().length; i++) {
                axisX[i] = xGap * i;
            }
            return axisX;
        }

        /**
         * set point Y Coordinate
         */
        private float[] setAxisY(int graphNum) {
            float[] axisY = new float[mCurveGraphVO.getArrGraph().get(graphNum).getCoordinateArr().length];

            for (int i = 0; i < mCurveGraphVO.getArrGraph().get(graphNum).getCoordinateArr().length; i++) {
                axisY[i] = yLength * mCurveGraphVO.getArrGraph().get(graphNum).getCoordinateArr()[i] / mCurveGraphVO.getMaxValue();
            }
            return axisY;
        }
    }
}

