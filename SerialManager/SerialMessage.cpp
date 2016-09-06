#include "SerialMessage.h"


SerialMessage::SerialMessage(float DataToWrite[4], bool PictureToWrite, bool VideoToWrite)
{
	for (size_t i = 0; i < 4; i++)
	{
		data[i] = DataToWrite[i];
	}

	picture = PictureToWrite;
	video = VideoToWrite;
}
