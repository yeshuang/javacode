package javacode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.summer.util.FileUtil;

public class FindErrorCode {

	public static void write(String[] gid, Map<String, Object> userName, Map<String, Object> method) {
		if ("userName".equals(gid[0])) {
			if (method.containsKey(gid[1])) {
				String[] methodGid = (String[]) method.get(gid[1]);
				StringBuilder str = new StringBuilder();
				str.append("gid:").append(gid[1]).append("\n");
				str.append("userName:").append(gid[2]).append("\n");
				str.append("method:").append(methodGid[2]).append("\n");
				str.append("requestContents:").append("\n");
				str.append(methodGid[3]).append("\n");
				str.append("responseContents:").append("\n");
				str.append(gid[3]);
				str.append("-----------------------------------------------------------------------------------------------------------------------------").append("\n");
				method.remove(gid[1]);
				userName.remove(gid[1]);
				try {
					// FileUtil.writerInFile("/Users/shuang/Downloads/finish.txt", str.toString());
					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO t_error_info (`gid`, `user_Name`, `method`, `request_contents`, `response_contents`) VALUES (");
					sql.append("'").append(gid[1]).append("',");
					sql.append("'").append(gid[2]).append("',");
					sql.append("'").append(methodGid[2]).append("',");
					sql.append("'").append(methodGid[3]).append("',");
					sql.append("'").append(gid[3]).append("'");
					sql.append(");");
					FileUtil.writerInFileIO("/Users/shuang/Downloads/sql.sql", sql.toString(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				str = null;
			} else {
				userName.put(gid[1], gid);
			}
		} else if ("method".equals(gid[0])) {
			if (userName.containsKey(gid[1])) {
				String[] userNameGid = (String[]) userName.get(gid[1]);
				StringBuilder str = new StringBuilder();
				str.append("gid:").append(gid[1]).append("\n");
				str.append("userName:").append(userNameGid[2]).append("\n");
				str.append("method:").append(gid[2]).append("\n");
				str.append("requestContents:").append("\n");
				str.append(gid[3]).append("\n");
				str.append("responseContents:").append("\n");
				str.append(userNameGid[3]);
				str.append("-----------------------------------------------------------------------------------------------------------------------------").append("\n");
				method.remove(gid[1]);
				userName.remove(gid[1]);
				try {
					// FileUtil.writerInFile("/Users/shuang/Downloads/finish.txt", str.toString());
					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO t_error_info (`gid`, `user_Name`, `method`, `request_contents`, `response_contents`) VALUES (");
					sql.append("'").append(gid[1]).append("',");
					sql.append("'").append(userNameGid[2]).append("',");
					sql.append("'").append(gid[2]).append("',");
					sql.append("'").append(gid[3]).append("',");
					sql.append("'").append(userNameGid[3]).append("'");
					sql.append(");");
					FileUtil.writerInFileIO("/Users/shuang/Downloads/sql.sql", sql.toString(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				str = null;
			} else {
				method.put(gid[1], gid);
			}
		}
	}

	public static String[] getGid(String cont, String errorCode, boolean isVague) {
		String flag = "", gid = "", userNameOrMethod = "", parameterPath = "", vague = "<FaultCode>" + errorCode + "</FaultCode>";
		if (isVague) {
			vague = "<FaultCode>";
		}
		if (cont.contains(vague)) {
			flag = "userName";
			userNameOrMethod = cont.substring(cont.indexOf("Authorization: Digest username=\"") + "Authorization: Digest username=\"".length(), cont.indexOf("\", realm="));
			gid = cont.substring(cont.indexOf("<cwmp:ID SOAP-ENV:mustUnderstand=\"1\">") + "<cwmp:ID SOAP-ENV:mustUnderstand=\"1\">".length(), cont.indexOf("</cwmp:ID>"));
		}

		if (cont.contains("<SOAP-ENV:Header>") && cont.contains(" <cwmp:ID SOAP-ENV:mustUnderstand=\"1\">")) {
			gid = cont.substring(cont.indexOf("<cwmp:ID SOAP-ENV:mustUnderstand=\"1\">") + "<cwmp:ID SOAP-ENV:mustUnderstand=\"1\">".length(), cont.indexOf("</cwmp:ID>"));
			if (cont.contains("<SOAP-ENV:Body>")) {
				if (cont.contains("<cwmp:GetParameterNames>")) {
					userNameOrMethod = "GetParameterNames";
					parameterPath = cont.substring(cont.indexOf("<ParameterPath>") + "<ParameterPath>".length(), cont.indexOf("</ParameterPath>"));
				}
				if (cont.contains("<cwmp:SetParameterNames>")) {
					userNameOrMethod = "SetParameterNames";
				}
				if (cont.contains("<cwmp:GetParameterValues>")) {
					userNameOrMethod = "GetParameterValues";
				}
				if (cont.contains("<cwmp:SetParameterValues>")) {
					userNameOrMethod = "SetParameterValues";
				}
				if (cont.contains("<cwmp:GetParameterAttributes>")) {
					userNameOrMethod = "GetParameterAttributes";
				}
				if (cont.contains("<cwmp:SetParameterAttributes>")) {
					userNameOrMethod = "SetParameterAttributes";
				}
				if (cont.contains("<cwmp:DeleteObject>")) {
					userNameOrMethod = "DeleteObject";
				}
				if (cont.contains("<cwmp:AddObject>")) {
					userNameOrMethod = "AddObject";
				}
			}
			flag = "method";
		}
		return new String[] { flag, gid, userNameOrMethod, cont, parameterPath };
	}

	public void readLine(String inPath, String encode, int bufSize, String errorCode, boolean isVague) {
		Map<String, Object> userName = new HashMap<String, Object>();
		Map<String, Object> method = new HashMap<String, Object>();
		StringBuilder contents = new StringBuilder();
		RandomAccessFile inraf = null;
		FileChannel inChannel = null;
		String enter = "\n";
		try {
			inraf = new RandomAccessFile(inPath, "r");
			inChannel = inraf.getChannel();
			ByteBuffer inBuf = ByteBuffer.allocateDirect(bufSize);
			// temp：由于是按固定字节读取，在一次读取中，第一行和最后一行经常是不完整的行，因此定义此变量来存储上次的最后一行和这次的第一行的内容，并将之连接成完成的一行，否则会出现汉字被拆分成2个字节，并被提前转换成字符串而乱码的问题
			byte[] tempByte = new byte[0];
			// 存放每一行数据
			byte[] lineByte = new byte[0];
			while (inChannel.read(inBuf) != -1) {
				// 已经读取到数据的长度
				int readedSize = inBuf.position();
				// 用来存放读取的内容的数组
				byte[] readedByte = new byte[readedSize];
				// 将position设回0,使得可以重读Buffer中的所有数据,此处如果不设置,无法使用下面的get方法
				inBuf.rewind();
				inBuf.get(readedByte);
				inBuf.clear();
				int startNum = 0;
				int LF = 10;// 换行符
				int CR = 13;// 回车符
				boolean hasLF = false;// 是否有换行符
				for (int i = 0; i < readedSize; i++) {
					if (LF == readedByte[i]) {
						hasLF = true;
						int tempNum = tempByte.length;
						int lineNum = i - startNum;
						lineByte = new byte[tempNum + lineNum];// 数组大小已经去掉换行符
						System.arraycopy(tempByte, 0, lineByte, 0, tempNum);// 填充了lineByte[0]~lineByte[tempNum-1]
						tempByte = new byte[0];
						System.arraycopy(readedByte, startNum, lineByte, tempNum, lineNum);// 填充lineByte[tempNum]~lineByte[tempNum+lineNum-1]
						String line = new String(lineByte, 0, lineByte.length, encode);// 一行完整的字符串(过滤了换行和回车)
						line = StringUtils.isBlank(line) ? line : line + enter;
						contents.append(line);
						if (StringUtils.isBlank(line) && StringUtils.isNoneBlank(contents.toString())) {
							String[] gid = getGid(contents.toString(), errorCode, isVague);
							if (StringUtils.isNotBlank(gid[0]) && StringUtils.isNotBlank(gid[1]) && StringUtils.isNotBlank(gid[2])) {
								// System.out.println("------------>" + gid[0] + "," + gid[1] + "," + gid[2] + "," + gid[3]);
								write(gid, userName, method);
							}
							contents = new StringBuilder();
						}
						// 过滤回车符和换行符
						if (i + 1 < readedSize && readedByte[i + 1] == CR) {
							startNum = i + 2;
						} else {
							startNum = i + 1;
						}
					}

				}

				if (hasLF) {
					tempByte = new byte[readedByte.length - startNum];
					System.arraycopy(readedByte, startNum, tempByte, 0, tempByte.length);
				} else {// 兼容单次读取的内容不足一行的情况
					byte[] toTemp = new byte[tempByte.length + readedByte.length];
					System.arraycopy(tempByte, 0, toTemp, 0, tempByte.length);
					System.arraycopy(readedByte, 0, toTemp, tempByte.length, readedByte.length);
					tempByte = toTemp;
				}
			}
			if (tempByte != null && tempByte.length > 0) {// 兼容文件最后一行没有换行的情况
				String line = new String(tempByte, 0, tempByte.length, encode);
				line = StringUtils.isBlank(line) ? line : line + enter;
				contents.append(line);
				if (StringUtils.isBlank(line) && StringUtils.isNoneBlank(contents.toString())) {
					String[] gid = getGid(contents.toString(), errorCode, isVague);
					if (StringUtils.isNotBlank(gid[0]) && StringUtils.isNotBlank(gid[1]) && StringUtils.isNotBlank(gid[2])) {
						write(gid, userName, method);
					}
					contents = new StringBuilder();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inChannel.close();
				inraf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		long st = System.currentTimeMillis();
		String encode = "UTF-8";
		int bufSize = 1000000;// 一次读取的字节长度
		// String inPath = "/Users/shuang/Downloads/test.txt";
		String inPath = "/Users/shuang/Downloads/acslog/acs-info-30dt.log.2018-03-28.0";
		new FindErrorCode().readLine(inPath, encode, bufSize, "9005", true);
		System.out.println("----contents----" + (System.currentTimeMillis() - st));
	}

}
