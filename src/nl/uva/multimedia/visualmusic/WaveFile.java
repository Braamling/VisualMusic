package nl.uva.multimedia.visualmusic;

/* 
 * Framework for audio playing, visualization and filtering
 *
 * For the Multimedia course in the BSc Computer Science 
 * at the University of Amsterdam 
 *
 * I.M.J. Kamps, S.J.R. van Schaik, R. de Vries (2013)
 *
 * Extended By:
 * Abe Wiersma
 * Bas van den Heuvel
 *
 * Retrieves wavefile data.
 */

import java.io.*;

import android.media.AudioFormat;
import android.util.Log;

public class WaveFile {

	FileInputStream file;
	
	byte[] byteBuffer;

	/* XXX Put here your variables. */
    private int num_channels, sample_rate, bits_per_sample;

	public WaveFile(String path) {
        byte[] buffer;

        /* Read the wave file's information. */
		try {
		    this.file = new FileInputStream(path);

            buffer = new byte[24];
            this.file.skip(12);
            this.file.read(buffer, 0, 24);
			
            /* Read the sample rate and bits per sample. */
            this.num_channels    = WaveFile.bytesToInt(buffer, 10, 2);
            this.sample_rate     = WaveFile.bytesToInt(buffer, 12, 4);
            this.bits_per_sample = WaveFile.bytesToInt(buffer, 22, 2);

            /* Move to the end of the file. */
            this.file.skip(8);
		}
        catch (IOException e) {
            return;
		}
	}

    /* Validate the wave file. */
	public static boolean isWaveFile(String path) {
		FileInputStream file;
        byte[] buffer;
        String chunk_id, format;

        try {
            file = new FileInputStream(path);
        }
        catch (Exception e) {
            return false;
        }

        buffer = new byte[4];
        try {
            if (file.read(buffer) == -1)
                return WaveFile.return_and_close(file, false);

            chunk_id = WaveFile.bytesToString(buffer, 0, 4);

            if (chunk_id.compareTo("RIFF") != 0)
                return WaveFile.return_and_close(file, false);

            file.skip(4);

            buffer = new byte[4];
            if (file.read(buffer) == -1)
                return WaveFile.return_and_close(file, false);

            format = WaveFile.bytesToString(buffer, 0, 4);

            if (format.compareTo("WAVE") != 0)
                return WaveFile.return_and_close(file, false);
        }
        catch (IOException e) {
            return WaveFile.return_and_close(file, false);
        }

        return WaveFile.return_and_close(file, true);
	}

    /* Return structure, as the file always needs to be closed. */
    private static boolean return_and_close(FileInputStream file,
                                            boolean val) {
        try {
            file.close();
        }
        catch(IOException e) {
            return false;
        }

        return val;
    }

    /* Fill a buffer with data from the file. */
	public boolean getData(short[] shortBuffer, int bufferSizeInBytes) {

		/* Re-use the bytebuffer for efficient memory usage. */
		if ((byteBuffer == null) || (byteBuffer.length != bufferSizeInBytes))
			byteBuffer = new byte[bufferSizeInBytes];

        try {
            if (this.file.read(byteBuffer) == -1)
                return false;

            for (int i = 0; i < bufferSizeInBytes; i += 2) {
                shortBuffer[i / 2] = WaveFile.bytesToShort(byteBuffer, i);
            }
        }
        catch (Exception e) {
            return false;
        }
		
		return true;

	}

	/* Return the sampleRate in Hz of the wave-file. */
	public int getSampleRate() {
		return this.sample_rate;
	}

	/*
	 * Return AudioFormat.ENCODING_PCM_16BIT OR AudioFormat.ENCODING_PCM_8BIT
	 * depending on the file..
	 */
	public int getAudioFormat() {
		if (this.bits_per_sample == 8)
            return AudioFormat.ENCODING_PCM_8BIT;
        else if (this.bits_per_sample == 16)
            return AudioFormat.ENCODING_PCM_16BIT;
        else
            return AudioFormat.ENCODING_INVALID;
	}

	/*
	 * Return AudioFormat.CHANNEL_OUT_STEREO or AudioFormat.CHANNEL_OUT_MONO
	 * depending on the file..
	 */
	public int getChannelConfig() {
        if (this.num_channels == 1)
		    return AudioFormat.CHANNEL_OUT_MONO;
        else if (this.num_channels == 2)
            return AudioFormat.CHANNEL_OUT_STEREO;
        else
            return AudioFormat.CHANNEL_INVALID;
	}
	
	/* Convert bytes to an integer. Little endian. */
	private static int bytesToInt(byte[] bytes, int offset, int length) {
		int result = 0;
		for (int i = 0; i < length; i++)
			result += (bytes[i + offset] & 0xFF) << i * 8;
		return result;
	}

	/* Convert bytes to an string. Big endian */
	private static String bytesToString(byte[] bytes, int offset, int length) {
		char[] string = new char[length];

		for (int i = 0; i < length; i++)
			string[i] += (char) bytes[i + offset];

		return new String(string);

	}

	/* Bytes to short, always uses 2 bytes */
	private static short bytesToShort(byte[] bytes, int offset) {
		return (short) (((bytes[offset + 1] & 0xFF) << 8) + (bytes[offset] & 0xFF));
	}
}
