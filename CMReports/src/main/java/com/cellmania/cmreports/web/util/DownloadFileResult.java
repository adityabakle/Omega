package com.cellmania.cmreports.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;

public class DownloadFileResult implements Result {

	public static Logger log = Logger.getLogger(DownloadFileResult.class);
	private static final long serialVersionUID = 1L;
	public static final int BUFFER_SIZE = 64 * 1024;

	private String mimeType;
	private String fileNameAttribute;
	private String fileAttrib;

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFileNameAttribute() {
		return fileNameAttribute;
	}

	public void setFileNameAttribute(String fileNameAttribute) {
		this.fileNameAttribute = fileNameAttribute;
	}

	public String getFileAttrib() {
		return fileAttrib;
	}

	public void setFileAttrib(String fileAttrib) {
		this.fileAttrib = fileAttrib;
	}

	public void execute(ActionInvocation invocation) throws Exception {
		ValueStack valueStack = invocation.getStack();
		//ServletActionContext.getResponse().addHeader("Cache-Control","no-cache");
		String _MimeTypeVal = null;
		if (valueStack.findValue(mimeType) != null) {
			_MimeTypeVal = (String) valueStack.findValue(mimeType);
		} else {
			_MimeTypeVal = "application/octet-stream";
		}
		log.debug("Mime type set from result object:" + _MimeTypeVal);

		ServletActionContext.getResponse().setContentType(_MimeTypeVal);
		String fileNameVal = null;
		if (fileNameAttribute != null) {
			fileNameVal = (String) valueStack.findValue(fileNameAttribute);
			ServletActionContext.getResponse().addHeader("Content-disposition",
					"attachment;filename=" + fileNameVal);
		}

		ByteBuffer bBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		bBuffer.clear();
		try {
			File fdownload = (java.io.File) valueStack.findValue(fileAttrib);
			if (fdownload != null) {
				final WritableByteChannel outChannel = Channels
						.newChannel(ServletActionContext.getResponse()
								.getOutputStream());
				final ReadableByteChannel inChannel = Channels
						.newChannel(new FileInputStream(fdownload));

				while (inChannel.read(bBuffer) != -1) {
					bBuffer.flip();
					outChannel.write(bBuffer);
					bBuffer.compact();
				}
				bBuffer.flip();

				while (bBuffer.hasRemaining()) {
					outChannel.write(bBuffer);
				}
				inChannel.close();
				outChannel.close();
			}
		} catch (IOException ioe) {
			log.error("Image Streaming Stopped by Client.",ioe);
		} catch (Exception e) {
			log.error("Error Downloading File" + fileNameVal, e);
		}
	}

}
