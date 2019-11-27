package org.fr.grand.util;

import org.fr.grand.util.MyStrUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyStrUtil {
	public static String parseEmpty(String str) {
		if (str == null || "null".equals(str.trim())) {
			str = "";
		}
		return str.trim();
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static int chineseLength(String str) {
		int valueLength = 0;
		String chinese = "[Α-￥]";

		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {

				String temp = str.substring(i, i + 1);

				if (temp.matches(chinese)) {
					valueLength += 2;
				}
			}
		}
		return valueLength;
	}

	public static int strLength(String str) {
		int valueLength = 0;
		String chinese = "[Α-￥]";
		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {

				String temp = str.substring(i, i + 1);

				if (temp.matches(chinese)) {

					valueLength += 2;
				} else {

					valueLength++;
				}
			}
		}
		return valueLength;
	}

	public static int subStringLength(String str, int maxL) {
		int currentIndex = 0;
		int valueLength = 0;
		String chinese = "[Α-￥]";

		for (int i = 0; i < str.length(); i++) {

			String temp = str.substring(i, i + 1);

			if (temp.matches(chinese)) {

				valueLength += 2;
			} else {

				valueLength++;
			}
			if (valueLength >= maxL) {
				currentIndex = i;
				break;
			}
		}
		return currentIndex;
	}

	public static Boolean isMobileNo(String str) {
		String telRegex = "[1][34578]\\d{9}";
		return Boolean.valueOf(str.matches(telRegex));
	}

	public static Boolean isNumberLetter(String str) {
		Boolean isNoLetter = Boolean.valueOf(false);
		String expr = "^[A-Za-z0-9]+$";
		if (str.matches(expr)) {
			isNoLetter = Boolean.valueOf(true);
		}
		return isNoLetter;
	}

	public static Boolean isNumber(String str) {
		Boolean isNumber = Boolean.valueOf(false);
		String expr = "^[0-9]+$";
		if (str.matches(expr)) {
			isNumber = Boolean.valueOf(true);
		}
		return isNumber;
	}

	public static Boolean isEmail(String str) {
		Boolean isEmail = Boolean.valueOf(false);
		String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if (str.matches(expr)) {
			isEmail = Boolean.valueOf(true);
		}
		return isEmail;
	}

	public static Boolean isChinese(String str) {
		Boolean isChinese = Boolean.valueOf(true);
		String chinese = "[Α-￥]";
		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {

				String temp = str.substring(i, i + 1);

				if (!temp.matches(chinese)) {
					isChinese = Boolean.valueOf(false);
				}
			}
		}
		return isChinese;
	}

	public static Boolean isContainChinese(String str) {
		Boolean isChinese = Boolean.valueOf(false);
		String chinese = "[Α-￥]";
		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {

				String temp = str.substring(i, i + 1);

				if (temp.matches(chinese)) {
					isChinese = Boolean.valueOf(true);
				}
			}
		}

		return isChinese;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
				sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String dateTimeFormat(String dateTime) {
		return "";
		// Byte code:
	}
	// 0: new java/lang/StringBuilder
	// 3: dup
	// 4: invokespecial <init> : ()V
	// 7: astore_1
	// 8: aload_0
	// 9: invokestatic isEmpty : (Ljava/lang/String;)Z
	// 12: ifeq -> 17
	// 15: aconst_null
	// 16: areturn
	// 17: aload_0
	// 18: ldc ' '
	// 20: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
	// 23: astore_2
	// 24: aload_2
	// 25: arraylength
	// 26: ifle -> 209
	// 29: aload_2
	// 30: astore_3
	// 31: aload_3
	// 32: arraylength
	// 33: istore #4
	// 35: iconst_0
	// 36: istore #5
	// 38: iload #5
	// 40: iload #4
	// 42: if_icmpge -> 209
	// 45: aload_3
	// 46: iload #5
	// 48: aaload
	// 49: astore #6
	// 51: aload #6
	// 53: ldc '-'
	// 55: invokevirtual indexOf : (Ljava/lang/String;)I
	// 58: iconst_m1
	// 59: if_icmpeq -> 125
	// 62: aload #6
	// 64: ldc '-'
	// 66: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
	// 69: astore #7
	// 71: iconst_0
	// 72: istore #8
	// 74: iload #8
	// 76: aload #7
	// 78: arraylength
	// 79: if_icmpge -> 122
	// 82: aload #7
	// 84: iload #8
	// 86: aaload
	// 87: astore #9
	// 89: aload_1
	// 90: aload #9
	// 92: invokestatic strFormat2 : (Ljava/lang/String;)Ljava/lang/String;
	// 95: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 98: pop
	// 99: iload #8
	// 101: aload #7
	// 103: arraylength
	// 104: iconst_1
	// 105: isub
	// 106: if_icmpge -> 116
	// 109: aload_1
	// 110: ldc '-'
	// 112: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 115: pop
	// 116: iinc #8, 1
	// 119: goto -> 74
	// 122: goto -> 203
	// 125: aload #6
	// 127: ldc ':'
	// 129: invokevirtual indexOf : (Ljava/lang/String;)I
	// 132: iconst_m1
	// 133: if_icmpeq -> 203
	// 136: aload_1
	// 137: ldc ' '
	// 139: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 142: pop
	// 143: aload #6
	// 145: ldc ':'
	// 147: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
	// 150: astore #7
	// 152: iconst_0
	// 153: istore #8
	// 155: iload #8
	// 157: aload #7
	// 159: arraylength
	// 160: if_icmpge -> 203
	// 163: aload #7
	// 165: iload #8
	// 167: aaload
	// 168: astore #9
	// 170: aload_1
	// 171: aload #9
	// 173: invokestatic strFormat2 : (Ljava/lang/String;)Ljava/lang/String;
	// 176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 179: pop
	// 180: iload #8
	// 182: aload #7
	// 184: arraylength
	// 185: iconst_1
	// 186: isub
	// 187: if_icmpge -> 197
	// 190: aload_1
	// 191: ldc ':'
	// 193: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
	// 196: pop
	// 197: iinc #8, 1
	// 200: goto -> 155
	// 203: iinc #5, 1
	// 206: goto -> 38
	// 209: goto -> 219
	// 212: astore_2
	// 213: aload_2
	// 214: invokevirtual printStackTrace : ()V
	// 217: aconst_null
	// 218: areturn
	// 219: aload_1
	// 220: invokevirtual toString : ()Ljava/lang/String;
	// 223: areturn
	// Line number table:
	// Java source line number -> byte code offset
	// #283 -> 0
	// #285 -> 8
	// #286 -> 15
	// #288 -> 17
	// #289 -> 24
	// #290 -> 29
	// #291 -> 51
	// #292 -> 62
	// #293 -> 71
	// #294 -> 82
	// #295 -> 89
	// #296 -> 99
	// #297 -> 109
	// #293 -> 116
	// #300 -> 122
	// #301 -> 136
	// #302 -> 143
	// #303 -> 152
	// #304 -> 163
	// #305 -> 170
	// #306 -> 180
	// #307 -> 190
	// #303 -> 197
	// #290 -> 203
	// #316 -> 209
	// #313 -> 212
	// #314 -> 213
	// #315 -> 217
	// #317 -> 219
	// Local variable table:
	// start length slot name descriptor
	// 89 27 9 str1 Ljava/lang/String;
	// 74 48 8 i I
	// 71 51 7 date [Ljava/lang/String;
	// 170 27 9 str1 Ljava/lang/String;
	// 155 48 8 i I
	// 152 51 7 date [Ljava/lang/String;
	// 51 152 6 str Ljava/lang/String;
	// 24 185 2 dateAndTime [Ljava/lang/String;
	// 213 6 2 e Ljava/lang/Exception;
	// 0 224 0 dateTime Ljava/lang/String;
	// 8 216 1 sb Ljava/lang/StringBuilder;
	// Exception table:
	// from to target type
	// 8 16 212 java/lang/Exception
	// 17 209 212 java/lang/Exception }

	public static String strFormat2(String str) {
		try {
			if (str.length() <= 1) {
				str = "0" + str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String cutString(String str, int length) {
		return cutString(str, length, "");
	}

	public static String cutString(String str, int length, String dot) { // Byte code:
		// 0: aload_0
		// 1: ldc 'GBK'
		// 3: invokestatic strlen : (Ljava/lang/String;Ljava/lang/String;)I
		// 6: istore_3
		// 7: iload_3
		// 8: iload_1
		// 9: if_icmpgt -> 14
		// 12: aload_0
		// 13: areturn
		// 14: iconst_0
		// 15: istore #4
		// 17: new java/lang/StringBuffer
		// 20: dup
		// 21: iload_1
		// 22: invokespecial <init> : (I)V
		// 25: astore #5
		// 27: aload_0
		// 28: invokevirtual toCharArray : ()[C
		// 31: astore #6
		// 33: aload #6
		// 35: astore #7
		// 37: aload #7
		// 39: arraylength
		// 40: istore #8
		// 42: iconst_0
		// 43: istore #9
		// 45: iload #9
		// 47: iload #8
		// 49: if_icmpge -> 110
		// 52: aload #7
		// 54: iload #9
		// 56: caload
		// 57: istore #10
		// 59: aload #5
		// 61: iload #10
		// 63: invokevirtual append : (C)Ljava/lang/StringBuffer;
		// 66: pop
		// 67: iload #10
		// 69: sipush #256
		// 72: if_icmple -> 81
		// 75: iinc #4, 2
		// 78: goto -> 84
		// 81: iinc #4, 1
		// 84: iload #4
		// 86: iload_1
		// 87: if_icmplt -> 104
		// 90: aload_2
		// 91: ifnull -> 110
		// 94: aload #5
		// 96: aload_2
		// 97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
		// 100: pop
		// 101: goto -> 110
		// 104: iinc #9, 1
		// 107: goto -> 45
		// 110: aload #5
		// 112: invokevirtual toString : ()Ljava/lang/String;
		// 115: areturn
		// Line number table:
		// Java source line number -> byte code offset
		// #363 -> 0
		// #364 -> 7
		// #365 -> 12
		// #367 -> 14
		// #368 -> 17
		// #369 -> 27
		// #370 -> 33
		// #371 -> 59
		// #372 -> 67
		// #373 -> 75
		// #375 -> 81
		// #377 -> 84
		// #378 -> 90
		// #379 -> 94
		// #370 -> 104
		// #384 -> 110
		// Local variable table:
		// start length slot name descriptor
		// 59 45 10 c C
		// 0 116 0 str Ljava/lang/String;
		// 0 116 1 length I
		// 0 116 2 dot Ljava/lang/String;
		// 7 109 3 strBLen I
		// 17 99 4 temp I
		// 27 89 5 sb Ljava/lang/StringBuffer;
		// 33 83 6 ch [C }

		return "";

	}

	public static String cutStringFromChar(String str1, String str2, int offset) {
		if (isEmpty(str1)) {
			return "";
		}
		int start = str1.indexOf(str2);
		if (start != -1 && str1.length() > start + offset) {
			return str1.substring(start + offset);
		}

		return "";
	}

	public static int strlen(String str, String charset) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		int length = 0;
		try {
			length = str.getBytes(charset).length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return length;
	}

	public static String getSizeDesc(long size) {
		String suffix = "B";
		if (size >= 1024L) {
			suffix = "K";
			size >>= 10;
			if (size >= 1024L) {
				suffix = "M";

				size >>= 10;
				if (size >= 1024L) {
					suffix = "G";
					size >>= 10;
				}
			}
		}

		return size + suffix;
	}

	public static long ip2int(String ip) {
		ip = ip.replace(".", ",");
		String[] items = ip.split(",");
		return Long.valueOf(items[0]).longValue() << 24 | Long.valueOf(items[1]).longValue() << 16
				| Long.valueOf(items[2]).longValue() << 8 | Long.valueOf(items[3]).longValue();
	}
}
