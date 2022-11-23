/*
 * Copyright (C) 2016 huanghaibin_dev <huanghaibin_dev@163.com>
 * WebSite https://github.com/MiracleTimes-Dev
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.core.view.calendar;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 一些日期辅助计算工具
 */
final class CalendarUtil {

    private static final long ONE_DAY = 1000 * 3600 * 24;

    @SuppressLint("SimpleDateFormat")
    static int getDate(String formatStr, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return Integer.parseInt(format.format(date));
    }

    /**
     * 判断一个日期是否是周末，即周六日
     *
     * @param calendar calendar
     * @return 判断一个日期是否是周末，即周六日
     */
    static boolean isWeekend(Calendar calendar) {
        int week = getWeekFormCalendar(calendar);
        return week == 0 || week == 6;
    }

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    static int getMonthDaysCount(int year, int month) {
        int count = 0;
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31;
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30;
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29;
            } else {
                count = 28;
            }
        }
        return count;
    }


    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
    static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }


    /**
     * 获取月视图的确切高度
     * Test pass
     *
     * @param year       年
     * @param month      月
     * @param itemHeight 每项的高度
     * @return 不需要多余行的高度
     */
    static int getMonthViewHeight(int year, int month, int itemHeight, int weekStartWith) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int preDiff = getMonthViewStartDiff(year, month, weekStartWith);
        int monthDaysCount = getMonthDaysCount(year, month);
        int nextDiff = getMonthEndDiff(year, month, monthDaysCount, weekStartWith);
        return (preDiff + monthDaysCount + nextDiff) / 7 * itemHeight;
    }


    /**
     * 获取某天在该月的第几周,换言之就是获取这一天在该月视图的第几行,第几周，根据周起始动态获取
     * Test pass，单元测试通过
     *
     * @param calendar  calendar
     * @param weekStart 其实星期是哪一天？
     * @return 获取某天在该月的第几周 the week line in MonthView
     */
    static int getWeekFromDayInMonth(Calendar calendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
        //该月第一天为星期几,星期天 == 0
        int diff = getMonthViewStartDiff(calendar, weekStart);
        return (calendar.getDay() + diff - 1) / 7 + 1;
    }

    /**
     * 获取上一个日子
     *
     * @param calendar calendar
     * @return 获取上一个日子
     */
    static Calendar getPreCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills - ONE_DAY);

        Calendar preCalendar = new Calendar();
        preCalendar.setYear(date.get(java.util.Calendar.YEAR));
        preCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        preCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return preCalendar;
    }

    static Calendar getNextCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());//

        long timeMills = date.getTimeInMillis();//获得起始时间戳

        date.setTimeInMillis(timeMills + ONE_DAY);

        Calendar nextCalendar = new Calendar();
        nextCalendar.setYear(date.get(java.util.Calendar.YEAR));
        nextCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        nextCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return nextCalendar;
    }

    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param calendar  calendar
     * @param weekStart weekStart 星期的起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(Calendar calendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, 1);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月份的结束偏移量，用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param calendar  calendar
     * @param weekStart weekStart 星期的起始
     * @return 获取日期所在月份的结束偏移量 the end diff in Month not MonthView
     */
    @SuppressWarnings("unused")
    static int getMonthEndDiff(Calendar calendar, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, getMonthDaysCount(calendar.getYear(), calendar.getMonth()));
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return 7 - week;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 6 : 7 - week - 1;
    }

    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期所在月视图对应的起始偏移量
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期所在月视图对应的起始偏移量 the start diff with MonthView
     */
    static int getMonthViewStartDiff(int year, int month, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return week - 1;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == CalendarViewDelegate.WEEK_START_WITH_SAT ? 0 : week;
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    static int getMonthEndDiff(int year, int month, int weekStart) {
        return getMonthEndDiff(year, month, getMonthDaysCount(year, month), weekStart);
    }


    /**
     * DAY_OF_WEEK return  1  2  3 	4  5  6	 7，偏移了一位
     * 获取日期月份对应的结束偏移量,用于计算两个年份之间总共有多少周，不用于MonthView
     * Test pass
     *
     * @param year      年
     * @param month     月
     * @param weekStart 周起始
     * @return 获取日期月份对应的结束偏移量 the end diff in Month not MonthView
     */
    private static int getMonthEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_SUN) {
            return 7 - week;
        }
        if (weekStart == CalendarViewDelegate.WEEK_START_WITH_MON) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }

    /**
     * 获取某个日期是星期几
     * 测试通过
     *
     * @param calendar 某个日期
     * @return 返回某个日期是星期几
     */
    static int getWeekFormCalendar(Calendar calendar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        return date.get(java.util.Calendar.DAY_OF_WEEK) - 1;
    }


    /**
     * 获取周视图的切换默认选项位置 WeekView index
     * 测试通过 test pass
     *
     * @param calendar  calendar
     * @param weekStart weekStart
     * @return 获取周视图的切换默认选项位置
     */
    static int getWeekViewIndexFromCalendar(Calendar calendar, int weekStart) {
        return getWeekViewStartDiff(calendar.getYear(), calendar.getMonth(), calendar.getDay(), weekStart);
    }


    /**
     * 获取两个年份之间一共有多少周，注意周起始周一、周日、周六
     * 测试通过 test pass
     *
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param maxYear      maxYear 最大年份
     * @param maxYearMonth maxYear 最大年份月份
     * @param weekStart    周起始
     * @return 周数用于WeekViewPager itemCount
     */
    @Deprecated
    static int getWeekCountBetweenYearAndYear(int minYear, int minYearMonth, int maxYear, int maxYearMonth, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, 1);
        long minTimeMills = date.getTimeInMillis();//给定时间戳
        int preDiff = getMonthViewStartDiff(minYear, minYearMonth, weekStart);

        date.set(maxYear, maxYearMonth - 1, getMonthDaysCount(maxYear, maxYearMonth));

        long maxTimeMills = date.getTimeInMillis();//给定时间戳

        int nextDiff = getMonthEndDiff(maxYear, maxYearMonth, weekStart);

        int count = preDiff + nextDiff;

        int c = (int) ((maxTimeMills - minTimeMills) / ONE_DAY) + 1;
        count += c;
        return count / 7;
    }

    /**
     * 是否在日期范围內
     * 测试通过 test pass
     *
     * @param calendar     calendar
     * @param minYear      minYear
     * @param minYearDay   最小年份天
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   最大年份天
     * @return 是否在日期范围內
     */
    static boolean isCalendarInRange(Calendar calendar,
                                     int minYear, int minYearMonth, int minYearDay,
                                     int maxYear, int maxYearMonth, int maxYearDay) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(minYear, minYearMonth - 1, minYearDay);
        long minTime = c.getTimeInMillis();
        c.set(maxYear, maxYearMonth - 1, maxYearDay);
        long maxTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime >= minTime && curTime <= maxTime;
    }

    /**
     * 获取两个日期之间一共有多少周，
     * 注意周起始周一、周日、周六
     * 测试通过 test pass
     *
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param maxYear      maxYear 最大年份
     * @param maxYearMonth maxYear 最大年份月份
     * @param maxYearDay   最大年份天
     * @param weekStart    周起始
     * @return 周数用于WeekViewPager itemCount
     */
    static int getWeekCountBetweenBothCalendar(int minYear, int minYearMonth, int minYearDay,
                                               int maxYear, int maxYearMonth, int maxYearDay,
                                               int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);
        long minTimeMills = date.getTimeInMillis();//给定时间戳
        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);

        date.set(maxYear, maxYearMonth - 1, maxYearDay);

        long maxTimeMills = date.getTimeInMillis();//给定时间戳

        int nextDiff = getWeekViewEndDiff(maxYear, maxYearMonth, maxYearDay, weekStart);

        int count = preDiff + nextDiff;

        int c = (int) ((maxTimeMills - minTimeMills) / ONE_DAY) + 1;
        count += c;
        return count / 7;
    }


    /**
     * 根据日期获取距离最小日期在第几周
     * 用来设置 WeekView currentItem
     * 测试通过 test pass
     *
     * @param calendar     calendar
     * @param minYear      minYear 最小年份
     * @param minYearMonth maxYear 最小年份月份
     * @param minYearDay   最小年份天
     * @param weekStart    周起始
     * @return 返回两个年份中第几周 the WeekView currentItem
     */
    static int getWeekFromCalendarStartWithMinCalendar(Calendar calendar,
                                                       int minYear, int minYearMonth, int minYearDay,
                                                       int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, minYearDay);//起始日期
        long firstTimeMill = date.getTimeInMillis();//获得范围起始时间戳

        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, minYearDay, weekStart);//范围起始的周偏移量

        int weekStartDiff = getWeekViewStartDiff(calendar.getYear(),
                calendar.getMonth(),
                calendar.getDay(),
                weekStart);//获取点击的日子在周视图的起始，为了兼容全球时区，最大日差为一天，如果周起始偏差weekStartDiff=0，则日期加1

        date.set(calendar.getYear(),
                calendar.getMonth() - 1,
                weekStartDiff == 0 ? calendar.getDay() + 1 : calendar.getDay());

        long curTimeMills = date.getTimeInMillis();//给定时间戳

        int c = (int) ((curTimeMills - firstTimeMill) / ONE_DAY);

        int count = preDiff + c;

        return count / 7 + 1;
    }

    /**
     * 根据星期数和最小日期推算出该星期的第一天
     * //测试通过 Test pass
     *
     * @param minYear      最小年份如2017
     * @param minYearMonth maxYear 最小年份月份，like : 2017-07
     * @param minYearDay   最小年份天
     * @param week         从最小年份minYear月minYearMonth 日1 开始的第几周 week > 0
     * @return 该星期的第一天日期
     */
    static Calendar getFirstCalendarStartWithMinCalendar(int minYear, int minYearMonth, int minYearDay, int week, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(minYear, minYearMonth - 1, minYearDay);//

        long firstTimeMills = date.getTimeInMillis();//获得起始时间戳


        long weekTimeMills = (week - 1) * 7 * ONE_DAY;

        long timeCountMills = weekTimeMills + firstTimeMills;

        date.setTimeInMillis(timeCountMills);

        int startDiff = getWeekViewStartDiff(date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH) + 1,
                date.get(java.util.Calendar.DAY_OF_MONTH), weekStart);

        timeCountMills -= startDiff * ONE_DAY;
        date.setTimeInMillis(timeCountMills);

        Calendar calendar = new Calendar();
        calendar.setYear(date.get(java.util.Calendar.YEAR));
        calendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        calendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return calendar;
    }


    /**
     * 根据日期获取两个年份中第几周,用来设置 WeekView currentItem
     * 测试通过 Test Pass
     *
     * @param calendar calendar
     * @param minYear  minYear
     * @return 返回两个年份中第几周 the WeekView currentItem
     */
    @Deprecated
    static int getWeekFromCalendarBetweenYearAndYear(Calendar calendar, int minYear, int minYearMonth, int weekStar) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(minYear, minYearMonth - 1, 1);//起始日期
        long firstTimeMill = date.getTimeInMillis();//获得范围起始时间戳

        int preDiff = getWeekViewStartDiff(minYear, minYearMonth, 1, weekStar);//范围起始的周偏移量

        int weekStartDiff = getWeekViewStartDiff(calendar.getYear(),
                calendar.getMonth(),
                calendar.getDay(),
                weekStar);//获取点击的日子在周视图的起始，为了兼容全球时区，最大日差为一天，如果周起始偏差weekStartDiff=0，则日期加1

        date.set(calendar.getYear(),
                calendar.getMonth() - 1,
                weekStartDiff == 0 ? calendar.getDay() + 1 : calendar.getDay());

        long curTimeMills = date.getTimeInMillis();//给定时间戳

        int c = (int) ((curTimeMills - firstTimeMill) / ONE_DAY);

        int count = preDiff + c;

        return count / 7 + 1;
    }


    /**
     * 是否在日期范围內
     *
     * @param calendar     calendar
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @return 是否在日期范围內
     */
    @Deprecated
    static boolean isCalendarInRange(Calendar calendar, int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(minYear, minYearMonth - 1, 1);
        long minTime = c.getTimeInMillis();
        c.set(maxYear, maxYearMonth - 1, getMonthDaysCount(maxYear, maxYearMonth));
        long maxTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime >= minTime && curTime <= maxTime;
    }

    /**
     * 是否在日期范围内
     *
     * @param calendar calendar
     * @param delegate delegate
     * @return 是否在日期范围内
     */
    static boolean isCalendarInRange(Calendar calendar, CalendarViewDelegate delegate) {
        return isCalendarInRange(calendar,
                delegate.getMinYear(), delegate.getMinYearMonth(), delegate.getMinYearDay(),
                delegate.getMaxYear(), delegate.getMaxYearMonth(), delegate.getMaxYearDay());
    }

    /**
     * 是否在日期范围內
     *
     * @param year         year
     * @param month        month
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @return 是否在日期范围內
     */
    static boolean isMonthInRange(int year, int month, int minYear, int minYearMonth, int maxYear, int maxYearMonth) {
        return !(year < minYear || year > maxYear) &&
                !(year == minYear && month < minYearMonth) &&
                !(year == maxYear && month > maxYearMonth);
    }

    /**
     * 根据星期数和最小年份推算出该星期的第一天
     * 测试通过 Test pass
     *
     * @param minYear      最小年份如2017
     * @param minYearMonth maxYear 最小年份月份，like : 2017-07
     * @param week         从最小年份minYear月minYearMonth 日1 开始的第几周 week > 0
     * @return 该星期的第一天日期
     */
    @Deprecated
    static Calendar getFirstCalendarFromWeekCount(int minYear, int minYearMonth, int week, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(minYear, minYearMonth - 1, 1);//

        long firstTimeMills = date.getTimeInMillis();//获得起始时间戳


        long weekTimeMills = (week - 1) * 7 * ONE_DAY;

        long timeCountMills = weekTimeMills + firstTimeMills;

        date.setTimeInMillis(timeCountMills);

        int startDiff = getWeekViewStartDiff(date.get(java.util.Calendar.YEAR),
                date.get(java.util.Calendar.MONTH) + 1,
                date.get(java.util.Calendar.DAY_OF_MONTH), weekStart);

        timeCountMills -= startDiff * ONE_DAY;
        date.setTimeInMillis(timeCountMills);

        Calendar calendar = new Calendar();
        calendar.setYear(date.get(java.util.Calendar.YEAR));
        calendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        calendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));

        return calendar;
    }

    /**
     * 运算 calendar1 - calendar2
     * test Pass
     *
     * @param calendar1 calendar1
     * @param calendar2 calendar2
     * @return calendar1 - calendar2
     */
    static int differ(Calendar calendar1, Calendar calendar2) {
        if (calendar1 == null) {
            return Integer.MIN_VALUE;
        }
        if (calendar2 == null) {
            return Integer.MAX_VALUE;
        }
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(calendar1.getYear(), calendar1.getMonth() - 11, calendar1.getDay());//

        long startTimeMills = date.getTimeInMillis();//获得起始时间戳

        date.set(calendar2.getYear(), calendar2.getMonth() - 11, calendar2.getDay());//

        long endTimeMills = date.getTimeInMillis();//获得结束时间戳

        return (int) ((startTimeMills - endTimeMills) / ONE_DAY);
    }

    /**
     * 比较日期大小
     *
     * @param minYear      minYear
     * @param minYearMonth minYearMonth
     * @param minYearDay   minYearDay
     * @param maxYear      maxYear
     * @param maxYearMonth maxYearMonth
     * @param maxYearDay   maxYearDay
     * @return -1 0 1
     */
    static int compareTo(int minYear, int minYearMonth, int minYearDay,
                         int maxYear, int maxYearMonth, int maxYearDay) {
        Calendar first = new Calendar();
        first.setYear(minYear);
        first.setMonth(minYearMonth);
        first.setDay(minYearDay);

        Calendar second = new Calendar();
        second.setYear(maxYear);
        second.setMonth(maxYearMonth);
        second.setDay(maxYearDay);
        return first.compareTo(second);
    }

    /**
     * 为月视图初始化日历
     *
     * @param year        year
     * @param month       month
     * @param currentDate currentDate
     * @param weekStar    weekStar
     * @return 为月视图初始化日历项
     */
    static List<Calendar> initCalendarForMonthView(int year, int month, Calendar currentDate, int weekStar) {
        java.util.Calendar date = java.util.Calendar.getInstance();

        date.set(year, month - 1, 1);

        int mPreDiff = getMonthViewStartDiff(year, month, weekStar);//获取月视图其实偏移量

        int monthDayCount = getMonthDaysCount(year, month);//获取月份真实天数

        int preYear, preMonth;
        int nextYear, nextMonth;

        int size = 42;

        List<Calendar> mItems = new ArrayList<>();

        int preMonthDaysCount;
        if (month == 1) {//如果是1月
            preYear = year - 1;
            preMonth = 12;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else if (month == 12) {//如果是12月
            preYear = year;
            preMonth = month - 1;
            nextYear = year + 1;
            nextMonth = 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        } else {//平常
            preYear = year;
            preMonth = month - 1;
            nextYear = year;
            nextMonth = month + 1;
            preMonthDaysCount = mPreDiff == 0 ? 0 : CalendarUtil.getMonthDaysCount(preYear, preMonth);
        }
        int nextDay = 1;
        for (int i = 0; i < size; i++) {
            Calendar calendarDate = new Calendar();
            if (i < mPreDiff) {
                calendarDate.setYear(preYear);
                calendarDate.setMonth(preMonth);
                calendarDate.setDay(preMonthDaysCount - mPreDiff + i + 1);
            } else if (i >= monthDayCount + mPreDiff) {
                calendarDate.setYear(nextYear);
                calendarDate.setMonth(nextMonth);
                calendarDate.setDay(nextDay);
                ++nextDay;
            } else {
                calendarDate.setYear(year);
                calendarDate.setMonth(month);
                calendarDate.setCurrentMonth(true);
                calendarDate.setDay(i - mPreDiff + 1);
            }
            if (calendarDate.equals(currentDate)) {
                calendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarDate);
            mItems.add(calendarDate);
        }
        return mItems;
    }

    static List<Calendar> getWeekCalendars(Calendar calendar, CalendarViewDelegate mDelegate) {
        long curTime = calendar.getTimeInMillis();

        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(calendar.getYear(),
                calendar.getMonth() - 1,
                calendar.getDay());//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        int startDiff;
        if (mDelegate.getWeekStart() == 1) {
            startDiff = week - 1;
        } else if (mDelegate.getWeekStart() == 2) {
            startDiff = week == 1 ? 6 : week - mDelegate.getWeekStart();
        } else {
            startDiff = week == 7 ? 0 : week;
        }

        curTime -= startDiff * ONE_DAY;
        java.util.Calendar minCalendar = java.util.Calendar.getInstance();
        minCalendar.setTimeInMillis(curTime);
        Calendar startCalendar = new Calendar();
        startCalendar.setYear(minCalendar.get(java.util.Calendar.YEAR));
        startCalendar.setMonth(minCalendar.get(java.util.Calendar.MONTH) + 1);
        startCalendar.setDay(minCalendar.get(java.util.Calendar.DAY_OF_MONTH));
        return initCalendarForWeekView(startCalendar, mDelegate, mDelegate.getWeekStart());
    }

    /**
     * 生成周视图的7个item
     *
     * @param calendar  calendar
     * @param mDelegate mDelegate
     * @param weekStart weekStart
     * @return 生成周视图的7个item
     */
    static List<Calendar> initCalendarForWeekView(Calendar calendar, CalendarViewDelegate mDelegate, int weekStart) {

        java.util.Calendar date = java.util.Calendar.getInstance();//当天时间
        date.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curDateMills = date.getTimeInMillis();//生成选择的日期时间戳

        int weekEndDiff = getWeekViewEndDiff(calendar.getYear(), calendar.getMonth(), calendar.getDay(), weekStart);
        List<Calendar> mItems = new ArrayList<>();

        date.setTimeInMillis(curDateMills);
        Calendar selectCalendar = new Calendar();
        selectCalendar.setYear(date.get(java.util.Calendar.YEAR));
        selectCalendar.setMonth(date.get(java.util.Calendar.MONTH) + 1);
        selectCalendar.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
        if (selectCalendar.equals(mDelegate.getCurrentDay())) {
            selectCalendar.setCurrentDay(true);
        }
        LunarCalendar.setupLunarCalendar(selectCalendar);
        selectCalendar.setCurrentMonth(true);
        mItems.add(selectCalendar);


        for (int i = 1; i <= weekEndDiff; i++) {
            date.setTimeInMillis(curDateMills + i * ONE_DAY);
            Calendar calendarDate = new Calendar();
            calendarDate.setYear(date.get(java.util.Calendar.YEAR));
            calendarDate.setMonth(date.get(java.util.Calendar.MONTH) + 1);
            calendarDate.setDay(date.get(java.util.Calendar.DAY_OF_MONTH));
            if (calendarDate.equals(mDelegate.getCurrentDay())) {
                calendarDate.setCurrentDay(true);
            }
            LunarCalendar.setupLunarCalendar(calendarDate);
            calendarDate.setCurrentMonth(true);
            mItems.add(calendarDate);
        }
        return mItems;
    }

    /**
     * 单元测试通过
     * 从选定的日期，获取周视图起始偏移量，用来生成周视图布局
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图起始偏移量，用来生成周视图布局
     */
    private static int getWeekViewStartDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);//
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return week - 1;
        }
        if (weekStart == 2) {
            return week == 1 ? 6 : week - weekStart;
        }
        return week == 7 ? 0 : week;
    }


    /**
     * 单元测试通过
     * 从选定的日期，获取周视图结束偏移量，用来生成周视图布局
     *
     * @param year      year
     * @param month     month
     * @param day       day
     * @param weekStart 周起始，1，2，7 日 一 六
     * @return 获取周视图结束偏移量，用来生成周视图布局
     */
    private static int getWeekViewEndDiff(int year, int month, int day, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, day);
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);
        if (weekStart == 1) {
            return 7 - week;
        }
        if (weekStart == 2) {
            return week == 1 ? 0 : 7 - week + 1;
        }
        return week == 7 ? 6 : 7 - week - 1;
    }

    /**
     * 从月视图切换获得第一天的日期
     *
     * @param position position
     * @param delegate position
     * @return 从月视图切换获得第一天的日期
     */
    @Deprecated
    static Calendar getCalendarFromMonthViewPager(int position, CalendarViewDelegate delegate) {
        Calendar calendar = new Calendar();
        calendar.setYear((position + delegate.getMinYearMonth() - 1) / 12 + delegate.getMinYear());
        calendar.setMonth((position + delegate.getMinYearMonth() - 1) % 12 + 1);
        calendar.setDay(1);
        calendar.setCurrentMonth(calendar.getYear() == delegate.getCurrentDay().getYear() &&
                calendar.getMonth() == delegate.getCurrentDay().getMonth());
        calendar.setCurrentDay(calendar.equals(delegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        return calendar;
    }


    /**
     * 从月视图切换获得第一天的日期
     *
     * @param position position
     * @param delegate position
     * @return 从月视图切换获得第一天的日期
     */
    static Calendar getFirstCalendarFromMonthViewPager(int position, CalendarViewDelegate delegate) {
        Calendar calendar = new Calendar();
        calendar.setYear((position + delegate.getMinYearMonth() - 1) / 12 + delegate.getMinYear());
        calendar.setMonth((position + delegate.getMinYearMonth() - 1) % 12 + 1);
        calendar.setDay(1);
        if (!isCalendarInRange(calendar, delegate)) {
            if (isMinRangeEdge(calendar, delegate)) {
                calendar = delegate.getMinRangeCalendar();
            } else {
                calendar = delegate.getMaxRangeCalendar();
            }
        }
        calendar.setCurrentMonth(calendar.getYear() == delegate.getCurrentDay().getYear() &&
                calendar.getMonth() == delegate.getCurrentDay().getMonth());
        calendar.setCurrentDay(calendar.equals(delegate.getCurrentDay()));
        LunarCalendar.setupLunarCalendar(calendar);
        return calendar;
    }


    /**
     * 根据传入的日期获取边界访问日期，要么最大，要么最小
     *
     * @param calendar calendar
     * @param delegate delegate
     * @return 获取边界访问日期
     */
    static Calendar getRangeEdgeCalendar(Calendar calendar, CalendarViewDelegate delegate) {
        if (CalendarUtil.isCalendarInRange(delegate.getCurrentDay(), delegate)) {
            return delegate.createCurrentDate();
        }
        if (isCalendarInRange(calendar, delegate)) {
            return calendar;
        }
        Calendar minRangeCalendar = delegate.getMinRangeCalendar();
        if (minRangeCalendar.isSameMonth(calendar)) {
            return delegate.getMinRangeCalendar();
        }
        return delegate.getMaxRangeCalendar();
    }

    /**
     * 是否是最小访问边界了
     *
     * @param calendar calendar
     * @return 是否是最小访问边界了
     */
    private static boolean isMinRangeEdge(Calendar calendar, CalendarViewDelegate delegate) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(delegate.getMinYear(), delegate.getMinYearMonth() - 1, delegate.getMinYearDay());
        long minTime = c.getTimeInMillis();
        c.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        long curTime = c.getTimeInMillis();
        return curTime < minTime;
    }

    private static int getBitInt(int data, int length, int shift) {
        return (data & (((1 << length) - 1) << shift)) >> shift;
    }

    private static long solarToInt(int y, int m, int d) {
        m = (m + 9) % 12;
        y = y - m / 10;
        return 365 * y + y / 4 - y / 100 + y / 400 + (m * 306 + 5) / 10 + (d - 1);
    }

    /**
     * 公历转农历 Solar To Lunar
     *
     * @param year  公历年
     * @param month 公历月
     * @param day   公历日
     * @return [0]农历年 [1]农历月 [2]农历日 [3]是否闰月 0 false : 1 true
     */
    public static int[] solarToLunar(int year, int month, int day) {

        int[] LUNAR_MONTH_DAYS = {1887, 0x1694, 0x16aa, 0x4ad5, 0xab6, 0xc4b7, 0x4ae, 0xa56, 0xb52a, 0x1d2a,
                0xd54, 0x75aa, 0x156a, 0x1096d, 0x95c, 0x14ae, 0xaa4d, 0x1a4c, 0x1b2a, 0x8d55, 0xad4, 0x135a, 0x495d, 0x95c,
                0xd49b, 0x149a, 0x1a4a, 0xbaa5, 0x16a8, 0x1ad4, 0x52da, 0x12b6, 0xe937, 0x92e, 0x1496, 0xb64b, 0xd4a, 0xda8,
                0x95b5, 0x56c, 0x12ae, 0x492f, 0x92e, 0xcc96, 0x1a94, 0x1d4a, 0xada9, 0xb5a, 0x56c, 0x726e, 0x125c, 0xf92d,
                0x192a, 0x1a94, 0xdb4a, 0x16aa, 0xad4, 0x955b, 0x4ba, 0x125a, 0x592b, 0x152a, 0xf695, 0xd94, 0x16aa, 0xaab5,
                0x9b4, 0x14b6, 0x6a57, 0xa56, 0x1152a, 0x1d2a, 0xd54, 0xd5aa, 0x156a, 0x96c, 0x94ae, 0x14ae, 0xa4c, 0x7d26,
                0x1b2a, 0xeb55, 0xad4, 0x12da, 0xa95d, 0x95a, 0x149a, 0x9a4d, 0x1a4a, 0x11aa5, 0x16a8, 0x16d4, 0xd2da,
                0x12b6, 0x936, 0x9497, 0x1496, 0x1564b, 0xd4a, 0xda8, 0xd5b4, 0x156c, 0x12ae, 0xa92f, 0x92e, 0xc96, 0x6d4a,
                0x1d4a, 0x10d65, 0xb58, 0x156c, 0xb26d, 0x125c, 0x192c, 0x9a95, 0x1a94, 0x1b4a, 0x4b55, 0xad4, 0xf55b,
                0x4ba, 0x125a, 0xb92b, 0x152a, 0x1694, 0x96aa, 0x15aa, 0x12ab5, 0x974, 0x14b6, 0xca57, 0xa56, 0x1526,
                0x8e95, 0xd54, 0x15aa, 0x49b5, 0x96c, 0xd4ae, 0x149c, 0x1a4c, 0xbd26, 0x1aa6, 0xb54, 0x6d6a, 0x12da,
                0x1695d, 0x95a, 0x149a, 0xda4b, 0x1a4a, 0x1aa4, 0xbb54, 0x16b4, 0xada, 0x495b, 0x936, 0xf497, 0x1496,
                0x154a, 0xb6a5, 0xda4, 0x15b4, 0x6ab6, 0x126e, 0x1092f, 0x92e, 0xc96, 0xcd4a, 0x1d4a, 0xd64, 0x956c, 0x155c,
                0x125c, 0x792e, 0x192c, 0xfa95, 0x1a94, 0x1b4a, 0xab55, 0xad4, 0x14da, 0x8a5d, 0xa5a, 0x1152b, 0x152a,
                0x1694, 0xd6aa, 0x15aa, 0xab4, 0x94ba, 0x14b6, 0xa56, 0x7527, 0xd26, 0xee53, 0xd54, 0x15aa, 0xa9b5, 0x96c,
                0x14ae, 0x8a4e, 0x1a4c, 0x11d26, 0x1aa4, 0x1b54, 0xcd6a, 0xada, 0x95c, 0x949d, 0x149a, 0x1a2a, 0x5b25,
                0x1aa4, 0xfb52, 0x16b4, 0xaba, 0xa95b, 0x936, 0x1496, 0x9a4b, 0x154a, 0x136a5, 0xda4, 0x15ac};

        int[] SOLAR = {1887, 0xec04c, 0xec23f, 0xec435, 0xec649, 0xec83e, 0xeca51, 0xecc46, 0xece3a,
                0xed04d, 0xed242, 0xed436, 0xed64a, 0xed83f, 0xeda53, 0xedc48, 0xede3d, 0xee050, 0xee244, 0xee439, 0xee64d,
                0xee842, 0xeea36, 0xeec4a, 0xeee3e, 0xef052, 0xef246, 0xef43a, 0xef64e, 0xef843, 0xefa37, 0xefc4b, 0xefe41,
                0xf0054, 0xf0248, 0xf043c, 0xf0650, 0xf0845, 0xf0a38, 0xf0c4d, 0xf0e42, 0xf1037, 0xf124a, 0xf143e, 0xf1651,
                0xf1846, 0xf1a3a, 0xf1c4e, 0xf1e44, 0xf2038, 0xf224b, 0xf243f, 0xf2653, 0xf2848, 0xf2a3b, 0xf2c4f, 0xf2e45,
                0xf3039, 0xf324d, 0xf3442, 0xf3636, 0xf384a, 0xf3a3d, 0xf3c51, 0xf3e46, 0xf403b, 0xf424e, 0xf4443, 0xf4638,
                0xf484c, 0xf4a3f, 0xf4c52, 0xf4e48, 0xf503c, 0xf524f, 0xf5445, 0xf5639, 0xf584d, 0xf5a42, 0xf5c35, 0xf5e49,
                0xf603e, 0xf6251, 0xf6446, 0xf663b, 0xf684f, 0xf6a43, 0xf6c37, 0xf6e4b, 0xf703f, 0xf7252, 0xf7447, 0xf763c,
                0xf7850, 0xf7a45, 0xf7c39, 0xf7e4d, 0xf8042, 0xf8254, 0xf8449, 0xf863d, 0xf8851, 0xf8a46, 0xf8c3b, 0xf8e4f,
                0xf9044, 0xf9237, 0xf944a, 0xf963f, 0xf9853, 0xf9a47, 0xf9c3c, 0xf9e50, 0xfa045, 0xfa238, 0xfa44c, 0xfa641,
                0xfa836, 0xfaa49, 0xfac3d, 0xfae52, 0xfb047, 0xfb23a, 0xfb44e, 0xfb643, 0xfb837, 0xfba4a, 0xfbc3f, 0xfbe53,
                0xfc048, 0xfc23c, 0xfc450, 0xfc645, 0xfc839, 0xfca4c, 0xfcc41, 0xfce36, 0xfd04a, 0xfd23d, 0xfd451, 0xfd646,
                0xfd83a, 0xfda4d, 0xfdc43, 0xfde37, 0xfe04b, 0xfe23f, 0xfe453, 0xfe648, 0xfe83c, 0xfea4f, 0xfec44, 0xfee38,
                0xff04c, 0xff241, 0xff436, 0xff64a, 0xff83e, 0xffa51, 0xffc46, 0xffe3a, 0x10004e, 0x100242, 0x100437,
                0x10064b, 0x100841, 0x100a53, 0x100c48, 0x100e3c, 0x10104f, 0x101244, 0x101438, 0x10164c, 0x101842,
                0x101a35, 0x101c49, 0x101e3d, 0x102051, 0x102245, 0x10243a, 0x10264e, 0x102843, 0x102a37, 0x102c4b,
                0x102e3f, 0x103053, 0x103247, 0x10343b, 0x10364f, 0x103845, 0x103a38, 0x103c4c, 0x103e42, 0x104036,
                0x104249, 0x10443d, 0x104651, 0x104846, 0x104a3a, 0x104c4e, 0x104e43, 0x105038, 0x10524a, 0x10543e,
                0x105652, 0x105847, 0x105a3b, 0x105c4f, 0x105e45, 0x106039, 0x10624c, 0x106441, 0x106635, 0x106849,
                0x106a3d, 0x106c51, 0x106e47, 0x10703c, 0x10724f, 0x107444, 0x107638, 0x10784c, 0x107a3f, 0x107c53,
                0x107e48};

        int[] lunarInt = new int[4];
        int index = year - SOLAR[0];
        int data = (year << 9) | (month << 5) | (day);
        int solar11;
        if (SOLAR[index] > data) {
            index--;
        }
        solar11 = SOLAR[index];
        int y = getBitInt(solar11, 12, 9);
        int m = getBitInt(solar11, 4, 5);
        int d = getBitInt(solar11, 5, 0);
        long offset = solarToInt(year, month, day) - solarToInt(y, m, d);

        int days = LUNAR_MONTH_DAYS[index];
        int leap = getBitInt(days, 4, 13);

        int lunarY = index + SOLAR[0];
        int lunarM = 1;
        int lunarD;
        offset += 1;

        for (int i = 0; i < 13; i++) {
            int dm = getBitInt(days, 1, 12 - i) == 1 ? 30 : 29;
            if (offset > dm) {
                lunarM++;
                offset -= dm;
            } else {
                break;
            }
        }
        lunarD = (int) (offset);
        lunarInt[0] = lunarY;
        lunarInt[1] = lunarM;
        lunarInt[3] = 0;

        if (leap != 0 && lunarM > leap) {
            lunarInt[1] = lunarM - 1;
            if (lunarM == leap + 1) {
                lunarInt[3] = 1;
            }
        }
        lunarInt[2] = lunarD;
        return lunarInt;
    }
}
