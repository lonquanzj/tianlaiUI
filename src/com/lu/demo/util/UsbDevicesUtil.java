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
 * �豸�ӿڰ����� ������
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
	/** ��������ӿ� */
	private UsbInterface bulkInterface;
	private UsbEndpoint inEndpoint;
	private UsbEndpoint outEndpoint;

	private PendingIntent intent;
	/** ���������� */
	// private static final double HUNXIANGQI = 2.5129;

	/**
	 * ���͵Ŀ�������
	 */
	private byte[] control = new byte[64];

	private final static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

	protected static final String TAG = "UsbDevicesUtil";
	protected static final int TIME_OUT = 5000;

	private Handler handler;

	/** ����ÿ���յ������� */
	private byte[] receiverMusicData = new byte[256];

	/**
	 * ��������
	 */
	private List<byte[]> list = new ArrayList<byte[]>();
	
	private List<byte[]> sendData = new ArrayList<byte[]>();

	/**
	 * ��ЧԤ��[¼����,KTV,��������,����Ů��,�ݳ���,������,�չ�,������]
	 */
	private byte[][] tianLaieffectDataNew = {

			// ԭ��
			{ 45, 42, 42, 45, 50, 55, 59,

			50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50,
					50, 50, },
			// ¼����
			{ 0, 7, 100, 51, 40, 100, 50, 50, 42, 45, 50, 55, 59, 63, 62, 58,
					45, 42, 42, 45, 50, 55, 59, 63, 62, 58, },
			// �ݳ���
			{ 31, 25, 65, 66, 35, 100, 50, 50, 49, 66, 66, 75, 52, 44, 33, 21,
					25, 43, 49, 66, 66, 75, 52, 44, 33, 21, },
			// KTV
			{ 14, 11, 80, 71, 55, 100, 46, 50, 63, 62, 67, 75, 66, 56, 47, 29,
					55, 59, 63, 62, 67, 75, 66, 56, 47, 29 },
			// ��������
			{ 0, 8, 83, 23, 0, 100, 66, 50, 64, 72, 80, 84, 77, 64, 54, 45, 47,
					57, 64, 72, 80, 84, 77, 64, 54, 45 },

			// ����Ů��
			{ 0, 8, 83, 23, 0, 100, 66, 50, 54, 60, 65, 66, 54, 49, 41, 27, 30,
					45, 54, 60, 65, 66, 54, 49, 41, 27 },

	};

	/**
	 * ��������Ԥ������
	 */
	private byte[][] hunXiangQiNew = {
			// С��Ч��
			{ 0, 7, 100, 51, 40, 100, 50, },
			// ����Ч��
			{ 11, 20, 46, 73, 39, 100, 46, },
			// KTVЧ��
			{ 14, 11, 80, 71, 55, 100, 46, },
			// ԡ��Ч��
			{ 0, 8, 83, 23, 0, 100, 66, },
			// ������Ч��
			{ 31, 25, 65, 66, 35, 100, 50, },
			// ������Ч��
			{ 22, 43, 96, 66, 38, 100, 40, }

	};

	/**
	 * ��������Ԥ��
	 */
	// private int[][] junHengQi = new int[6][10];

	private byte[][] junHengQiMusicNew = {
			// ҡ��
			{ 74, 63, 59, 54, 49, 49, 54, 59, 62, 74 },
			// ����
			{ 54, 59, 62, 65, 75, 74, 67, 62, 59, 54 },
			// ��ʿ
			{ 58, 62, 63, 59, 56, 49, 45, 40, 38, 45 },
			// �ŵ�
			{ 45, 40, 41, 44, 49, 54, 59, 63, 63, 58 },
			// �ص���
			{ 49, 62, 63, 49, 35, 24, 38, 63, 73, 63, }

	};

	private byte[][] junHengQiRenShengNew = {
			// ��������
			{ 64, 72, 79, 84, 77, 64, 54, 45, },
			// ����Ů��
			{ 54, 60, 65, 66, 54, 49, 41, 27, },
			// ����
			{ 41, 44, 49, 54, 59, 63, 63, 58 },

	};

	/**
	 * �������Ԥ��,С����,Ů�������е�����������
	 */
	private byte[] junHengQiVoiceNew = { 80, 62, 46, 25

	};

	/**
	 * ���������򷵻ؽ��յ���¼������ ,û�������򷵻�null
	 * 
	 * @return ¼������
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
	 * ��ȡ�����ʵ��,������������ʼ��һ��
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
	 * ��ȡ�����ʵ��,����������򷵻�null
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
	 *            ��Ϣ������.�������Ϣ�뿴 {@link UsbDeviceConstant}��ľ�̬����
	 */
	private UsbDevicesUtil(Context context, Handler handler) {
		this.mContext = context;
		this.handler = handler;
		registerReceiver();
	}

	private UsbDevicesUtil() {

	}

	/**
	 * ��ȡһ���豸
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
	 * �ж��豸�Ƿ�����
	 * 
	 * @return true ��ǰ������,false ��ǰδ����
	 */
	public boolean isConnection() {
		return usbDeviceConnection != null;
	}

	/**
	 * ��ȡȨ�޲����������� ���ӳɹ�����{@link UsbDeviceConstant#DEVICE_CONNECTION_SUCCESS}��Ϣ
	 * ����ʧ������{@link UsbDeviceConstant#DEVICE_CONNECTION_FAIL}��Ϣ ������Ϣ������鿴
	 * {@link UsbDeviceConstant#DEVICE_CONNECTION_SUCCESS}�� ��Ϣ����������Handler�ഫ��
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
	 * �ر�����
	 */
	public void release() {
		if (usbDeviceConnection != null) {
			usbDeviceConnection.close();
			usbDeviceConnection = null;
		}

	}

	private boolean isRecoder = false;

	/**
	 * ��ʼ¼��
	 */
	public void startRecord() {

		isRecoder = true;
		new Thread(new Runnable() {

			@Override
			public void run() {

				/** 64λ��2ά���� */
				bulkInterface = usbDevice.getInterface(5);
				if (inEndpoint == null) {
					inEndpoint = bulkInterface.getEndpoint(0);
				}
				usbDeviceConnection.claimInterface(bulkInterface, true);

				list.clear();
				while (isRecoder) {
					usbDeviceConnection.bulkTransfer(inEndpoint,
							receiverMusicData, 256, TIME_OUT);
					// ������յĵ����ݳ���Ϊ256
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
	 * ��֤�豸 ��֤�ɹ����� {@link UsbDeviceConstant#VALID_DEVICE}��Ϣ ʧ�ܷ���
	 * {@link UsbDeviceConstant#INVALID_DEVICE}��Ϣ ������Ϣ�����뿴
	 * {@link UsbDeviceConstant}��˵��
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
					// ���������ж��豸
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
	 * �ر�¼��
	 */
	public void stopRecord() {
		this.isRecoder = false;
	}

	/**
	 * ���͵����ؼ���ֵ����
	 * @see 
	 * @param index
	 *            �ؼ�����
	 *            ������: 	0->��Ͳ ,
	 *            			1->���� ,
	 *            			2->������,
	 *            
	 *            �������:	10->Ԥ��ʱ ,
	 *            		 	11->����ʱ�� ,
	 *            			13->��ʪ��,
	 *            			14->�ռ��� ,
	 *            			15->��ɢ�̶�,
	 *            			16->������� ,
	 *            
	 *            ��������:	20->���� ,
	 *            			30->125HZ, 
	 *            			31->250HZ,
	 *            			32->500HZ, 
	 *            			33->1KHZ, 
	 *            			34->2KHZ,
	 *            			35->4KHZ, 
	 *            			36->8KHZ, 
	 *            			37->16KHZ, 
	 *            
	 *            ���ֽ���:	40->31HZ,
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
	 *         ����ֵ
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
	 * ��������Ч��������������Ч��
	 * 
	 * @param type
	 *            Ч������0 �������� 1 ����Ů�� 2 ����
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

		// ���浱ǰ����Ч������
		for (int i = 0; i < tempYuSheData.length; i++) {
			currentRenShengData[i + 1] = tempYuSheData[i];
		}
	}

	/**
	 * �����������Ч�����������������Ч��
	 * 
	 * @param type
	 *            0 С���� 1 Ů���� 2 �е��� 3 ������
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
	 * ���ͼ���sendData���е�����
	 */
	private void sendDataByBulk() {
		bulkInterface = usbDevice.getInterface(5);

		if (outEndpoint == null) {
			outEndpoint = bulkInterface.getEndpoint(1);
		}
		usbDeviceConnection.claimInterface(bulkInterface, true);

		// ���η��ͼ������������
		for (byte[] bytes : sendData) {
			usbDeviceConnection.bulkTransfer(outEndpoint, bytes, bytes.length,
					TIME_OUT);
		}
		// ���������
		sendData.clear();
	}

	/**
	 * ����yinxiaoList�������������
	 */
	private void sendEffectDataByBulk() {
		bulkInterface = usbDevice.getInterface(5);

		if (outEndpoint == null) {
			outEndpoint = bulkInterface.getEndpoint(1);
		}
		usbDeviceConnection.claimInterface(bulkInterface, true);

		// ���η��ͼ������������
		for (byte[] bytes : yinxiaoList) {
			usbDeviceConnection.bulkTransfer(outEndpoint, bytes, bytes.length,
					TIME_OUT);
		}

		// ���������
		yinxiaoList.clear();
	}

	/**
	 * �����豸,���ӳɹ���ʧ�����ͳɹ���ʧ����Ϣ
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
	 * ע��USB�β�㲥
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
	 * ע���㲥
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
	 * ������ЧЧ������������ЧЧ��
	 * 
	 * @param type
	 *            ��Ч������:0 ԭ�� 1 ¼���� 2 �ݳ��� 3 KTV 4 �������� 5 ����Ů��
	 */
	public void sendEffectDataNew(int type) {

		// ��ռ���
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
	 * ��������Ч��������������Ч��
	 * 
	 * @param type
	 *            ����Ч������:0 ҡ�� 1 ���� 2 ��ʿ 3 �ŵ� 4 �ص���
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
	 * ���ص�ǰ����ʹ�õ�����Ч������,�����СΪ10
	 * 
	 * @return ��ǰ����ʹ�õ�����Ч������
	 */
	public byte[] getCurrentMusicData() {
		return currentMusicData;
	}

	/**
	 * ���ص�ǰ����ʹ�õ�����Ч������,�����СΪ9
	 * 
	 * @return ��ǰ����ʹ�õ�����Ч������
	 */
	public byte[] getCurrentRenShengData() {
		return currentRenShengData;
	}

	/**
	 * ���ص�ǰ����ʹ�õĻ���Ч������,�����СΪ6
	 * 
	 * @return ��ǰ����ʹ�õĻ���Ч������
	 */
	public byte[] getCurrentHunXiangData() {
		return currentHunXiangData;
	}

	private byte[] currentMusicData = new byte[10];
	private byte[] currentRenShengData = new byte[9];
	private byte[] currentHunXiangData = new byte[6];

	/**
	 * ���浱ǰ����ʹ�õ�����Ч������,
	 * 
	 * @param data
	 *            ����������31Hz��16KHz 10 �������ֵ
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
	 * ���浱ǰ����ʹ�õĻ���Ч������,
	 * 
	 * @param data
	 *            ����������Ԥ��ʱ��������� 6�������ֵ
	 */
	public void saveHunXiangData(byte[] data) {
		saveData(data, currentHunXiangData);
	}

	/**
	 * ���浱ǰ����ʹ�õ�����Ч������,
	 * 
	 * @param data��������������������16KHZ
	 *            9�������ֵ
	 */
	public void saveRenShengData(byte[] data) {
		saveData(data, currentRenShengData);
	}

	/**
	 * ���ݻ�����Ч���������û�����Ч��
	 * 
	 * @param type
	 *            ������Ч������:0 С��Ч�� 1 ����Ч�� 2 KTVЧ�� 3 ԡ��Ч�� 4 ������Ч�� 5 ������Ч��
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
