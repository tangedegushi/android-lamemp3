//
// Created by clam314 on 2017/3/26.
//

#include <jni.h>

extern "C"
{

void JNICALL
Java_handle_recorder_SimpleLame_close(JNIEnv *env, jclass type) ;

jint JNICALL
Java_handle_recorder_SimpleLame_encode(JNIEnv *env, jclass type,
                                                      jshortArray buffer_l_, jshortArray buffer_r_,
                                                      jint samples, jbyteArray mp3buf_);
jint JNICALL
Java_handle_recorder_SimpleLame_flush(JNIEnv *env, jclass type, jbyteArray mp3buf_);

void JNICALL
Java_handle_recorder_SimpleLame_init__IIIII(JNIEnv *env, jclass type,
                                                           jint inSampleRate, jint outChannel,
                                                           jint outSampleRate, jint outBitrate,
                                                           jint quality);

}