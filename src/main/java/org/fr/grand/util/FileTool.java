/**
 * 
 */
package org.fr.grand.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author PE
 * @date 2019年7月25日 下午4:08:43
 * @explain
 */
public class FileTool {

	public static void testFile(StringBuffer sb, String floder, String fileName) {
		try {
			File dir = new File(floder);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		String file = floder + fileName;
		try {
			byte[] bs = sb.toString().getBytes();
			OutputStream out = new FileOutputStream(file, true);
			for (int i = 0; i < bs.length; i++) {
				out.write(bs[i]);
			}
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
