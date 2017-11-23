package com.kfplc.ci.datafeed.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String todaysDate() {
		DateFormat simpleDateFormat = new SimpleDateFormat("ddmmyyyy");
		 Date today = Calendar.getInstance().getTime();
		 return simpleDateFormat.format(today);
	}

}
