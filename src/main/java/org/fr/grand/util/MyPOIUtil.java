package org.fr.grand.util;

import java.text.DecimalFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public class MyPOIUtil {
	public static String getCellValue(Cell cell) {
		String cellValue = "";

		if (cell != null) {
			boolean b;
			DecimalFormat df = new DecimalFormat("#");
			switch (cell.getCellType()) {
			case 1:
				cellValue = cell.getRichStringCellValue().getString().trim();
				break;
			case 0:
				b = DateUtil.isCellDateFormatted(cell);
				if (b) {
					cellValue = cell.getDateCellValue().toString();

					break;
				}
				cellValue = df.format(cell.getNumericCellValue()).toString();
				break;

			case 4:
				cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
				break;
			case 2:
				cellValue = cell.getCellFormula();
				break;
			}
		}
		return cellValue;
	}
}
