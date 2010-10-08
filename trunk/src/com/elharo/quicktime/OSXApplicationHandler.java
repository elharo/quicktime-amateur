package com.elharo.quicktime;

import java.lang.reflect.*;

import quicktime.QTException;
import quicktime.io.QTFile;

public class OSXApplicationHandler implements InvocationHandler {

	protected Method setHandledMethod = null, getFilenameMethod = null;
	protected Class appEventClass = null;

	public OSXApplicationHandler() throws Throwable {
		appEventClass = Class.forName("com.apple.eawt.ApplicationEvent");

		setHandledMethod = appEventClass.getMethod("setHandled",new Class[] {boolean.class});
		getFilenameMethod = appEventClass.getMethod("getFilename", new Class[0]);
	}

	public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		//System.out.println(methodName);

		if (methodName.equals("handleAbout")) {
			handleAbout(args[0]);
		} else if (methodName.equals("handleOpenApplication")) {
			handleOpenApplication(args[0]);
		} else if (methodName.equals("handleReOpenApplication")) {
			handleReOpenApplication(args[0]);
		} else if (methodName.equals("handleOpenFile")) {
			handleOpenFile(args[0]);
		} else if (methodName.equals("handlePreferences")) {
			handlePreferences(args[0]);
		} else if (methodName.equals("handlePrintFile"))  {
			handlePrintFile(args[0]);
		} else if (methodName.equals("handleQuit"))  {
			handleQuit(args[0]);
		}
		return null;
	}

	public void handleAbout (Object e) throws Throwable {
		PlayerFrame.showAboutBox();
		setHandledMethod.invoke(e, new Object[] {Boolean.TRUE});
	}
	public void handleOpenApplication (Object e) throws Throwable { }
	public void handleReOpenApplication (Object e) throws Throwable { }
	public void handleOpenFile (Object e) throws Throwable {
		handleOpenAndPrint(e, false);
	}
	public void handlePreferences (Object e) throws Throwable {
		PlayerFrame.showPreferencesDialog();
		setHandledMethod.invoke(e, new Object[] {Boolean.TRUE});
	}
	public void handlePrintFile (Object e) throws Throwable {
		handleOpenAndPrint(e, true);
	}
	public void handleQuit (Object e) throws Throwable {
		PlayerFrame.quit();
		setHandledMethod.invoke(e, new Object[] {Boolean.TRUE});
	}

	private void handleOpenAndPrint (Object e, boolean isPrint) throws Throwable {
		String fileName = (String) getFilenameMethod.invoke(e, null);
		fileName = fileName.replace(':', '/');
		QTFile file = new QTFile(fileName);
		PlayerFrame frame = null;
		try {
			frame = FileOpener.openFile(file);
		} catch (QTException ex) {
			// ???? Auto-generated catch block
			ex.printStackTrace();
		}
		if (isPrint && frame!=null) {
			PlayerFrame.print(frame);
			frame.dispose();
		}
		setHandledMethod.invoke(e, new Object[] {Boolean.TRUE});
	}
}

