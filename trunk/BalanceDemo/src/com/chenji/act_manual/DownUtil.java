package com.chenji.act_manual;



import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.chenji.util.Logger;

import android.os.Handler;
import android.os.Message;


public class DownUtil implements Runnable
{
	// ����������Դ��·��
	private String path;
	// ָ�������ص��ļ��ı���λ��
	private String targetFile;
	// ������Ҫʹ�ö����߳�������Դ
	private int threadNum;
	// �������ص��̶߳���
	private DownloadThread[] threads;
	// �������ص��ļ����ܴ�С
	private int fileSize;
	
	private Handler progressHandler;
	private int mDownStatus;
	
	public DownUtil(String path, String targetFile, int threadNum, Handler progressHandler)
	{
		this.path = path;
		this.threadNum = threadNum;
		// ��ʼ��threads����
		threads = new DownloadThread[threadNum];
		this.targetFile = targetFile;
		this.progressHandler = progressHandler;
	}
	
	public void run()
	{
		try {
			download();
			
			
			// ����ÿ����Ȼ�ȡһ��ϵͳ����ɽ���
			final Timer timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					
					// ��ȡ�����������ɱ���
					double completeRate = getCompleteRate();
					mDownStatus = (int) (completeRate * 100);
					
					Message msg = new Message();
					msg.what = 0x123;
					msg.obj = mDownStatus;
					
					// ������ȫ��ȡ���������
					if (mDownStatus >= 100)
					{
						timer.cancel();
					}				
					
					
					// ������Ϣ֪ͨ������½�����
					progressHandler.sendMessage(msg);
				}
			}, 0, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void download() throws Exception
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		// �õ��ļ���С
		fileSize = conn.getContentLength();
		conn.disconnect();
		
		
		
		int currentPartSize = fileSize / threadNum + 1;
		
		Logger.i("fileSize:"+fileSize);
		Logger.i("currentPartSize:"+currentPartSize);
		
		RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
		// ���ñ����ļ��Ĵ�С
		file.setLength(fileSize);
		file.close();
		for (int i = 0; i < threadNum; i++)
		{
			// ����ÿ���̵߳����صĿ�ʼλ��
			int startPos = i * currentPartSize;
			int endPos = (i+1) * currentPartSize;
			if (endPos > fileSize)
			{
				endPos = fileSize;
			}

			// ���������߳�
			threads[i] = new DownloadThread(startPos, endPos,	targetFile);
			// ���������߳�
			
			
			
			threads[i].start();
		}
	}

	// ��ȡ���ص���ɰٷֱ�
	public double getCompleteRate()
	{
		// ͳ�ƶ����߳��Ѿ����ص��ܴ�С
		int sumSize = 0;
		for (int i = 0; i < threadNum; i++)
		{
			sumSize += threads[i].length;
		}
		// �����Ѿ���ɵİٷֱ�
		return sumSize * 1.0 / fileSize;
	}

	private class DownloadThread extends Thread
	{
		// ��ǰ�̵߳�����λ��
		private int startPos;
		// ���嵱ǰ�̸߳������ص��ļ���С
		private int endPos;
		// ��ǰ�߳���Ҫ���ص��ļ���
		private String targetFile;
		// �����Ѿ����߳������ص��ֽ���
		public int length;
		private RandomAccessFile  fos = null;
		
		public DownloadThread(int startPos, int endPos, String targetFile)
		{
			this.startPos = startPos;
			this.endPos = endPos;
			this.targetFile = targetFile;
		}

		@Override
		public void run()
		{
			try
			{
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				
				Logger.i(""+startPos+"  "+endPos);
				
				conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
				
				BufferedInputStream inStream = new BufferedInputStream(conn.getInputStream());
				
				length = 0;
				
				byte[] buffer = new byte[1024];
				int hasRead = 0;
				int currentPos = startPos + length;
				
				// �����ȡ�ļ���ʽ�򿪣���λ����ʼλ��
				fos = new RandomAccessFile(targetFile, "rw");
				fos.seek(startPos);
				
				// ��ȡ�������ݣ���д�뱾���ļ�
				while (currentPos < endPos
					&& (hasRead = inStream.read(buffer)) != -1)
				{
					
					fos.write(buffer, 0, hasRead);
					// �ۼƸ��߳����ص��ܴ�С

					length += hasRead;
					currentPos = startPos + length;
					
				}
				fos.close();
				inStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
