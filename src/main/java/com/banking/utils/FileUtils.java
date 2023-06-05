package com.banking.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {
    public static String readWithFileChannel(FileChannel fc) throws IOException {
        // Get the size of the file
        long fileSize = fc.size();

        // Create a ByteBuffer with the size of the file
        ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);

        // Read data from the file into the buffer
        fc.read(buffer);

        // Convert the buffer contents to a String
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        return new String(bytes);
    }

    public static void writeWithFileChannel(FileChannel fc, String s, String fileName) throws IOException {
        wipeFileContents(fc);

        // Convert the String to bytes
        byte[] bytes = s.getBytes();

        // Create a ByteBuffer with the size of the String's bytes
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        // Write the buffer contents to the file
        fc.write(buffer);
    }

    public static void wipeFileContents(FileChannel channel) throws IOException {
        // Truncate the file to 0 bytes
        channel.truncate(0);

        // Rewind the channel's position to the beginning
        channel.position(0);

        // Write 0 bytes to the channel to clear the file contents
        ByteBuffer buffer = ByteBuffer.allocate(0);
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}
