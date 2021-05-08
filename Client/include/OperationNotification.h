#ifndef INCLUDE_OperationNotification
#define INCLUDE_OperationNotification
#include <string>
#include <iostream>
#include <vector>
using namespace std;
class OperationNotification
{

public:
  OperationNotification(char bytes[]);
  std::string getOpNumber();
  void process();
  short bytesToShort(char* bytesArr);
	void shortToBytes(short num, char* bytesArr);

private:
bool isPublic;
std::string senderName;
std::string content;


};


#endif /*INCLUDE_OperationNotification*/