/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snaker.engine.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import org.snaker.engine.SnakerException;

/**
 * 流数据帮助类
 * @author yuqs
 * @since 1.0
 */
public class StreamHelper {
	public static final int DEFAULT_CHUNK_SIZE = 1024;
	public static final int BUFFERSIZE = 4096;
	/**
	 * 根据文件名称resource打开输入流，并返回
	 * @param resource
	 * @return
	 */
	public static InputStream openStream(String resource) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resource);

		if (stream == null) {
			stream = StreamHelper.class.getClassLoader().getResourceAsStream(resource);
		}

		return stream;
	}
	
	public static InputStream getStreamFromString(String text) {
		try {
			byte[] bytes = text.getBytes("UTF-8");
			return new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public static InputStream getStreamFromFile(File file) {
		InputStream stream = null;
		try {
			if (!file.exists()) {
				throw new SnakerException("file " + file + " doesn't exist");
			}
			if (file.isDirectory()) {
				throw new SnakerException("file " + file + " is a directory");
			}
			stream = new FileInputStream(file);

		} catch (Exception e) {
			throw new SnakerException("couldn't access file " + file + ": "
					+ e.getMessage(), e);
		}
		return stream;
	}

	public static InputStream getStreamFromClasspath(String resourceName) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resourceName);

		if (stream == null) {
			stream = StreamHelper.class.getClassLoader().getResourceAsStream(
					resourceName);
		}

		if (stream == null) {
			throw new SnakerException("resource " + resourceName
					+ " does not exist");
		}
		return stream;
	}

	public static InputStream getStreamFromUrl(URL url) {
		InputStream stream = null;
		try {
			stream = url.openStream();
		} catch (IOException e) {
			throw new SnakerException("couldn't open URL stream", e);
		}
		return stream;
	}
	
	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		transfer(in, out);
		return out.toByteArray();
	}

	public static long transfer(InputStream in, OutputStream out)
			throws IOException {
		long total = 0;
		byte[] buffer = new byte[BUFFERSIZE];
		for (int count; (count = in.read(buffer)) != -1;) {
			out.write(buffer, 0, count);
			total += count;
		}
		return total;
	}
	
	/**
	 * input->output字节流copy
	 * @param inputStream
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		return copy( inputStream, outputStream, DEFAULT_CHUNK_SIZE );
	}

	/**
	 * input->output字节流copy
	 * @param inputStream
	 * @param outputStream
	 * @param bufferSize
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n;
		while ( -1 != ( n = inputStream.read( buffer ) ) ) {
			outputStream.write( buffer, 0, n );
			count += n;
		}
		return count;

	}

	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy( reader, writer, DEFAULT_CHUNK_SIZE );
	}

	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		char[] buffer = new char[bufferSize];
		long count = 0;
		int n;
		while ( -1 != ( n = reader.read( buffer ) ) ) {
			writer.write( buffer, 0, n );
			count += n;
		}
		return count;

	}
}
