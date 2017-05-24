#ifndef __FACEVERI_H__
#define __FACEVERI_H__

#define FEAT_DIM 256

struct FACERC
{
	int x, y, width, height;
};

int faceVeriInit(char* faceVeriConfigPath);
int faceDetectCamera(unsigned char* pFrame, FACERC& rc, int w, int h, int isFlip);
int faceFeatureExtractCamera( char* imgPath, FACERC& rc, float** featc, int isCompare);
int faceFeatureExtractCamera(unsigned char* pFrame, int w, int h, FACERC& rc, float** featc, int isCompare, int isFlip);
int faceFeatureExtractIDCard( char* imgPath, FACERC& rc, float** feati);
int faceFeatureExtractIDCard(unsigned char* pFrame, int w, int h, FACERC& rc, float** feati);
int faceFeatureCompare(float* score);
void faceVeriFree();
void debugTest(unsigned char* pFrame, int w, int h, char* imagepath, int isFlip);
#endif // __FACEVERI_H__