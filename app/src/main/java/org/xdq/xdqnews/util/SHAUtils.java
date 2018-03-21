/*
 * Created by 向定权 on 17-12-8 下午4:25
 *
 * Copyright (c) 2017.  All rights reserved.
 *
 * Last modified 17-11-28 下午2:09
 */

package org.xdq.xdqnews.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtils {

	/**
	 * @param decript
	 *            要加密的字符串
	 * @return 加密的字符串 SHA1加密
	 */
	public final static String SHA1(String decript) {
		try {
			// 加盐处理
			decript += "www.ganweish.com";
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
