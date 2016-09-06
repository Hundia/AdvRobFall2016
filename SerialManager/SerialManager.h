#include <boost/lockfree/queue.hpp>
#include <atomic>
#include <iostream>
#include "SerialMessage.h"
#include <boost/thread.hpp>

class SerialManager
{
public:
	SerialManager();
	~SerialManager();

	//	Allows user to control the size of the queue
	//	inialized with 1000
	bool Init(unsigned int sizeOfQueue = 1000);
	bool WriteToSerial(SerialMessage& MessageToWrite);
	bool startSerialManager();
	boost::lockfree::queue<SerialMessage*> q{ 100 };
	void consumeSerialMessage();
	//boost::thread deep_thought_2(consumeSerialMessage);
};