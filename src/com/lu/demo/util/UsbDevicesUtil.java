package com.lu.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;

/**
 * 设备接口帮助类 单例类
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class UsbDevicesUtil {
	private Context mContext;
	private UsbManager usbManager;
	private UsbDeviceConnection usbDeviceConnection;
	private UsbDevice usbDevice;
	/** 批量传输接口 */
	private UsbInterface bulkInterface;
	private UsbEndpoint inEndpoint;
	private UsbEndpoint outEndpoint;

	private PendingIntent intent;
	/** 混响器常数 */
	// private static final double HUNXIANGQI = 2.5129;

	/**
	 * 发送的控制数据
	 */
	private byte[] control = new byte[64];

	private final static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

	protected static final String TAG = "UsbDevicesUtil";
	protected static final int TIME_OUT = 5000;

	private Handler handler;

	/** 批量每次收到的数据 */
	private byte[] receiverMusicData = new byte[256];

	/**
	 * 音乐数据
	 */
	private List<byte[]> list = new ArrayList<byte[]>();
	
	private List<byte[]> sendData = new ArrayList<byte[]>();

	/**
	 * 音效预设[录音棚,KTV,经典男声,经典女声,演唱会,音乐厅,空谷,留声机]
	 */
	private byte[][] tianLaieffectDataNew = {

			// 原声
			{ 45, 42, 42, 45, 50, 55, 59,

			50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
					50, 50, },
			// 录音棚
			{ 0, 7, 100, 51, 40, 100, 50, 50, 42, 45, 50, 55, 59, 63, 62, 58,
					45, 42, 42, 45, 50, 55, 59, 63, 62, 58, },
			// 演唱会
			{ 31, 25, 65, 66, 35, 100, 50, 50, 49, 66, 66, 75, 52, 44, 33, 21,
					25, 43, 49, 66, 66, 75, 52, 44, 33, 21, },
			// KTV
			{ 14, 11, 80, 71, 55, 100, 46, 50, 63, 62, 67, 75, 66, 56, 47, 29,
					55, 59, 63, 62, 67, 75, 66, 56, 47, 29 },
			// 经典男声
			{ 0, 8, 83, 23, 0, 100, 66, 50, 64, 72, 80, 84, 77, 64, 54, 45, 47,
					57, 64, 72, 80, 84, 77, 64, 54, 45 },

			// 经典女声
			{ 0, 8, 83, 23, 0, 100, 66, 50, 54, 60, 65, 66, 54, 49, 41, 27, 30,
					45, 54, 60, 65, 66, 54, 49, 41, 27 },

	};

	/**
	 * 混响器的预设数据
	 */
	private byte[][] hunXiangQiNew = {
			// 小厅效果
			{ 0, 7, 100, 51, 40, 100, 50, },
			// 房间效果
			{ 11, 20, 46, 73, 39, 100, 46, },
			// KTV效果
			{ 14, 11, 80, 71, 55, 100, 46, },
			// 浴室效果
			{ 0, 8, 83, 23, 0, 100, 66, },
			// 体育馆效果
			{ 31, 25, 65, 66, 35, 100, 50, },
			// 音乐厅效果
			{ 22, 43, 96, 66, 38, 100, 40, }

	};

	/**
	 * 均衡器的预设
	 */
	// private int[][] junHengQi = new int[6][10];

	private byte[][] junHengQiMusicNew = {
			// 摇滚
			{ 74, 63, 59, 54, 49, 49, 54, 59, 62, 74 },
			// 流行
			{ 54, 59, 62, 65, 75, 74, 67, 62, 59, 54 },
			// 爵士
			{ 58, 62, 63, 59, 56, 49, 45, 40, 38, 45 },
			// 古典
			{ 45, 40, 41, 44, 49, 54, 59, 63, 63, 58 },
			// 重低音
			{ 49, 62, 63, 49, 35, 24, 38, 63, 73, 63, }

	};

	private byte[][] junHengQiRenShengNew = {
			// 经典男声
			{ 64, 72, 79, 84, 77, 64, 54, 45, },
			// 经典女声
			{ 54, 60, 65, 66, 54, 49, 41, 27, },
			// 民族
			{ 41, 44, 49, 54, 59, 63, 63, 58 },

	};

	/**
	 * 人声变调预设,小孩声,女高声，男低声，怪兽声
	 */
	private byte[] junHengQiVoiceNew = { 80, 62, 46, 25

	};

	/**
	 * 若有数据则返回接收到的录音数据 ,没有数据则返回null
	 * 
	 * @return 录音数据
	 */
	public List<byte[]> getRecordData() {
		synchronized (UsbDevicesUtil.this) {
			if (list.size() > 0) {
				List<byte[]> tempList = new ArrayList<byte[]>();
				tempList.addAll(list);
				list.clear();
				return tempList;
			}
			return null;

		}

	}

	private static UsbDevicesUtil usbDevicesUtil = null;

	/**
	 * 获取该类的实例,如果不存在则初始化一个
	 * 
	 * @param context
	 * @param handler
	 * @return
	 */
	public static UsbDevicesUtil getInstance(Context context, Handler handler) {
		if (usbDevicesUtil == null) {
			usbDevicesUtil = new UsbDevicesUtil(context, handler);
		}
		return usbDevicesUtil;
	}

	/**
	 * 获取该类的实例,如果不存在则返回null
	 * 
	 * @return
	 */
	public static UsbDevicesUtil getInstance() {
		return usbDevicesUtil;
	}

	private List<byte[]> yinxiaoList = new ArrayList<byte[]>();

	/**
	 * 
	 * @param context
	 * @param handler
	 *            消息传递类.具体的消息请看 {@link UsbDeviceConstant}类的静态常量
	 */
	private UsbDevicesUtil(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;
		registerReceiver();
	}

	private UsbDevicesUtil() {

	}

	/**
	 * 获取一个设备
	 */
	private void getDevice() {
		usbManager = (UsbManager) mContext
				.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
		Iterator<UsbDevice> iterator = usbDevices.values().iterator();
		while (iterator.hasNext()) {
			usbDevice = iterator.next();
		}
	}

	/**
	 * 判断设备是否连接
	 * 
	 * @return true 当前已连接,false 当前未连接
	 */
	public boolean isConnection() {
		return usbDeviceConnection != null;
	}

	/**
	 * 获取权限并且连接声卡 连接成功则发送{@link UsbDeviceConstant#DEVICE_CONNECTION_SUCCESS}消息
	 * 连接失败则发送{@link UsbDeviceConstant#DEVICE_CONNECTION_FAIL}消息 具体消息内容请查看
	 * {@link UsbDeviceConstant#DEVICE_CONNECTION_SUCCESS}类 消息机制则利用Handler类传递
	 */
	public void getPermissionConnection() {
		getDevice();
		if (usbDevice == null) {
			handler.sendEmptyMessage(UsbDeviceConstant.DEVICE_CONNECTION_FAIL);
			return;
		}
		//
		// if (usbManager.hasPermission(usbDevice)) {
		// connectionUsbDevice();
		// return;
		// } else {
		usbManager.requestPermission(usbDevice, intent);
		// return;
		// }
	}

	/**
	 * 关闭连接
	 */
	public void release() {
		if (usbDeviceConnection != null) {
			usbDeviceConnection.close();
			usbDeviceConnection = null;
		}

	}

	private boolean isRecoder = false;

	/**
	 * 开始录音
	 */
	public void startRecord() {

		isRecoder = true;
		new Thread(new Runnable() {

			@Override
			public void run() {

				/** 64位的2维数组 */
				bulkInterface = usbDevice.getInterface(5);
				if (inEndpoint == null) {
					inEndpoint = bulkInterface.getEndpoint(0);
				}
				usbDeviceConnection.claimInterface(bulkInterface, true);

				list.clear();
				while (isRecoder) {
					usbDeviceConnection.bulkTransfer(inEndpoint,
							receiverMusicData, 256, TIME_OUT);
					// 如果接收的的数据长度为256
					if (receiverMusicData[40] == 1) {

						byte[] bytes = new byte[receiverMusicData.length - 64];
						for (int i = 0; i < bytes.length; i++) {
							bytes[i] = receiverMusicData[i + 64];
						}
						synchronized (UsbDevicesUtil.this) {

							list.add(bytes);

						}
					}
				}

			}
		}).start();
	}

	/**
	 * 验证设备 验证成功发送 {@link UsbDeviceConstant#VALID_DEVICE}消息 失败发送
	 * {@link UsbDeviceConstant#INVALID_DEVICE}消息 具体消息详情请看
	 * {@link UsbDeviceConstant}类说明
	 */
	public void verifyDevice() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] bytes = new byte[10];
				;
				int a = usbDeviceConnection.bulkTransfer(outEndpoint, bytes,
						bytes.length, TIME_OUT);
				if (a != -1) {
					// handler.sendEmptyMessage(UsbDeviceConstant.SEND_DATA_SUCCESS);
					// 根据数据判断设备
					if (true) {
						handler.sendEmptyMessage(UsbDeviceConstant.VALID_DEVICE);
					}

					// else {
					// handler.sendEmptyMessage(UsbDeviceConstant.INVALID_DEVICE);
					// }

				} else {
					handler.sendEmptyMessage(UsbDeviceConstant.VALID_DEVICE);
				}
			}

		}).start();
	}

	/**
	 * 关闭录音
	 */
	public void stopRecord() {
		this.isRecoder = false;
	}

	/**
	 * 发送单个控件的值数据
	 * @see 
	 * @param index
	 *            控件索引
	 *            主界面: 	0->话筒 ,
	 *            			1->伴奏 ,
	 *            			2->主音量,
	 *            
	 *            混响界面:	10->预延时 ,
	 *            		 	11->混响时长 ,
	 *            			13->干湿比,
	 *            			14->空间广度 ,
	 *            			15->扩散程度,
	 *            			16->输出增益 ,
	 *            
	 *            人声界面:	20->音调 ,
	 *            			30->125HZ, 
	 *            			31->250HZ,
	 *            			32->500HZ, 
	 *            			33->1KHZ, 
	 *            			34->2KHZ,
	 *            			35->4KHZ, 
	 *            			36->8KHZ, 
	 *            			37->16KHZ, 
	 *            
	 *            音乐界面:	40->31HZ,
	 *            			41->62HZ, 
	 *            			42->126HZ, 
	 *            			43->250HZ, 
	 *           	 		44->500HZ, 
	 *           	 		45->1KHZ, 
	 *           	 		46->2KHZ, 
	 *            			47->4KHZ, 
	 *            			48->8KHZ, 
	 *            			49->16KHZ,
	 * @param value
	 * 
	 *         控制值
	 * 
	 *            
	 */
	public void sendParamterDataNew(int index, int value) {
		sendData.clear();
		byte[] temp = new byte[64];
		if (index != 13) {

			control[0] = 4;
			control[1] = 1;
			control[2] = (byte) index;
			control[3] = (byte) value;
			control[4] = 0;
			control[5] = 0;

			for (int i = 0; i < 64; i++) {
				temp[i] = control[i];
			}
		} else {
			control[0] = 4;
			control[1] = 2;
			control[2] = (byte) 12;
			control[3] = (byte) (100 - value);
			control[4] = (byte) 13;
			control[5] = (byte) value;

			for (int i = 0; i < 64; i++) {
				temp[i] = control[i];
			}

		}
		sendData.add(temp);

		sendDataByBulk();
	}

	/**
	 * 根据人声效果索引设置人声效果
	 * 
	 * @param type
	 *            效果索引0 经典男声 1 经典女声 2 民族
	 */
	public void sendRenShengEQYuSheData(int type) {
		yinxiaoList.clear();
		byte[] tempYuSheData = junHengQiRenShengNew[type];

		byte[] temp = new byte[64];
		temp[0] = 4;
		temp[1] = 8;
		temp[2] = 30;
		temp[3] = tempYuSheData[0];
		temp[4] = 31;
		temp[5] = tempYuSheData[1];
		temp[6] = 32;
		temp[7] = tempYuSheData[2];
		temp[8] = 33;
		temp[9] = tempYuSheData[3];
		temp[10] = 34;
		temp[11] = tempYuSheData[4];
		temp[12] = 35;
		temp[13] = tempYuSheData[5];
		temp[14] = 36;
		temp[15] = tempYuSheData[6];
		temp[16] = 37;
		temp[17] = tempYuSheData[7];

		yinxiaoList.add(temp);

		 sendEffectDataByBulk();

		// 保存当前人声效果数据
		for (int i = 0; i < tempYuSheData.length; i++) {
			currentRenShengData[i + 1] = tempYuSheData[i];
		}
	}

	/**
	 * 根据人声变调效果索引设置人声变调效果
	 * 
	 * @param type
	 *            0 小孩声 1 女高声 2 男低声 3 怪兽声
	 */
	public void sendRenShengBiaoDiaoYuSheDate(int type) {
		sendData.clear();

		byte[] temp = new byte[64];
		temp[0] = 4;
		temp[1] = 1;
		temp[2] = 20;
		temp[3] = junHengQiVoiceNew[type];

		sendData.add(temp);
		sendDataByBulk();
		currentRenShengData[0] = junHengQiVoiceNew[type];
	}

	/**
	 * 发送集合sendData所有的数据
	 */
	private void sendDataByBulk() {
		bulkInterface = usbDevice.getInterface(5);

		if (outEndpoint == null) {
			outEndpoint = bulkInterface.getEndpoint(1);
		}
		usbDeviceConnection.claimInterface(bulkInterface, true);

		// 依次发送集合里面的数据
		for (byte[] bytes : sendData) {
			usbDeviceConnection.bulkTransfer(outEndpoint, bytes, bytes.length,
					TIME_OUT);
		}
		// 将集合清空
		sendData.clear();
	}

	/**
	 * 发送yinxiaoList集合里面的数据
	 */
	private void sendEffectDataByBulk() {
		bulkInterface = usbDevice.getInterface(5);

		if (outEndpoint == null) {
			outEndpoint = bulkInterface.getEndpoint(1);
		}
		usbDeviceConnection.claimInterface(bulkInterface, true);

		// 依次发送集合里面的数据
		for (byte[] bytes : yinxiaoList) {
			usbDeviceConnection.bulkTransfer(outEndpoint, bytes, bytes.length,
					TIME_OUT);
		}

		// 将集合清空
		yinxiaoList.clear();
	}

	/**
	 * 连接设备,连接成功或失败则发送成功或失败消息
	 */
	@SuppressLint("NewApi")
	private void connectionUsbDevice() {

		usbDeviceConnection = usbManager.openDevice(usbDevice);
		if (usbDeviceConnection != null) {

			handler.sendEmptyMessage(UsbDeviceConstant.DEVICE_CONNECTION_SUCCESS);

			bulkInterface = usbDevice.getInterface(5);

			if (outEndpoint == null) {
				outEndpoint = bulkInterface.getEndpoint(1);
			}
			usbDeviceConnection.claimInterface(bulkInterface, true);
		} else {
			handler.sendEmptyMessage(UsbDeviceConstant.DEVICE_CONNECTION_FAIL);
		}

	}

	/**
	 * 注册USB拔插广播
	 */
	private void registerReceiver() {
		intent = PendingIntent.getBroadcast(mContext, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		mContext.registerReceiver(mUsbReceiver, filter);
	}

	/**
	 * 注销广播
	 */
	public void unregisterReceiver() {
		if (mContext != null && mUsbReceiver != null) {
			mContext.unregisterReceiver(mUsbReceiver);
		}

	}

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String actionString = intent.getAction();
			if (actionString.equals(ACTION_USB_PERMISSION)) {
				connectionUsbDevice();
			} else if (actionString
					.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
				handler.sendEmptyMessage(UsbDeviceConstant.ACTION_USB_DEVICE_ATTACHED);

			} else if (actionString
					.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
				handler.sendEmptyMessage(UsbDeviceConstant.ACTION_USB_DEVICE_DETACHED);
				stopRecord();
				release();
			}

		}

	};

	/**
	 * 根据音效效果索引设置音效效果
	 * 
	 * @param type
	 *            音效的类型:0 原声 1 录音棚 2 演唱会 3 KTV 4 经典男声 5 经典女声
	 */
	public void sendEffectDataNew(int type) {

		// 清空集合
		yinxiaoList.clear();
		byte[] tempEffect = tianLaieffectDataNew[type];
		byte[] temp = new byte[64];
		temp[0] = 4;
		temp[1] = 26;
		temp[2] = 10;
		temp[3] = tempEffect[0];
		temp[4] = 11;
		temp[5] = tempEffect[1];
		temp[6] = 12;
		temp[7] = tempEffect[2];
		temp[8] = 13;
		temp[9] = tempEffect[3];
		temp[10] = 14;
		temp[11] = tempEffect[4];
		temp[12] = 15;
		temp[13] = tempEffect[5];
		temp[14] = 16;
		temp[15] = tempEffect[6];
		temp[16] = 20;
		temp[17] = tempEffect[7];
		temp[18] = 30;
		temp[19] = tempEffect[8];
		temp[20] = 31;
		temp[21] = tempEffect[9];
		temp[22] = 32;
		temp[23] = tempEffect[10];
		temp[24] = 33;
		temp[25] = tempEffect[11];
		temp[26] = 34;
		temp[27] = tempEffect[12];
		temp[28] = 35;
		temp[29] = tempEffect[13];
		temp[30] = 36;
		temp[31] = tempEffect[14];
		temp[32] = 37;
		temp[33] = tempEffect[15];
		temp[34] = 40;
		temp[35] = tempEffect[16];
		temp[36] = 41;
		temp[37] = tempEffect[17];
		temp[38] = 42;
		temp[39] = tempEffect[18];
		temp[40] = 43;
		temp[41] = tempEffect[19];
		temp[42] = 44;
		temp[43] = tempEffect[20];
		temp[44] = 45;
		temp[45] = tempEffect[21];
		temp[46] = 46;
		temp[47] = tempEffect[22];
		temp[48] = 47;
		temp[49] = tempEffect[23];
		temp[50] = 48;
		temp[51] = tempEffect[24];
		temp[52] = 49;
		temp[53] = tempEffect[25];

		yinxiaoList.add(temp);
		// for (byte[] bs : yinxiaoList) {
		// StringBuffer string = new StringBuffer();
		// for (byte b : bs) {
		// string.append(b);
		// string.append(",");
		// }
		// Log.i(TAG, new String(string));
		// }
		sendEffectDataByBulk();
	}

	/**
	 * 根据音乐效果索引设置音乐效果
	 * 
	 * @param type
	 *            音乐效果索引:0 摇滚 1 流行 2 爵士 3 古典 4 重低音
	 */
	public void sendJunHengQiYuSheDataMusic(int type) {
		yinxiaoList.clear();
		byte[] tempYuSheData = junHengQiMusicNew[type];

		byte[] temp = new byte[64];
		temp[0] = 4;
		temp[1] = 10;
		temp[2] = 40;
		temp[3] = tempYuSheData[0];
		temp[4] = 41;
		temp[5] = tempYuSheData[1];
		temp[6] = 42;
		temp[7] = tempYuSheData[2];
		temp[8] = 43;
		temp[9] = tempYuSheData[3];
		temp[10] = 44;
		temp[11] = tempYuSheData[4];
		temp[12] = 45;
		temp[13] = tempYuSheData[5];
		temp[14] = 46;
		temp[15] = tempYuSheData[6];
		temp[16] = 47;
		temp[17] = tempYuSheData[7];
		temp[18] = 48;
		temp[19] = tempYuSheData[8];
		temp[20] = 49;
		temp[21] = tempYuSheData[9];

		yinxiaoList.add(temp);

		 sendEffectDataByBulk();
		saveMusicData(tempYuSheData);
	}

	/**
	 * 返回当前正在使用的音乐效果数据,数组大小为10
	 * 
	 * @return 当前正在使用的音乐效果数据
	 */
	public byte[] getCurrentMusicData() {
		return currentMusicData;
	}

	/**
	 * 返回当前正在使用的人声效果数据,数组大小为9
	 * 
	 * @return 当前正在使用的人声效果数据
	 */
	public byte[] getCurrentRenShengData() {
		return currentRenShengData;
	}

	/**
	 * 返回当前正在使用的混响效果数据,数组大小为6
	 * 
	 * @return 当前正在使用的混响效果数据
	 */
	public byte[] getCurrentHunXiangData() {
		return currentHunXiangData;
	}

	private byte[] currentMusicData = new byte[10];
	private byte[] currentRenShengData = new byte[9];
	private byte[] currentHunXiangData = new byte[6];

	/**
	 * 保存当前正在使用的音乐效果数据,
	 * 
	 * @param data
	 *            数组里面是31Hz到16KHz 10 个滑块的值
	 */
	public void saveMusicData(byte[] data) {
		saveData(data, currentMusicData);
	}

	private void saveData(byte[] data, byte[] savedata) {
		for (int i = 0; i < data.length; i++) {
			savedata[i] = data[i];
		}
	}

	/**
	 * 保存当前正在使用的混响效果数据,
	 * 
	 * @param data
	 *            数组里面是预延时到输出增益 6个滑块的值
	 */
	public void saveHunXiangData(byte[] data) {
		saveData(data, currentHunXiangData);
	}

	/**
	 * 保存当前正在使用的人声效果数据,
	 * 
	 * @param data数组里面是人声音调到16KHZ
	 *            9个滑块的值
	 */
	public void saveRenShengData(byte[] data) {
		saveData(data, currentRenShengData);
	}

	/**
	 * 根据混响器效果索引设置混响器效果
	 * 
	 * @param type
	 *            混响器效果索引:0 小厅效果 1 房间效果 2 KTV效果 3 浴室效果 4 体育馆效果 5 音乐厅效果
	 * 
	 * 
	 */
	public void sendHunXiangQiYuSheData(int type) {

		yinxiaoList.clear();
		byte[] tempHunXiangQi = hunXiangQiNew[type];

		byte[] temp = new byte[64];
		temp[0] = 4;
		temp[1] = 7;
		temp[2] = 10;
		temp[3] = tempHunXiangQi[0];
		temp[4] = 11;
		temp[5] = tempHunXiangQi[1];
		temp[6] = 12;
		temp[7] = tempHunXiangQi[2];
		temp[8] = 13;
		temp[9] = tempHunXiangQi[3];
		temp[10] = 14;
		temp[11] = tempHunXiangQi[4];
		temp[12] = 15;
		temp[13] = tempHunXiangQi[5];
		temp[14] = 16;
		temp[15] = tempHunXiangQi[6];

		yinxiaoList.add(temp);

		 sendEffectDataByBulk();
//		 saveData(tempHunXiangQi, currentHunXiangData);
		for (int i = 0; i < currentHunXiangData.length; i++) {

			currentHunXiangData[i] = tempHunXiangQi[i];
			if (i > 2) {
				currentHunXiangData[i] = tempHunXiangQi[i + 1];
			}
		}
	}

}
