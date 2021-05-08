#include "connectionHandler.h"
#include <thread>
#include <string>
#include <iostream>
#include <vector>
#include "OperationError.h"
#include "OperationNotification.h"
#include "OperationAck.h"
bool flag = true;
bool loggedIn=false;
bool socketFlag=true;

short bytesToShort(char *bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}
void shortToBytes(short num, char *bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
std::vector<std::string> split(const std::string &str, const char &by)
{

    std::vector<std::string> ret;
    std::string tmp;
    for (size_t i = 0; i < str.size(); i++)
    {
        if (str[i] == by)
        {
            ret.push_back(tmp);
            tmp.clear();
        }
        else
        {
            tmp.push_back(str[i]);
        }
    }
    ret.push_back(tmp);
    return ret;
}

class Task2
{
  private:
    ConnectionHandler *connection1;

  public:
    Task2(ConnectionHandler &connection) : connection1(&connection)
    {
    }

    void run()
    {
            while (socketFlag  == true)
    {
        char OpNumBytes[2];
        connection1->getBytes(OpNumBytes, 2);
        if (bytesToShort(OpNumBytes) == 11)
        {
            char bytesToProcess[2];
            connection1->getBytes(bytesToProcess, 2);
            OperationError(bytesToProcess).process();
        }
        else if (bytesToShort(OpNumBytes) == 9)
        {
            char bitByBit[1];
            std::vector<char> dynamicVec;
            connection1->getBytes(bitByBit, 1);
            dynamicVec.push_back(bitByBit[0]);
            bitByBit[0] = 'X';
            while (bitByBit[0] != 0)
            {
                connection1->getBytes(bitByBit, 1);
                dynamicVec.push_back(bitByBit[0]);
            }
            bitByBit[0] = 'X';
            while (bitByBit[0] != 0)
            {
                connection1->getBytes(bitByBit, 1);
                dynamicVec.push_back(bitByBit[0]);
            }
            char toProcess[dynamicVec.size()];
            for (size_t i = 0; i < dynamicVec.size(); i++){
                toProcess[i] = dynamicVec.at(i);
            }
            OperationNotification(toProcess).process();
        }
        else if (bytesToShort(OpNumBytes) == 10)
        {
            char OpCodeBytes[2];
            connection1->getBytes(OpCodeBytes, 2);
            if (bytesToShort(OpCodeBytes) == 1 || bytesToShort(OpCodeBytes) == 2 ||
                bytesToShort(OpCodeBytes) == 5 || bytesToShort(OpCodeBytes) == 6)
            {
                if(bytesToShort(OpCodeBytes)==2)
                loggedIn=true;
                OperationAck(OpCodeBytes).process();
            }
            else if (bytesToShort(OpCodeBytes) == 3)
            {
                std::cout << "ACK 3" << std::endl;
                flag = false;
                socketFlag=false;
            }
            else if (bytesToShort(OpCodeBytes) == 8)
            {
                char StatBytes[6];
                connection1->getBytes(StatBytes, 6);
                char toProcess[8];
                toProcess[0] = OpCodeBytes[0];
                toProcess[1] = OpCodeBytes[1];
                for (size_t i = 2; i < 8; i++)
                    toProcess[i] = StatBytes[i - 2];
                OperationAck(toProcess).process();
            }
            else if (bytesToShort(OpCodeBytes) == 7 || bytesToShort(OpCodeBytes) == 4)
            {
                char numOfUsersBytes[2];
                connection1->getBytes(numOfUsersBytes, 2);
                std::vector<char> tmp;
                tmp.push_back(OpCodeBytes[0]);
                tmp.push_back(OpCodeBytes[1]);
                tmp.push_back(numOfUsersBytes[0]);
                tmp.push_back(numOfUsersBytes[1]);
                int zeroCounter = 0;
                char bitByBit[1];
                bitByBit[0] = 'X';
                while (zeroCounter < bytesToShort(numOfUsersBytes))
                {
                    while (bitByBit[0] != 0)
                    {
                        connection1->getBytes(bitByBit, 1);
                        tmp.push_back(bitByBit[0]);
                    }
                    zeroCounter++;
                    bitByBit[0] = 'X';
                }
                char toProcess[tmp.size()];
                for (size_t i = 0; i < tmp.size(); i++)
                    toProcess[i] = tmp.at(i);
                OperationAck(toProcess).process();
            }
        }
    }
    }
};

class Task
{
  private:
    ConnectionHandler *connection1;

  public:
    Task(ConnectionHandler &connection) : connection1(&connection)
    {
    }
    void run()
    {
            while (flag == true)
    {
        std::string input;
        std::getline(std::cin, input);
        std::vector<std::string> vec = split(input, ' ');
        if (vec[0] == "REGISTER")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(1, tmp);
            this->connection1->sendBytes(tmp, 2);
            this->connection1->sendFrameAscii(vec[1], '\0');
            this->connection1->sendFrameAscii(vec[2], '\0');
            free(tmp);
        }
        if (vec[0] == "LOGIN")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(2, tmp);
            this->connection1->sendBytes(tmp, 2);
            this->connection1->sendFrameAscii(vec[1], '\0');
            this->connection1->sendFrameAscii(vec[2], '\0');
            free(tmp);
        }
        if (vec[0] == "LOGOUT")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(3, tmp);
            this->connection1->sendBytes(tmp, 2);
            free(tmp);
            if(loggedIn)
            flag=false;
        }
        if (vec[0] == "FOLLOW")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(4, tmp);
            this->connection1->sendBytes(tmp, 2);
            if(std::stoi(vec[1])==0){
                char* tmp3=(char *)malloc(1);
                tmp3[0]='\0';
               this->connection1->sendBytes(tmp3,1);
            }
            else{
                char *tmp2=(char *)malloc(1);
                tmp2[0]='\x01';
               this->connection1->sendBytes(tmp2,1);
            }
            int numberOfUsers = std::stoi(vec[2]);
            char *ch = (char *)malloc(2);
            shortToBytes(numberOfUsers, ch);
            this->connection1->sendBytes(ch, 2);
            for (size_t i = 3; i < (unsigned)(numberOfUsers + 3); i++)
            {
                this->connection1->sendFrameAscii(vec[i], '\0');
            }
            free(tmp);
            free(ch);
        }
        if (vec[0] == "POST")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(5, tmp);
            this->connection1->sendBytes(tmp, 2);
            std::string combined = "";
            for (size_t i = 1; i < vec.size(); i++)
            {
                combined = combined + vec[i];
                combined = combined + " ";
            }
            this->connection1->sendFrameAscii(combined, '\0');
            free(tmp);
        }
        if (vec[0] == "PM")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(6, tmp);
            this->connection1->sendBytes(tmp, 2);
            this->connection1->sendFrameAscii(vec[1], '\0');
            std::string combined = "";
            for (size_t i = 2; i < vec.size(); i++)
            {
                combined = combined + vec[i];
                combined = combined + " ";
            }
            this->connection1->sendFrameAscii(combined, '\0');
            free(tmp);
        }
        if (vec[0] == "USERLIST")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(7, tmp);
            this->connection1->sendBytes(tmp, 2);
            free(tmp);
        }
        if (vec[0] == "STAT")
        {
            char *tmp = (char *)malloc(2);
            shortToBytes(8, tmp);
            this->connection1->sendBytes(tmp, 2);
            this->connection1->sendFrameAscii(vec[1], '\0');
            free(tmp);
        }

        vec.clear();
    }
    }
};

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl
                  << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect())
    {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
        Task task1(connectionHandler);
        Task2 task2(connectionHandler);
        std::thread th1(&Task::run, &task1);
        std::thread th2(&Task2::run, &task2);
        th1.join();
        th2.join();

    connectionHandler.close();

    return 0;
}
