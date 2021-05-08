#include "../include/OperationAck.h"
#include <iostream>
#include <vector>
using namespace std;

short OperationAck::bytesToShort(char *bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

OperationAck::OperationAck(char bytesArr[]):OpNumber(0),listOfNames({}),numberOfPosts(0),numberOfFollowers(0),numberOfFollowing(0)
{
    char *tmp = bytesArr;

    this->OpNumber = this->bytesToShort(tmp);

    if (this->OpNumber == 4 || this->OpNumber == 7)
    {
        char numberOfUsersBytes[2];
        numberOfUsersBytes[0] = bytesArr[2];
        numberOfUsersBytes[1] = bytesArr[3];
        char *toint = numberOfUsersBytes;
        int numberusers = this->bytesToShort(toint);
        int i = 4;
        vector<char> userNameList;
        size_t counter = 0;
        while (counter < (unsigned)(numberusers))
        {
            while (bytesArr[i] != 0)
            {
                userNameList.push_back(bytesArr[i]);
                i++;
            }
            counter++;
            i++;
            char list[userNameList.size()];
            for (size_t j = 0; j < userNameList.size(); j++)
                list[j] = userNameList[j];
            std::string toadd(list, sizeof(list));
            this->listOfNames.push_back(toadd);
            userNameList.clear();
        }
    }
    if (this->OpNumber == 8)
    {

        char posts[2];
        posts[0] = bytesArr[2];
        posts[1] = bytesArr[3];
        this->numberOfPosts = this->bytesToShort(posts);
        char followers[2];
        followers[0] = bytesArr[4];
        followers[1] = bytesArr[5];
        this->numberOfFollowers = this->bytesToShort(followers);
        char following[2];
        following[0] = bytesArr[6];
        following[1] = bytesArr[7];
        this->numberOfFollowing = this->bytesToShort(following);
    }
}

void OperationAck::process()
{
    if (this->OpNumber == 1 || this->OpNumber == 2 || this->OpNumber == 3 || this->OpNumber == 5 || this->OpNumber == 6)
    {
        std::cout << "ACK " << this->OpNumber << std::endl;
    }

    if (this->OpNumber == 7 || this->OpNumber == 4)
    {
        std::string names = "";
        for (size_t i = 0; i < this->listOfNames.size(); i++)
            names = names + " " + this->listOfNames[i];
        std::cout << "ACK " << this->OpNumber << " " << this->listOfNames.size() << names << std::endl;
    }
    if (this->OpNumber == 8)
    {
        std::cout << "ACK 8 " << this->numberOfPosts << " "
                  << this->numberOfFollowers << " " << this->numberOfFollowing
                  << std::endl;
    }
}
