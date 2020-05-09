package com.graduation.android.readme.base.image;

/**
 * function： 圆角方向
 *
 * @author： linjiliang  2019/4/17
 */
public class RoundedDirection {

    public boolean leftTop;
    public boolean rightTop;
    public boolean leftBottom;
    public boolean rightBottom;

    public RoundedDirection(boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.leftBottom = leftBottom;
        this.rightBottom = rightBottom;
    }
}
