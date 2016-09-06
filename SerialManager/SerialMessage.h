#ifndef SERIALMESSAGE_HEADER
#define SERIALMESSAGE_HEADER
class SerialMessage
{
public:
	SerialMessage(float DataToWrite[4], bool PictureToWrite, bool VideoToWrite);
	float data[4];
	bool picture;
	bool video;
};
#endif
