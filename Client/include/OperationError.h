#ifndef INCLUDE_OperationError
#define INCLUDE_OperationError
#include <string>

class OperationError
{

public:
  OperationError(char bytes[]);
  int getOpNumber();
  void process();
  short bytesToShort(char* bytesArr);
	void shortToBytes(short num, char* bytesArr);

private:
  int OpNumber;
};

#endif /*INCLUDE_OperationError*/