#include <jni.h>
#include <string>
#include <opencv/cv.h>
#include <opencv2/legacy/legacy.hpp>
#include <opencv2/core/core.hpp>
#include "imageProc.h"
using namespace std;
using namespace cv;
extern "C"{
int histCompare(unsigned char *buf1,unsigned char *buf2)
{
    IplImage *img_gray1;
    IplImage *img_gray2;
    int hist_size=256;
    int hist_height=256;
    float range[]={0,255};
    float * ranges[]={range};
    CvHistogram *Histgram1=cvCreateHist(1,&hist_size,CV_HIST_ARRAY,ranges,1);
    CvHistogram *Histgram2=cvCreateHist(1,&hist_size,CV_HIST_ARRAY,ranges,1);
    cvCalHist(&)
}

void grayProc(unsigned char * cbuf,int w,int h)
{
    Mat imgData(h,w,CV_8UC4,(unsigned char *)cbuf);
    uchar* ptr=imgData.ptr(0);
    for(int i=0;i<w*h;i++){
        uchar grayScale = (uchar)(ptr[4*i+2]*0.299 + ptr[4*i+1]*0.587 + ptr[4*i+0]*0.114);
        ptr[4*i+1] = grayScale;
        ptr[4*i+2] = grayScale;
        ptr[4*i+0] = grayScale;
    }
}
JNIEXPORT jstring JNICALL
Java_com_example_opencvtest_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
JNIEXPORT jintArray JNICALL Java_com_example_opencvtest_MainActivity_grayProc(
        JNIEnv *env,jobject instance,jintArray buf,jint w,jint h){
    jint *cbuf;
    jboolean  ptfalse =false;
    cbuf=env->GetIntArrayElements(buf,&ptfalse);
    if(cbuf==NULL){
        return 0;
    }
    Mat imgData(h,w,CV_8UC4,(unsigned char *)cbuf);
    uchar* ptr=imgData.ptr(0);
    for(int i=0;i<w*h;i++){
        uchar grayScale = (uchar)(ptr[4*i+2]*0.299 + ptr[4*i+1]*0.587 + ptr[4*i+0]*0.114);
        ptr[4*i+1] = grayScale;
        ptr[4*i+2] = grayScale;
        ptr[4*i+0] = grayScale;
    }
    int size = w* h;
    jintArray result=env->NewIntArray(size);
    env->SetIntArrayRegion(result,0,size,cbuf);
    env->ReleaseIntArrayElements(buf,cbuf,0);
    return result;
 }
}
