package io.github.biezhi.wechat.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author biezhi
 *         2017/6/2
 */
public final class IOUtils {

    private IOUtils() {
        throw new IllegalStateException("IOUtils shouldn't be constructed!");
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readToString(String file) throws IOException {
        BufferedReader crunchifyBufferReader = Files.newBufferedReader(Paths.get(file));
        return crunchifyBufferReader.lines().collect(Collectors.joining());
    }

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            assert inputChannel != null;
            inputChannel.close();
            assert outputChannel != null;
            outputChannel.close();
        }
    }

}
