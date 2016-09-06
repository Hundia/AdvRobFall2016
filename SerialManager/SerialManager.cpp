// SerialManager.cpp : Defines the entry point for the console application.
//

#include "SerialMessage.h"
#include "SerialManager.h"
#include <boost/thread.hpp>
#include "stdafx.h"
#include "targetver.h"
using namespace std;


//void consumeSerialMessage(void* thr)
//{
//	SerialManager* SerialManager = (SerialManager*)thr;
//	SerialMessage* msgToWrite = NULL;
//	while (true) {
//		SerialManager->q.pop(msgToWrite);
//
//		//	Sanity check that the object isnt NULL
//		if (msgToWrite) {
//
//		}
//		else {
//			printf("[SerialManager::consumeSerialMessage]: Message popped is NULL..!");
//		}
//	}
//}

SerialManager::SerialManager() {

}

//	API to synchronic thread safe push to serial manager
bool SerialManager::WriteToSerial(SerialMessage& MessageToWrite)
{
	if (q.push(&MessageToWrite))
	{
		return true;
	}
	else 
	{
		return false;
	}
	
}

//	Starts the manager, basically starts the thread
//	that consumes writing requests from the queue
bool SerialManager::Init(unsigned int sizeOfQueue)
{
	q.reserve(sizeOfQueue);
	return true;
}

bool SerialManager::startSerialManager() {
	//thread* t2 = new thread(consumeSerialMessage(this));
	boost::thread* thr = new boost::thread(boost::bind(&SerialManager::consumeSerialMessage, this));
	thr->join();
	return true;
}

void SerialManager::consumeSerialMessage()
{
	SerialMessage* msgToWrite = NULL;
	printf("Starting serial");
	while (true) {
		printf("Waiting to pop");
		q.pop(msgToWrite);
		printf("Popped");
		//	Sanity check that the object isnt NULL
		if (msgToWrite) {

		}
		else {
			printf("[SerialManager::consumeSerialMessage]: Message popped is NULL..!");
		}
	}
}

