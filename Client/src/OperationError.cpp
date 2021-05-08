#include "../include/OperationError.h"
#include <iostream>

OperationError::OperationError(char bytes[]):OpNumber(0){
  char *tmp=bytes;
  this->OpNumber=this->bytesToShort(tmp);
}

void OperationError::process(){
  std::cout << "ERROR " << this->OpNumber << std::endl;
}

short OperationError::bytesToShort(char* bytesArr){
  short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}