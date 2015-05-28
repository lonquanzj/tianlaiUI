package com.lu.demo.util;

/**
 *  设备接口常量类
 * @author Administrator
 *
 */
public class UsbDeviceConstant {
	
	
	/**
	 * 向设备发送数据成功
	 */
	public final static int SEND_DATA_SUCCESS=0;
	/**
	 * 向设备发送数据成功
	 */
	public final static int SEND_DATA_FAIL=1;
	
	/**
	 * 无效设备
	 */
	public final static int INVALID_DEVICE=3;
	
	/**
	 * 有效设备
	 */
	public final static int VALID_DEVICE=4;
	
	/**
	 * 设备连接成功
	 */
	public final static int DEVICE_CONNECTION_SUCCESS = 77;
	/**
	 * 设备连接失败
	 */
	public final static int DEVICE_CONNECTION_FAIL = 78;
	
	/**
	 * 设备已插入
	 */
	public final static int ACTION_USB_DEVICE_ATTACHED = 79;
	/**
	 * 设备已删除
	 */
	public final static int ACTION_USB_DEVICE_DETACHED = 80;
	
	
}
