package com.jzg.lib.cgv.adapter;

import java.util.ArrayList;

/**
 * <br/>
 * Created by lipanquan on 2017/5/2.<br />
 * phoneNumber:18500214652 <br />
 * email:lipq@jingzhengu.com <br />
 *
 * @author lipanquan   2017/5/2
 */
public final class CustomGridViewAdapter {

    private ArrayList<String> allItemStrs;

    public CustomGridViewAdapter(ArrayList<String> allItemStrs) {
        this.allItemStrs = allItemStrs;
    }

    /**
     * 获取总的Item个数
     *
     * @return 总的Item个数
     */
    public int getCount() {
        return allItemStrs != null ? this.allItemStrs.size() : 0;
    }

    /**
     * 获取指定位置Item上要显示的文字
     *
     * @param position 指定位置
     * @return 指定位置Item上要显示的文字
     */
    public String getItemContent(int position) {
        return this.allItemStrs.get(position);
    }
}