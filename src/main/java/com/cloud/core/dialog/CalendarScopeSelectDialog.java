package com.cloud.core.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.core.R;
import com.cloud.core.dialog.plugs.DialogPlus;
import com.cloud.core.events.Action3;
import com.cloud.core.icons.IconView;
import com.cloud.core.utils.DateUtils;
import com.cloud.core.utils.GlobalUtils;
import com.cloud.core.utils.PixelUtils;
import com.cloud.core.utils.ToastUtils;
import com.cloud.core.view.VariableButton;
import com.cloud.core.view.calendar.Calendar;
import com.cloud.core.view.calendar.CalendarView;


/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/10/29
 * Description:日历范围选择dialog
 * Modifier:
 * ModifyContent:
 */
public class CalendarScopeSelectDialog {

    private CalendarViewHolder holder = null;
    private DialogPlus dialogPlus = null;

    /**
     * 选择时间回调
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    protected void onSelectTimeRange(long startTime, long endTime) {

    }

    public void show(Context context) {
        DialogManager.DialogManagerBuilder<Object> builder = DialogManager.getInstance().builder(context, R.layout.cl_calendar_scope_select_view);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        dialogPlus = builder.show(new Action3<View, DialogPlus, Object>() {
            @Override
            public void call(View contentView, DialogPlus dialogPlus, Object object) {
                holder = new CalendarViewHolder(contentView);
                tabItems.setTabnum(2);
                holder.startDateLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabItems.onTabChanged(0);
                        tabItems.switchSelector(holder.indicatorV, 0, 0.01f);
                    }
                });
                holder.endDateLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabItems.onTabChanged(1);
                        tabItems.switchSelector(holder.indicatorV, 1, 2.0f);
                    }
                });
                int tabIndicatorWidth = Math.max(holder.getStartIndicatorWidth(), holder.getEndIndicatorWidth());
                int containerWidth = holder.getContainerWidth();
                if (containerWidth > 0 && tabIndicatorWidth > containerWidth) {
                    tabIndicatorWidth = containerWidth;
                }
                tabItems.initTabLineWidth(holder.indicatorV, tabItems.getTabnum(), tabIndicatorWidth);
                tabItems.switchSelector(holder.indicatorV, 0, 0.01f);
            }
        });
    }

    private class TabItems {
        private int screenWidth = 0;
        private int currentItem = 0;
        private int tabnum = 0;
        private int indicatorWidth = 0;

        public void onTabChanged(int position) {

        }

        public void setTabnum(int tabnum) {
            this.tabnum = tabnum;
        }

        public int getTabnum() {
            return this.tabnum;
        }

        /**
         * 设置滑动条的宽度为屏幕的1/n(根据Tab的个数而定)
         */
        public void initTabLineWidth(ImageView cursor, int tabnum, int indicatorWidth) {
            this.tabnum = tabnum;
            this.indicatorWidth = indicatorWidth;
            screenWidth = GlobalUtils.getScreenWidth(cursor.getContext());
            if (cursor != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor
                        .getLayoutParams();
                int itemWidth = screenWidth / tabnum;
                if (indicatorWidth > itemWidth) {
                    indicatorWidth = itemWidth;
                }
                lp.width = indicatorWidth > 0 ? indicatorWidth : itemWidth;
                cursor.setLayoutParams(lp);
            }
        }

        public void switchSelector(ImageView cursor, int position, float offset) {
            if (cursor != null) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor
                        .getLayoutParams();
                double itemwidth = screenWidth * 1.0 / tabnum;
                double moffset = (itemwidth - indicatorWidth) / 2;
                if (currentItem == position) {
                    lp.leftMargin = (int) (offset * itemwidth + currentItem * itemwidth + moffset);
                } else {
                    lp.leftMargin = (int) (-(1 - offset) * itemwidth + currentItem * itemwidth + moffset);
                }
                cursor.setLayoutParams(lp);
            }
        }
    }

    TabItems tabItems = new TabItems() {
        public void onTabChanged(int position) {
            switch (position) {
                case 0: {
                    holder.startDateTv.setVisibility(View.VISIBLE);
                    holder.startDateCv.setVisibility(View.VISIBLE);
                    holder.endDateCv.setVisibility(View.GONE);
                    Calendar calendar = holder.startDateCv.getSelectedCalendar();
                    selectDate(holder.startDateCv, calendar, true);
                }
                break;
                case 1: {
                    holder.endDateTv.setVisibility(View.VISIBLE);
                    holder.startDateCv.setVisibility(View.GONE);
                    holder.endDateCv.setVisibility(View.VISIBLE);
                    Calendar calendar = holder.endDateCv.getSelectedCalendar();
                    selectDate(holder.endDateCv, calendar, false);
                }
                break;
            }
        }
    };

    private class CalendarViewHolder implements View.OnClickListener {

        public LinearLayout startDateLl = null;
        public LinearLayout endDateLl = null;
        public TextView startDateTv = null;
        public TextView endDateTv = null;
        public ImageView indicatorV = null;
        public CalendarView startDateCv = null;
        public CalendarView endDateCv = null;
        public TextView dateTv = null;
        public IconView preMonthIv = null;
        public IconView nextMonthIv = null;
        private VariableButton confirmVb = null;
        private int containerWidth = 0;
        private Context context = null;

        public CalendarViewHolder(View contentView) {
            this.context = contentView.getContext();
            startDateLl = (LinearLayout) contentView.findViewById(R.id.start_date_ll);
            endDateLl = (LinearLayout) contentView.findViewById(R.id.end_date_ll);
            startDateTv = (TextView) contentView.findViewById(R.id.start_date_tv);
            endDateTv = (TextView) contentView.findViewById(R.id.end_date_tv);
            indicatorV = (ImageView) contentView.findViewById(R.id.indicator_v);
            startDateCv = (CalendarView) contentView.findViewById(R.id.start_date_cv);
            endDateCv = (CalendarView) contentView.findViewById(R.id.end_date_cv);
            dateTv = (TextView) contentView.findViewById(R.id.date_tv);
            preMonthIv = (IconView) contentView.findViewById(R.id.pre_month_iv);
            preMonthIv.setOnClickListener(this);
            nextMonthIv = (IconView) contentView.findViewById(R.id.next_month_iv);
            nextMonthIv.setOnClickListener(this);
            confirmVb = (VariableButton) contentView.findViewById(R.id.confirm_vb);
            confirmVb.setOnClickListener(this);
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            containerWidth = contentView.getMeasuredWidth();

            startDateCv.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
                @Override
                public void onCalendarOutOfRange(Calendar calendar) {

                }

                @Override
                public void onCalendarSelect(Calendar calendar, boolean isClick) {
                    selectDate(startDateCv, calendar, true);
                }
            });
            endDateCv.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
                @Override
                public void onCalendarOutOfRange(Calendar calendar) {

                }

                @Override
                public void onCalendarSelect(Calendar calendar, boolean isClick) {
                    selectDate(endDateCv, calendar, false);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.pre_month_iv) {
                scrollToPre();
            } else if (id == R.id.next_month_iv) {
                scrollToNext();
            } else if (id == R.id.confirm_vb) {
                onConfirmClick();
            }
        }

        private void onConfirmClick() {
            Calendar start = startDateCv.getSelectedCalendar();
            Calendar end = endDateCv.getSelectedCalendar();
            long startTime = start.getTimeInMillis();
            long endTime = end.getTimeInMillis();
            if (startTime <= endTime) {
                onSelectTimeRange(startTime, endTime);
                if (dialogPlus != null) {
                    dialogPlus.dismiss();
                }
            } else {
                ToastUtils.showLong(context, "结束时间必须大于等于开始时间");
            }
        }

        private void scrollToPre() {
            if (startDateCv.getVisibility() == View.VISIBLE) {
                startDateCv.scrollToPre(true);
            } else if (endDateCv.getVisibility() == View.VISIBLE) {
                endDateCv.scrollToPre(true);
            }
        }

        private void scrollToNext() {
            if (startDateCv.getVisibility() == View.VISIBLE) {
                startDateCv.scrollToNext(true);
            } else if (endDateCv.getVisibility() == View.VISIBLE) {
                endDateCv.scrollToNext(true);
            }
        }

        public int getContainerWidth() {
            return containerWidth;
        }

        public int getStartIndicatorWidth() {
            float textSize = startDateTv.getTextSize();
            CharSequence text = startDateTv.getText();
            if (TextUtils.isEmpty(text)) {
                return PixelUtils.dip2px(context, 60);
            }
            return (int) (text.length() * textSize + 24);
        }

        public int getEndIndicatorWidth() {
            float textSize = endDateTv.getTextSize();
            CharSequence text = endDateTv.getText();
            if (TextUtils.isEmpty(text)) {
                return PixelUtils.dip2px(context, 60);
            }
            return (int) (text.length() * textSize + 24);
        }
    }

    private void selectDate(CalendarView calendarView, Calendar calendar, boolean isStart) {
        if (holder == null) {
            return;
        }
        if (calendarView.getVisibility() != View.VISIBLE) {
            return;
        }
        holder.dateTv.setText(DateUtils.getDateTime("yyyy年MM月", calendar.getTimeInMillis()));
        if (isStart) {
            holder.startDateTv.setText(DateUtils.getDateTime("MM月dd日", calendar.getTimeInMillis()));
        } else {
            holder.endDateTv.setText(DateUtils.getDateTime("MM月dd日", calendar.getTimeInMillis()));
        }
    }
}
