package javacode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.summer.util.FileUtil;

public class TestIO {

	public static void main(String[] args) {
		System.out.println("ready go !!! \n");
		long st = System.currentTimeMillis();
		try {
			readyGo();
			// testMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n all files have been raped -_- expenditure time (" + (System.currentTimeMillis() - st) + ")ms");
	}

	/**
	 * 测试方法
	 * @throws Exception
	 */
	public static void testMethod() throws Exception {
		FileInputStream fis = new FileInputStream("/Users/shuang/git/rms-js/rms-dao-js/src/main/resources/sqlmap/GatewayBusinessDetailJsMapper.xml");
		BufferedReader buf = new BufferedReader(new InputStreamReader(fis));
		String s = null;
		StringBuffer cont = new StringBuffer();
		while ((s = buf.readLine()) != null) {
			cont.append(s);
		}
		buf.close();
		Pattern pattern = Pattern.compile("<select [\\s\\S]*?>[\\s\\S]*?</select>");// <select [\\s\\S]*?>[\\s\\S(order by)+\\s\\S]*?</select>
		Matcher matcher = pattern.matcher(cont);
		StringBuffer sql = new StringBuffer();
		while (matcher.find()) {
			String pm = matcher.group();
			System.out.println(pm);
			if (((pm.indexOf("distinct") > -1 || pm.indexOf("DISTINCT") > -1) && (pm.indexOf("order by") > -1 || pm.indexOf("ORDER BY") > -1)) || (pm.indexOf("group by") > -1 || pm.indexOf("GROUP BY") > -1)) {
				sql.append(pm);
			}
		}
		System.out.println("-------------------------- \n pattern contents: " + sql);
	}

	public static void readyGo() throws Exception {
		Set<File> filelist = FileUtil.getFileList("/Users/shuang/git/rms-js/rms-dao-js/src/main/resources/sqlmap/", ".xml");
		if (filelist.isEmpty()) {
			System.out.println("----not find files----");
			System.exit(0);
		}
		Iterator<File> it = filelist.iterator();
		while (it.hasNext()) {
			File file = it.next();
			Pattern pattern = Pattern.compile("<select [\\s\\S]*?>[\\s\\S]*?</select>");// <select [\\s\\S]*?>[\\s\\S(order by)+\\s\\S]*?</select>
			Matcher matcher = pattern.matcher(FileUtil.getFileContentsIO(file.getPath()));
			StringBuffer sql = new StringBuffer();
			while (matcher.find()) {
				String pm = matcher.group();
				if (pm.isEmpty()) {
					System.out.println("----not pattern contents----");
					System.exit(0);
				}
				if (((pm.indexOf("distinct") > -1 || pm.indexOf("DISTINCT") > -1) && (pm.indexOf("order by") > -1 || pm.indexOf("ORDER BY") > -1)) || (pm.indexOf("group by") > -1 || pm.indexOf("GROUP BY") > -1)) {
					sql.append(pm);
				}
			}
			FileUtil.writerInFileIO("/Users/shuang/Downloads/orderby-group-distinct/" + file.getName(), sql.toString(), false);
			// System.out.println(file.getName() + " have been raped -_- -_-");
		}
	}
}
