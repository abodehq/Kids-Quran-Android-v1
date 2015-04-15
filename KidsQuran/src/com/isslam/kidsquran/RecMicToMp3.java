/* 
 * Copyright (c) 2011-2012 Yuichi Hirano
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.isslam.kidsquran;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import com.uraroji.garage.android.lame.SimpleLame;

/**
 * ƒ}ƒCƒN‚©‚çæ“¾‚µ‚½‰¹º‚ğMP3‚É•Û‘¶‚·‚é
 * 
 * •ÊƒXƒŒƒbƒh‚Åƒ}ƒCƒN‚©‚ç‚Ì˜^‰¹AMP3‚Ö‚Ì•ÏŠ·‚ğs‚¤
 */
public class RecMicToMp3 {

	static {
		System.loadLibrary("mp3lame");
	}

	/**
	 * MP3ƒtƒ@ƒCƒ‹‚ğ•Û‘¶‚·‚éƒtƒ@ƒCƒ‹ƒpƒX
	 */
	private String mFilePath;

	/**
	 * ƒTƒ“ƒvƒŠƒ“ƒOƒŒ[ƒg
	 */
	private int mSampleRate;

	/**
	 * ˜^‰¹’†‚©
	 */
	private boolean mIsRecording = false;

	/**
	 * ˜^‰¹‚Ìó‘Ô•Ï‰»‚ğ’Ê’m‚·‚éƒnƒ“ƒhƒ‰
	 * 
	 * @see RecMicToMp3#MSG_REC_STARTED
	 * @see RecMicToMp3#MSG_REC_STOPPED
	 * @see RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
	 * @see RecMicToMp3#MSG_ERROR_CREATE_FILE
	 * @see RecMicToMp3#MSG_ERROR_REC_START
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_RECORD
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
	 * @see RecMicToMp3#MSG_ERROR_WRITE_FILE
	 * @see RecMicToMp3#MSG_ERROR_CLOSE_FILE
	 */
	private Handler mHandler;

	/**
	 * ˜^‰¹‚ªŠJn‚µ‚½
	 */
	public static final int MSG_REC_STARTED = 0;

	/**
	 * ˜^‰¹‚ªI—¹‚µ‚½
	 */
	public static final int MSG_REC_STOPPED = 1;

	/**
	 * ƒoƒbƒtƒ@ƒTƒCƒY‚ªæ“¾‚Å‚«‚È‚¢BƒTƒ“ƒvƒŠƒ“ƒOƒŒ[ƒg“™‚Ìİ’è‚ğ’[––‚ªƒTƒ|[ƒg‚
	 * µ‚Ä‚¢‚È‚¢‰Â”\«‚ª‚ ‚éB
	 */
	public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = 2;

	/**
	 * ƒtƒ@ƒCƒ‹‚ª¶¬‚Å‚«‚È‚¢
	 */
	public static final int MSG_ERROR_CREATE_FILE = 3;

	/**
	 * ˜^‰¹‚ÌŠJn‚É¸”s‚µ‚½
	 */
	public static final int MSG_ERROR_REC_START = 4;

	/**
	 * ˜^‰¹‚ª‚Å‚«‚È‚¢B˜^‰¹’†ŠJnŒã‚Ì‚İ”­s‚·‚éB
	 */
	public static final int MSG_ERROR_AUDIO_RECORD = 5;

	/**
	 * ƒGƒ“ƒR[ƒh‚É¸”s‚µ‚½B˜^‰¹’†ŠJnŒã‚Ì‚İ”­s‚·‚éB
	 */
	public static final int MSG_ERROR_AUDIO_ENCODE = 6;

	/**
	 * ƒtƒ@ƒCƒ‹‚Ì‘‚«o‚µ‚É¸”s‚µ‚½B˜^‰¹’†ŠJnŒã‚Ì‚İ”­s‚·‚éB
	 */
	public static final int MSG_ERROR_WRITE_FILE = 7;

	/**
	 * ƒtƒ@ƒCƒ‹‚ÌƒNƒ[ƒY‚É¸”s‚µ‚½B˜^‰¹’†ŠJnŒã‚Ì‚İ”­s‚·‚éB
	 */
	public static final int MSG_ERROR_CLOSE_FILE = 8;

	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 * 
	 * @param filePath
	 *            •Û‘¶‚·‚éƒtƒ@ƒCƒ‹ƒpƒX
	 * @param sampleRate
	 *            ˜^‰¹‚·‚éƒTƒ“ƒvƒŠƒ“ƒOƒŒ[ƒgiHzj
	 */
	public RecMicToMp3(String filePath, int sampleRate) {
		if (sampleRate <= 0) {
			throw new InvalidParameterException(
					"Invalid sample rate specified.");
		}
		this.mFilePath = filePath;
		this.mSampleRate = sampleRate;
	}

	public void setAudioPath(String filePath) {
		this.mFilePath = filePath;
	}

	/**
	 * ˜^‰¹‚ğŠJn‚·‚é
	 */
	public void start() {
		// ˜^‰¹’†‚Ìê‡‚Í‰½‚à‚µ‚È‚¢
		if (mIsRecording) {
			return;
		}

		// ˜^‰¹‚ğ•ÊƒXƒŒƒbƒh‚ÅŠJn‚·‚é
		new Thread() {
			@Override
			public void run() {
				android.os.Process
						.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				// Å’áŒÀ‚Ìƒoƒbƒtƒ@ƒTƒCƒY
				final int minBufferSize = AudioRecord.getMinBufferSize(
						mSampleRate, AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);
				// ƒoƒbƒtƒ@ƒTƒCƒY‚ªæ“¾‚Å‚«‚È‚¢BƒTƒ“ƒvƒŠƒ“ƒOƒŒ[ƒg“™‚Ìİ’è‚ğ’[––‚ªƒTƒ|[ƒg‚µ‚Ä‚¢‚È‚¢‰Â”\«‚ª‚ ‚éB
				if (minBufferSize < 0) {
					if (mHandler != null) {
						mHandler.sendEmptyMessage(MSG_ERROR_GET_MIN_BUFFERSIZE);
					}
					return;
				}
				// getMinBufferSize‚Åæ“¾‚µ‚½’l‚Ìê‡
				// "W/AudioFlinger(75): RecordThread: buffer overflow"‚ª”­¶‚·‚é‚æ‚¤‚Å‚ ‚é‚½‚ßA­‚µ‘å‚«‚ß‚Ì’l‚É‚µ‚Ä‚¢‚é
				AudioRecord audioRecord = new AudioRecord(
						MediaRecorder.AudioSource.MIC, mSampleRate,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 2);

				// PCM buffer size (5sec)
				short[] buffer = new short[mSampleRate * (16 / 8) * 1 * 5]; // SampleRate[Hz]
																			// *
																			// 16bit
																			// *
																			// Mono
																			// *
																			// 5sec
				byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

				FileOutputStream output = null;
				try {
					output = new FileOutputStream(new File(mFilePath));
				} catch (FileNotFoundException e) {
					// ƒtƒ@ƒCƒ‹‚ª¶¬‚Å‚«‚È‚¢
					if (mHandler != null) {
						mHandler.sendEmptyMessage(MSG_ERROR_CREATE_FILE);
					}
					return;
				}

				// Lame init
				SimpleLame.init(mSampleRate, 1, mSampleRate, 32);

				mIsRecording = true; // ˜^‰¹‚ÌŠJnƒtƒ‰ƒO‚ğ—§‚Ä‚é
				try {
					try {
						audioRecord.startRecording(); // ˜^‰¹‚ğŠJn‚·‚é
					} catch (IllegalStateException e) {
						// ˜^‰¹‚ÌŠJn‚É¸”s‚µ‚½
						if (mHandler != null) {
							mHandler.sendEmptyMessage(MSG_ERROR_REC_START);
						}
						return;
					}

					try {
						// ˜^‰¹‚ªŠJn‚µ‚½
						if (mHandler != null) {
							mHandler.sendEmptyMessage(MSG_REC_STARTED);
						}

						int readSize = 0;
						while (mIsRecording) {
							readSize = audioRecord.read(buffer, 0,
									minBufferSize);
							if (readSize < 0) {
								// ˜^‰¹‚ª‚Å‚«‚È‚¢
								if (mHandler != null) {
									mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_RECORD);
								}
								break;
							}
							// ƒf[ƒ^‚ª“Ç‚İ‚ß‚È‚©‚Á‚½ê‡‚Í‰½‚à‚µ‚È‚¢
							else if (readSize == 0) {
								;
							}
							// ƒf[ƒ^‚ª“ü‚Á‚Ä‚¢‚éê‡
							else {
								int encResult = SimpleLame.encode(buffer,
										buffer, readSize, mp3buffer);
								if (encResult < 0) {
									// ƒGƒ“ƒR[ƒh‚É¸”s‚µ‚½
									if (mHandler != null) {
										mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
									}
									break;
								}
								if (encResult != 0) {
									try {
										output.write(mp3buffer, 0, encResult);
									} catch (IOException e) {
										// ƒtƒ@ƒCƒ‹‚Ì‘‚«o‚µ‚É¸”s‚µ‚½
										if (mHandler != null) {
											mHandler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
										}
										break;
									}
								}
							}
						}

						int flushResult = SimpleLame.flush(mp3buffer);
						if (flushResult < 0) {
							// ƒGƒ“ƒR[ƒh‚É¸”s‚µ‚½
							if (mHandler != null) {
								mHandler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
							}
						}
						if (flushResult != 0) {
							try {
								output.write(mp3buffer, 0, flushResult);
							} catch (IOException e) {
								// ƒtƒ@ƒCƒ‹‚Ì‘‚«o‚µ‚É¸”s‚µ‚½
								if (mHandler != null) {
									mHandler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
								}
							}
						}

						try {
							output.close();
						} catch (IOException e) {
							// ƒtƒ@ƒCƒ‹‚ÌƒNƒ[ƒY‚É¸”s‚µ‚½
							if (mHandler != null) {
								mHandler.sendEmptyMessage(MSG_ERROR_CLOSE_FILE);
							}
						}
					} finally {
						audioRecord.stop(); // ˜^‰¹‚ğ’â~‚·‚é
						audioRecord.release();
					}
				} finally {
					SimpleLame.close();
					mIsRecording = false; // ˜^‰¹‚ÌŠJnƒtƒ‰ƒO‚ğ‰º‚°‚é
				}

				// ˜^‰¹‚ªI—¹‚µ‚½
				if (mHandler != null) {
					mHandler.sendEmptyMessage(MSG_REC_STOPPED);
				}
			}
		}.start();
	}

	/**
	 * ˜^‰¹‚ğ’â~‚·‚é
	 */
	public void stop() {
		mIsRecording = false;
	}

	/**
	 * ˜^‰¹’†‚©‚ğæ“¾‚·‚é
	 * 
	 * @return true‚Ìê‡‚Í˜^‰¹’†A‚»‚êˆÈŠO‚Ífalse
	 */
	public boolean isRecording() {
		return mIsRecording;
	}

	/**
	 * ˜^‰¹‚Ìó‘Ô•Ï‰»‚ğ’Ê’m‚·‚éƒnƒ“ƒhƒ‰‚ğİ’è‚·‚é
	 * 
	 * @param handler
	 *            ˜^‰¹‚Ìó‘Ô•Ï‰»‚ğ’Ê’m‚·‚éƒnƒ“ƒhƒ‰
	 * 
	 * @see RecMicToMp3#MSG_REC_STARTED
	 * @see RecMicToMp3#MSG_REC_STOPPED
	 * @see RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
	 * @see RecMicToMp3#MSG_ERROR_CREATE_FILE
	 * @see RecMicToMp3#MSG_ERROR_REC_START
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_RECORD
	 * @see RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
	 * @see RecMicToMp3#MSG_ERROR_WRITE_FILE
	 * @see RecMicToMp3#MSG_ERROR_CLOSE_FILE
	 */
	public void setHandle(Handler handler) {
		this.mHandler = handler;
	}
}
