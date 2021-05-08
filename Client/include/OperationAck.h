#ifndef INCLUDE_OperationAck
#define INCLUDE_OperationAck
#include <vector>
#include <iostream>
#include <string>

class OperationAck 
{
  public:
    OperationAck(char bytesArr[]) ;
    void process() ;
    short getOpCode() ;
    std::vector<char> encode() ;
    void printACK();
    short bytesToShort(char* bytesArr);
    char *encodeToClient();

  private:
    int OpNumber;
    std::vector<std::string> listOfNames;
    int numberOfPosts;
    int numberOfFollowers;
    int numberOfFollowing;
};

#endif /*INCLUDE_OperationAck*/