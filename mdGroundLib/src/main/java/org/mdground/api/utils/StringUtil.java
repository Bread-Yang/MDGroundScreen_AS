package org.mdground.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class StringUtil {
	public static String getAge(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long oldtime = calendar.getTime().getTime();
		long newtime = System.currentTimeMillis();
		long monthcur = (long) 24 * (long) 3600 * (long) 30 * (long) 1000;
		long yearcur = monthcur * 12;
		long timecur = newtime - oldtime;
		if (timecur > 3 * yearcur) {
			return ((int) (timecur / yearcur)) + "岁";
		} else if ((newtime - oldtime) < (3 * yearcur) && (newtime - oldtime) > yearcur) {
			int sui = (int) (timecur / yearcur);
			int yue = (int) ((timecur - yearcur * sui) / monthcur);
			if (yue == 0) {
				return sui + "岁";
			} else {
				return sui + "岁" + yue + "月";
			}

		} else if (timecur < yearcur && timecur > monthcur) {
			int yue = (int) (timecur / monthcur);
			return yue + "月";
		} else if (timecur < monthcur) {
			int tian = (int) (timecur / ((long) 24 * (long) 3600 * (long) 1000));
			if (tian == 0) {
				return "今天";
			}
			return tian + "天";
		}
		return "1天";

	}

	public static int getDobYear(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int oldYear = calendar.get(Calendar.YEAR);
		return oldYear;
	}

	public static int getDobMONTH(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int MONTH = calendar.get(Calendar.MONTH);
		return MONTH;
	}

	public static int getDobDay(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
		return DAY_OF_MONTH;
	}

	public static long getDateMILL(String dob) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(simpleDateFormat.parse(dob));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calendar.getTimeInMillis();
	}

	static SimpleDateFormat simpleDateFormatdayTime = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
	static SimpleDateFormat simpleDateFormatday = new SimpleDateFormat("yyyy年MM月dd日");

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static String getViewString(String dobString) {
		try {
			return simpleDateFormatdayTime.format(simpleDateFormat.parse(dobString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getViewStringNoTime(String dobString) {
		try {
			return simpleDateFormatday.format(simpleDateFormat.parse(dobString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getDOBString(String viewString) {
		try {
			return simpleDateFormat.format(simpleDateFormatday.parse(viewString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static Date getDOB(String viewString) {
		try {
			return simpleDateFormatday.parse(viewString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getDOBString(long cur) {
		try {
			return simpleDateFormat.format(cur);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getPhotoString(String clinicID, String fileID) {
		return "/yideguan?clinicID=" + clinicID + "&fileID=" + fileID;
	}

	/**
	 * 
	 * 获取UUID
	 * 
	 * 
	 * 
	 * @return 32UUID小写字符串
	 */
	public static String gainUUID() {
		String strUUID = UUID.randomUUID().toString();
		strUUID = strUUID.replaceAll("-", "").toLowerCase();
		return strUUID;
	}

	/**
	 * 
	 * 判断字符串是否非空非null
	 * 
	 * 
	 * 
	 * @param strParm
	 * 
	 *            需要判断的字符串
	 * 
	 * @return 真假
	 */
	public static boolean isNoBlankAndNoNull(String strParm) {
		return !((strParm == null) || (strParm.equals("")));
	}

	/**
	 * 
	 * 将流转成字符串
	 * 
	 * 
	 * 
	 * @param is
	 * 
	 *            输入流
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	/**
	 * 将文件转成字符串
	 * 
	 * @param file
	 *            文件
	 * @return
	 * @throws Exception
	 */
	public static String getStringFromFile(File file) throws Exception {
		FileInputStream fin = new FileInputStream(file);
		String ret = convertStreamToString(fin);
		// Make sure you close all streams.

		fin.close();
		return ret;
	}

	/**
	 * 字符全角化
	 * 
	 * @param input
	 * @return
	 */
	public static String ToSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}
