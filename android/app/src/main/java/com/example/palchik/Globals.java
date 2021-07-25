package com.example.palchik;

import com.digitalpersona.uareu.Reader;
import com.digitalpersona.uareu.ReaderCollection;
import com.digitalpersona.uareu.UareUException;
import com.digitalpersona.uareu.UareUGlobal;
import com.digitalpersona.uareu.Reader.Capabilities;

import android.content.Context;

public class Globals 
{
	public static Reader.ImageProcessing DefaultImageProcessing = Reader.ImageProcessing.IMG_PROC_DEFAULT;

	public ReaderCollection getReaders(Context appContext) throws UareUException
	{
		ReaderCollection readers = UareUGlobal.GetReaderCollection(appContext);
		readers.GetReaders();
		return readers;
	}

	private static Globals instance;

	static 
	{
		instance = new Globals();
	}

	public static Globals getInstance()
	{
		return Globals.instance;
	}

	public static final int GetFirstDPI(Reader reader)
	{
		Capabilities caps = reader.GetCapabilities();
		return caps.resolutions[0];
	}
}
