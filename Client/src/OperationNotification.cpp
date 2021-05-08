#include "../include/OperationNotification.h"
#include <iostream>
#include <vector>
using namespace std;

OperationNotification::OperationNotification(char bytes[]):isPublic(true),senderName(""),content("")
{
  if (bytes[0] == 0)
    this->isPublic = false;
  if (bytes[0] == 1)
    this->isPublic = true;
  std::vector<char> arraySenderName;

  int i = 1;
  while (bytes[i] != 0)
  {
    arraySenderName.push_back(bytes[i]);
    i++;
  }
  i++;
  char bytesSender[arraySenderName.size()];
  for (size_t j = 0; j < arraySenderName.size(); j++)
  {
    bytesSender[j] = arraySenderName[j];
  }
  std::string s(bytesSender, arraySenderName.size());
  this->senderName = s;
  std::vector<char> contentMessage;
  while (bytes[i] != 0)
  {
    contentMessage.push_back(bytes[i]);
    i++;
  }
  char bytesContent[contentMessage.size()];
  for (size_t j = 0; j < contentMessage.size(); j++)
  {
    bytesContent[j] = contentMessage[j];
  }
  std::string contentTxt(bytesContent, contentMessage.size());
  this->content = contentTxt;
}

void OperationNotification::process()
{

  if (this->isPublic)
  {
    std::cout << "NOTIFICATION " << "Public " << this->senderName << " " << this->content << endl;
  }
  else
  {
    std::cout << "NOTIFICATION PM " << this->senderName << " " << this->content << endl;
  }
}

std::string OperationNotification::getOpNumber()
{
  return "9";
}
