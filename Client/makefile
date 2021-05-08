CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: Client
	g++ -o bin/BGSclient bin/connectionHandler.o bin/OperationAck.o bin/OperationNotification.o bin/OperationError.o bin/BGSclient.o $(LDFLAGS) -lpthread

Client: bin/connectionHandler.o bin/OperationAck.o bin/OperationNotification.o  bin/OperationError.o bin/BGSclient.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/OperationAck.o: src/OperationAck.cpp
	g++ $(CFLAGS) -o bin/OperationAck.o src/OperationAck.cpp

bin/OperationNotification.o: src/OperationNotification.cpp
	g++ $(CFLAGS) -o bin/OperationNotification.o src/OperationNotification.cpp
	
bin/OperationError.o: src/OperationError.cpp
	g++ $(CFLAGS) -o bin/OperationError.o src/OperationError.cpp
	
bin/BGSclient.o: src/BGSclient.cpp
	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp -lpthread
	
.PHONY: clean
clean:
	rm -f bin/*
