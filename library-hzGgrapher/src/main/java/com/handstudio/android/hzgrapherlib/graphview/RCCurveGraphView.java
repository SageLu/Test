package com.handstudio.android.hzgrapherlib.graphview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.handstudio.android.hzgrapherlib.canvas.GraphCanvasWrapper;
import com.handstudio.android.hzgrapherlib.error.ErrorCode;
import com.handstudio.android.hzgrapherlib.error.ErrorDetector;
import com.handstudio.android.hzgrapherlib.util.Spline;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraphVO;

import java.util.WeakHashMap;

/**
 * Created by loo on 16-8-4.
 * 柱状图
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RCCurveGraphView extends TextureView implements TextureView.SurfaceTextureListener{

    public static final String TAG = "CurveGraphView";
    private Surface surface;
    private DrawThread mDrawThread;

    private CurveGraphVO mCurveGraphVO = null;
    private Spline spline = null;
    public int customColor = 0;//设置分时购买特殊颜色，没有设置就是普通ｖｉｅｗ


    //Constructor
    public RCCurveGraphView(Context context, CurveGraphVO vo) {
        super(context);
        mCurveGraphVO = vo;
        initView(context, vo);
    }

    private void initView(Context context, CurveGraphVO vo) {
        ErrorCode ec = ErrorDetector.checkGraphObject(vo);
        ec.printError();
        setBackgroundColor(0x11ffffff);
        setSurfaceTextureListener(this);
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawThread != null)
            mDrawThread.isRun = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawThread != null)
            mDrawThread.isRun = false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        if (mDrawThread == null) {
            View view = (View) getParent();
            if (view != null)
                view.setBackgroundColor(0xffffffff);
            surface=new Surface(surfaceTexture);
            mDrawThread = new DrawThread(surface, getContext());
            mDrawThread.start();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mDrawThread != null) {
            mDrawThread.setRunFlag(false);
            mDrawThread = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    class DrawThread extends Thread {
        final Surface mHolder;
        Context mCtx;

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
        Paint pMarkText = new TextPaint();

        //animation
        float anim = 0.0f;
        boolean isAnimation = false;
        boolean isDrawRegion = false;
        long animStartTime = -1;
        int animationType = 0;

        WeakHashMap<Integer, Bitmap> arrIcon = new WeakHashMap<Integer, Bitmap>();
        Bitmap bg = null;

        public DrawThread(Surface surface, Context context) {
            mHolder = surface;
            mCtx = context;

            int size = mCurveGraphVO.getArrGraph().size();
            for (int i = 0; i < size; i++) {
                int bitmapResource = mCurveGraphVO.getArrGraph().get(i).getBitmapResource();
                if (bitmapResource != -1) {
                    arrIcon.put(i, BitmapFactory.decodeResource(getResources(), bitmapResource));
                } else {
                    if (arrIcon.get(i) != null) {
                        arrIcon.remove(i);
                    }
                }
            }
            int bgResource = mCurveGraphVO.getGraphBG();
            if (bgResource != -1) {
                Bitmap tempBg = BitmapFactory.decodeResource(getResources(), bgResource);
                bg = Bitmap.createScaledBitmap(tempBg, width, height, true);
                tempBg.recycle();
            }
        }

        public void setRunFlag(boolean bool) {
            isRun = bool;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            GraphCanvasWrapper graphCanvasWrapper = null;

            setPaint();
            isAnimation();
            isDrawRegion();

            pCircle.setColor(mCurveGraphVO.getArrGraph().get(0).getColor());

            animStartTime = System.currentTimeMillis();

            Rect rect=new Rect(getLeft(),getTop(),getRight(),getBottom());

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

                calcTimePass();
                canvas = mHolder.lockCanvas(rect);
                if (canvas == null) {
                    return;
                }
                graphCanvasWrapper = new GraphCanvasWrapper(canvas, width, height, mCurveGraphVO.getPaddingLeft(), mCurveGraphVO.getPaddingBottom());
                synchronized (mHolder) {
                    synchronized (touchLock) {

                        try {
                            //bg color
                            canvas.drawColor(Color.WHITE);
                            if (bg != null) {
                                canvas.drawBitmap(bg, 0, 0, null);
                            }

                            // x coord dot Line
                            drawBaseLine(graphCanvasWrapper);

                            // x coord
                            graphCanvasWrapper.drawLine(-50, 0, chartXLength, 0, pCircle);

                            drawAL((int) graphCanvasWrapper.mMt.calcX(0), (int) graphCanvasWrapper.mMt.calcY(0)
                                    , (int) graphCanvasWrapper.mMt.calcX(chartXLength), (int) graphCanvasWrapper.mMt.calcY(0), canvas);

                            // x, y coord text
                            drawXText(graphCanvasWrapper);

                            // Draw Graph
                            drawGraph(graphCanvasWrapper);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (graphCanvasWrapper.getCanvas() != null&&isRun) {
                                mHolder.unlockCanvasAndPost(graphCanvasWrapper.getCanvas());
                            }
                        }

                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
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
                isDirty = true;
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
                animationType = mCurveGraphVO.getAnimation().getAnimation();
            } else {
                isAnimation = false;
            }
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
            pCircle.setColor(Color.BLUE);
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
            pBaseLine.setColor(Color.GRAY);
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
            pMarkText.setColor(Color.BLACK);
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
        private void drawGraphWithoutAnimation(GraphCanvasWrapper graphCanvas) {
            pCircle.setColor(mCurveGraphVO.getArrGraph().get(0).getColor());
            pMarkText.setColor(pCircle.getColor());
            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                float xGap = xLength / (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);

                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);


                Bitmap icon = arrIcon.get(i);

                // draw point
                for (int j = 0; j < mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length; j++) {
                    float pointX = xGap * j;
                    float pointY = yLength * mCurveGraphVO.getArrGraph().get(i).getCoordinateArr()[j] / mCurveGraphVO.getMaxValue();
                    if (j >= 2) {
                        if (customColor != 0) {
                            pCircle.setColor(customColor);
                            pMarkText.setColor(pCircle.getColor());
                        }
                    }
                    if (icon == null) {
                        String txt = mCurveGraphVO.getArrGraph().get(i).getCoordinateTitleArr()[j];
                        int tW = (int) pMarkText.measureText(txt);
                        tW = tW >> 1;
                        int tH = getFontHeight(pMarkText.getTextSize());
                        graphCanvas.drawText(txt, x[j] - tW, y[j] + tH, pMarkText);
                    } else {
                        graphCanvas.drawBitmapIcon(icon, pointX, pointY, null);
                    }

                    graphCanvas.drawRect(x[j] - 30, 0, x[j] + 30, y[j], pCircle);
                }
            }

            pCircle.setColor(mCurveGraphVO.getArrGraph().get(0).getColor());
        }

        /**
         * draw graph with animation
         */
        private void drawGraphWithAnimation(GraphCanvasWrapper graphCanvas) {
            //for draw animation
            pCircle.setColor(mCurveGraphVO.getArrGraph().get(0).getColor());
            pMarkText.setColor(pCircle.getColor());
            for (int i = 0; i < mCurveGraphVO.getArrGraph().size(); i++) {
                float xGap = xLength / (mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length - 1);
                float pointNum = (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length * anim) / 1;

                float[] x = setAxisX(xGap, i);
                float[] y = setAxisY(i);

                Bitmap icon = arrIcon.get(i);

                // draw point and rect
                for (int j = 0; j < pointNum + 1; j++) {
                    if (j >= 2) {
                        if (customColor != 0) {
                            pCircle.setColor(customColor);
                            pMarkText.setColor(pCircle.getColor());
                        }
                    }
                    if (j < mCurveGraphVO.getArrGraph().get(i).getCoordinateArr().length) {
                        if (icon == null) {
                            String txt = mCurveGraphVO.getArrGraph().get(i).getCoordinateTitleArr()[j];
                            int tW = (int) pMarkText.measureText(txt);
                            tW = tW >> 1;
                            int tH = getFontHeight(pMarkText.getTextSize());
                            graphCanvas.drawText(txt, x[j] - tW, y[j] + tH, pMarkText);
                        } else {
                            graphCanvas.drawBitmapIcon(icon, x[j], y[j], null);
                        }
                        float left = x[j] - 30;
                        float right = x[j] + 30;
                        float maxY = y[j];
                        graphCanvas.drawRect(left, 2.5f, right, maxY, pCircle);

                    }
                }
                if (anim == 1)
                    isDirty = false;
            }
            pCircle.setColor(mCurveGraphVO.getArrGraph().get(0).getColor());
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

            float xGap = xLength / (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length - 1);
            for (int i = 0; i < mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length; i++) {
                x = xGap * i;

                graphCanvas.drawLine(x, 0, x, -10, pBaseLine);
            }
        }

        /**
         * draw X Text
         */
        private void drawXText(GraphCanvasWrapper graphCanvas) {
            float x = 0;
            pMarkText.setColor(0xff646464);
            float xGap = xLength / (mCurveGraphVO.getArrGraph().get(0).getCoordinateArr().length - 1);
            for (int i = 0; i < mCurveGraphVO.getLegendArr().length; i++) {
                x = xGap * i;

                String text = mCurveGraphVO.getLegendArr()[i];
                pMarkText.measureText(text);
                pMarkText.setTextSize(20);
                Rect rect = new Rect();
                pMarkText.getTextBounds(text, 0, text.length(), rect);
                StaticLayout layout = new StaticLayout(text, (TextPaint) pMarkText, text.trim().length() > 9 ? rect.width() - 15 : rect.width() + 15, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
                graphCanvas.getCanvas().save();
                graphCanvas.getCanvas().translate(graphCanvas.mMt.calcX(x - (rect.width() / 2)), graphCanvas.mMt.calcY(-(20 + rect.height())));
                layout.draw(graphCanvas.getCanvas());
                graphCanvas.getCanvas().restore();
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
                ;
            }
            return axisY;
        }
    }
}

