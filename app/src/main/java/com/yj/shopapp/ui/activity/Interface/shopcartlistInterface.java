package com.yj.shopapp.ui.activity.Interface;

/**
 * Created by LK on 2017/12/27.
 *
 * @author LK
 */

public class shopcartlistInterface {
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position 元素位置
         */
        void doIncrease(int position);

        /**
         * 删减操作
         *
         * @param position 元素位置
         */
        void doDecrease(int position);

        /**
         * 统计数据
         */
        void statistics();

        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }

}
