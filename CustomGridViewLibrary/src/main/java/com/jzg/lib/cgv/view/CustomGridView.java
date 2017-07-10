package com.jzg.lib.cgv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzg.lib.cgv.R;
import com.jzg.lib.cgv.adapter.CustomGridViewAdapter;

import java.util.ArrayList;
/**
 * 自定义CustomGridView布局 <br/>
 * Created by lipanquan on 2017/5/2.<br />
 * phoneNumber:18500214652 <br />
 * email:lipq@jingzhengu.com <br />
 *
 * @author lipanquan   2017/5/2
 */
public class CustomGridView extends ViewGroup implements View.OnClickListener {

    /**
     * 屏幕的宽度
     */
//    private int screenWidth;

    /**
     * 屏幕的高度
     */
//    private int screenHeight;

    /**
     * 每行显示多少个item > 0
     */
    private int allCountLine;

    /**
     * 行间距
     */
    private int line_spacing;

    /**
     * 最多可选择多少个item > 0
     */
//    private int max_select_item_number;

    /**
     * item左右边距
     */
    private int item_margin_left_or_right;

    /**
     * item内边距
     */
    private int item_padding;

    /**
     * item字体的大小
     */
    private int item_text_size;

    /**
     * item字体的颜色
     */
    private int item_text_color;

    /**
     * item选中时的字体颜色
     */
    private int item_text_selected_color;

    /**
     * item正常时的背景
     */
    private int item_background_normal;

    /**
     * item选中时的背景
     */
    private int item_background_selected;

    /**
     * 控件测量后的宽度
     */
//    private int measuredWidth;

    /**
     * 设置适配器
     */
    private CustomGridViewAdapter adapter;

    /**
     * 控件的最大高度
     */
    private int maxHeight;

    /**
     * item点击事件回调接口
     */
    private OnCustomGridViewItemClickListener listener;

    /**
     * 已选择的ID集合
     */
    private ArrayList<Integer> selectedIds = new ArrayList<>();

    /**
     * item是否可点击
     */
    private boolean enabled = true;

    public CustomGridView(Context context) {
        this(context, null);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        Point screenSize = new Point();
        ((WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(screenSize);
//        screenWidth = screenSize.x;
//        screenHeight = screenSize.y;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomGridView);
        if (typedArray != null) {
            allCountLine = typedArray.getInteger(R.styleable.CustomGridView_all_count_line, 4);
            line_spacing = dp2px(typedArray.getInteger(R.styleable.CustomGridView_line_spacing, 15));
//            max_select_item_number = dp2px(typedArray.getInteger(R.styleable.CustomGridView_max_select_item_number, 1));
            item_margin_left_or_right = dp2px(typedArray.getInteger(R.styleable.CustomGridView_item_margin_left_or_right, 10));
            item_padding = dp2px(typedArray.getInteger(R.styleable.CustomGridView_item_padding, 5));
//            item_text_size = px2sp(typedArray.getInteger(R.styleable.CustomGridView_item_text_size, 16));
//            item_text_size = px2sp(typedArray.getInteger(R.styleable.CustomGridView_item_text_size, 16));
            item_text_size = typedArray.getInteger(R.styleable.CustomGridView_item_text_size, 16);
            item_text_color = typedArray.getColor(R.styleable.CustomGridView_item_text_color, Color.WHITE);
            item_text_selected_color = typedArray.getColor(R.styleable.CustomGridView_item_text_selected_color, Color.WHITE);
            item_background_normal = typedArray.getResourceId(R.styleable.CustomGridView_item_background_normal, Color.BLUE);
            item_background_selected = typedArray.getResourceId(R.styleable.CustomGridView_item_background_selected, Color.RED);
            typedArray.recycle();
        }

        if (allCountLine == 0) {
            throw new RuntimeException("请设置all_count_line的值大于0个");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = measuredWidth;
        availableWidth -= this.getPaddingLeft();
        availableWidth -= this.getPaddingRight();
        if (this.adapter != null) {
            this.removeAllViews();
            int count = this.adapter.getCount();
            for (int i = 0; i < count; i++) {
                // 得到实际的item的宽度
                int itemWidth = (availableWidth / allCountLine) - (item_margin_left_or_right * 2);
                View itemView = View.inflate(getContext(), R.layout.custom_gridview_adapter_item, null);
                TextView tvItem = (TextView) itemView.findViewById(R.id.tvItem);
                tvItem.setTextColor(item_text_color);
                tvItem.setTextSize(item_text_size);
                tvItem.setText(this.adapter.getItemContent(i));
                StateListDrawable drawable = new StateListDrawable();
                drawable.addState(new int[]{android.R.attr.state_selected}, getResources().getDrawable(item_background_selected));
                drawable.addState(new int[0], getResources().getDrawable(item_background_normal));
                if (Build.VERSION.SDK_INT >= 16) {
                    tvItem.setBackground(drawable);
                } else {
                    tvItem.setBackgroundDrawable(drawable);
                }
                tvItem.setPadding(tvItem.getPaddingLeft(), item_padding, tvItem.getPaddingRight(), item_padding);
                LinearLayout.LayoutParams params;
                tvItem.measure(0, 0);
                if (this.selectedIds.size() > 0) {
                    for (Integer itg : this.selectedIds) {
                        if (itg == i) {
                            tvItem.setSelected(true);
                            tvItem.setTextColor(item_text_selected_color);
                        }
                    }
                }
                int itemHeight = tvItem.getMeasuredHeight();
                params = (LinearLayout.LayoutParams) tvItem.getLayoutParams();
                params.setMargins(item_margin_left_or_right, 0, item_margin_left_or_right, 0);
                params.width = itemWidth;
                params.height = itemHeight;
                itemView.setLayoutParams(params);

                int maxRows = (count / allCountLine) + (count % allCountLine == 0 ? 0 : 1);
                itemView.measure(0, 0);
                maxHeight = itemView.getMeasuredHeight() * maxRows + (maxRows * line_spacing);

                String textCount = this.adapter.getItemContent(i);
                tvItem.setText(textCount);
                if (enabled)
                    itemView.setOnClickListener(this);
                itemView.setTag(tvItem);
                this.addView(itemView, i);
            }
        }
        setMeasuredDimension(measuredWidth, maxHeight);
    }

    /**
     * 设置全部item是否可点击
     *
     * @param enabled item是否可点击
     */
    public void setAllItemEnabled(boolean enabled) {
        this.enabled = enabled;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setOnClickListener(enabled ? this : null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int maxWidth = right - left;
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int row = 0;
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width;
                y = row * height + height;
                count++;
                if (x > maxWidth) {
                    x = width;
                    if (count > 1) {
                        y = ++row * height + height;
                    } else {
                        y = row * height + height;
                        count = 1;
                    }
                }
                y += row * line_spacing;
                child.layout(x - width, row * line_spacing + y - height, x,
                        row * line_spacing + y);
            }
        }
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器对象
     */
    public void setAdapter(CustomGridViewAdapter adapter) {
        this.adapter = adapter;
        requestLayout();
    }

    /**
     * 设置item点击事件回调接口
     *
     * @param listener item点击事件回调接口
     */
    public void setOnItemClickListener(OnCustomGridViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        TextView tvItem = (TextView) v.getTag();
        boolean itemIsSelected = tvItem.isSelected();
        int position = setAllNuSelectedAndReturnPosition(v);
        if (itemIsSelected) {
            tvItem.setTextColor(item_text_color);
            tvItem.setSelected(false);
            this.selectedIds.clear();
        } else {
            tvItem.setTextColor(item_text_selected_color);
            tvItem.setSelected(true);
            this.selectedIds.clear();
            this.selectedIds.add(position);
        }
        if (this.listener != null)
            this.listener.onCustomGridViewItemClick(this, v, position);
    }

    /**
     * 设置所有的item恢复未选中状态，并返回当前选中的view的position
     *
     * @param v 当前选中的view
     * @return 当前选中的view的position
     */
    private int setAllNuSelectedAndReturnPosition(View v) {
        int position = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = this.getChildAt(i);
            if (view.equals(v)) {
                position = i;
            }
            TextView tvItem = (TextView) view.getTag();
            tvItem.setSelected(false);
            tvItem.setTextColor(item_text_color);
        }
        return position;
    }

    /**
     * 获取当前选中的position集合
     *
     * @return 当前选中的position集合
     */
    public ArrayList<Integer> getSelectedList() {
        return this.selectedIds;
    }

    /**
     * 设置当前选中的position
     *
     * @return 当前选中的position
     */
    public void setSelectedList(int position) {
        if (this.adapter.getCount() > position) {
            this.selectedIds.clear();
            this.selectedIds.add(position);
            if (this.getChildCount() > position) {
                TextView tvItem = (TextView) this.getChildAt(position).getTag();
                boolean itemIsSelected = tvItem.isSelected();
                setAllNuSelectedAndReturnPosition(this.getChildAt(position));
                if (itemIsSelected) {
                    tvItem.setTextColor(item_text_selected_color);
                    tvItem.setSelected(true);
                }
            }
        }
    }

    /**
     * dp转px
     *
     * @param dpValue sp值
     * @return 对应的px值
     */
    public int dp2px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param pxValue px值
     * @return 对应的px值
     */
    public int px2sp(int pxValue) {
        return (int) (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                pxValue, getResources().getDisplayMetrics());
    }

    /**
     * 自定义CustomGridView——item点击事件回调接口 <br/>
     * Created by lipanquan on 2017/5/3.<br />
     * phoneNumber:18500214652 <br />
     * email:lipq@jingzhengu.com <br />
     *
     * @author lipanquan   2017/5/3
     */
    public interface OnCustomGridViewItemClickListener {

        /**
         * 自定义CustomGridView——item点击事件回调
         *
         * @param parent   CustomGridView
         * @param view     点击的view
         * @param position 位置
         */
        void onCustomGridViewItemClick(CustomGridView parent, View view, int position);
    }
}
