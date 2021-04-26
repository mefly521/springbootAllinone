package com.demo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

public class T6 {

	public static void main(String[] args) throws InterruptedException {
		// 好友昵称
		String friendNickName = "c兴波";
//        String friendNickName = "文件传输助手";

		searchMyFriendAndSend(friendNickName);
	}

	private static void searchMyFriendAndSend(String friendNickName) throws InterruptedException {
		// 创建Robot对象
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		//打开微信 Ctrl+Alt+W
		assert robot != null;
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_W);
		//释放Ctrl按键，像Ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_ALT);

		// 该延迟不能少，否则无法搜索
		robot.delay(1000);

		// Ctrl + F 搜索指定好友
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_F);
		robot.keyRelease(KeyEvent.VK_CONTROL);

		// 将好友昵称发送到剪切板
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(friendNickName);
		clip.setContents(tText, null);
		// 以下两行按下了ctrl+v，完成粘贴功能
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(1000);
		robot.keyPress(KeyEvent.VK_ENTER);

		// 发送消息
		sendMsg();
	}

	private static void sendMsg() throws InterruptedException {
		String[] mottoes = {
				"我只爱你四天，春天夏天秋天冬天！",
				"我只爱你三天，昨天,今天,明天！",
				"我只爱你两天，白天，黑天！",
				"我只爱你一天，每一天！",
				"爱你么么哒！",
				"[坏笑]",
				""
		};
		for (String motto : mottoes) {
			sendOneMsg(motto);
		}
		Thread.sleep(2000);

		sendOneMsg("使用程序自动发送！");
	}

	private static void sendOneMsg(String msg) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText;
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		//延迟十秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
		assert robot != null;
		robot.delay(500);
		tText = new StringSelection(msg);
		clip.setContents(tText, null);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.delay(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.delay(500);
	}

}

