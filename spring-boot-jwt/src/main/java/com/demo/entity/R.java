/**
 * Copyright (c) 2016-2019  All rights reserved.
 *
 *
 *
 *
 */

package com.demo.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 *
 */
@Data
public class R<T>  {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private T data;

	public R() {
		code = 0;
		msg = "success";
	}
}
