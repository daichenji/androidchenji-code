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
	// 定义下载资源的路径
	private String path;
	// 指定所下载的文件的保存位置
	private String targetFile;
	// 定义需要使用多少线程下载资源
	private int threadNum;
	// 定义下载的线程对象
	private DownloadThread[] threads;
	// 定义下载的文件的总大小
	private int fileSize;
	
	private Handler progressHandler;
	private int mDownStatus;
	
	public DownUtil(String path, String targetFile, int threadNum, Handler progressHandler)
	{
		this.path = path;
		this.threadNum = threadNum;
		// 初始化threads数组
		threads = new DownloadThread[threadNum];
		this.targetFile = targetFile;
		this.progressHandler = progressHandler;
	}
	
	public void run()
	{
		try {
			download();
			
			
			// 定义每秒调度获取一次系统的完成进度
			final Timer timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					
					// 获取下载任务的完成比率
					double completeRate = getCompleteRate();
					mDownStatus = (int) (completeRate * 100);
					
					Message msg = new Message();
					msg.what = 0x123;
					msg.obj = mDownStatus;
					
					// 下载完全后取消任务调度
					if (mDownStatus >= 100)
					{
						timer.cancel();
					}				
					
					
					// 发送消息通知界面更新进度条
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
		// 得到文件大小
		fileSize = conn.getContentLength();
		conn.disconnect();
		
		
		
		int currentPartSize = fileSize / threadNum + 1;
		
		Logger.i("fileSize:"+fileSize);
		Logger.i("currentPartSize:"+currentPartSize);
		
		RandomAccessFile file = new RandomAccessFile(targetFile, "rw");
		// 设置本地文件的大小
		file.setLength(fileSize);
		file.close();
		for (int i = 0; i < threadNum; i++)
		{
			// 计算每条线程的下载的开始位置
			int startPos = i * currentPartSize;
			int endPos = (i+1) * currentPartSize;
			if (endPos > fileSize)
			{
				endPos = fileSize;
			}

			// 创建下载线程
			threads[i] = new DownloadThread(startPos, endPos,	targetFile);
			// 启动下载线程
			
			
			
			threads[i].start();
		}
	}

	// 获取下载的完成百分比
	public double getCompleteRate()
	{
		// 统计多条线程已经下载的总大小
		int sumSize = 0;
		for (int i = 0; i < threadNum; i++)
		{
			sumSize += threads[i].length;
		}
		// 返回已经完成的百分比
		return sumSize * 1.0 / fileSize;
	}

	private class DownloadThread extends Thread
	{
		// 当前线程的下载位置
		private int startPos;
		// 定义当前线程负责下载的文件大小
		private int endPos;
		// 当前线程需要下载的文件块
		private String targetFile;
		// 定义已经该线程已下载的字节数
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
				
				// 随机读取文件方式打开，定位到开始位置
				fos = new RandomAccessFile(targetFile, "rw");
				fos.seek(startPos);
				
				// 读取网络数据，并写入本地文件
				while (currentPos < endPos
					&& (hasRead = inStream.read(buffer)) != -1)
				{
					
					fos.write(buffer, 0, hasRead);
					// 累计该线程下载的总大小

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
