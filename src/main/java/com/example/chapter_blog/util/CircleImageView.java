package com.example.chapter_blog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

        // 计算圆形图像的直径
        int diameter = Math.min(w, h);

        // 创建一个圆形的位图
        Bitmap roundBitmap = getCroppedBitmap(bitmap, diameter);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private static Bitmap getCroppedBitmap(Bitmap bmp, int diameter) {
        Bitmap sbmp;
        if (bmp.getWidth() != diameter || bmp.getHeight() != diameter) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / diameter;
            sbmp = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final BitmapShader shader = new BitmapShader(sbmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setAntiAlias(true);
        paint.setShader(shader);

        float radius = diameter / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        return output;
    }
}