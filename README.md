# Reactor-Social-Network
Simple Social Network with Client/Server Communication  

Description: Simple social network with modern functions including: Follow, Post, Private Message and more. Built using Microservices architecture with focus on concurrency & OOP 
and thread communication in both Java 8 and CPP.  Two communication patters are included:  
1) Reactor pattern: Where the server has a fixed number of threads to do all the work (reading/decoding/encoding/sending/...)  
2) Thread-Per-Client: In TPC, Every connection to the server has a thread of it's own. PS: In gaming it would make since to use TPC, In social networking Reactor is preferred

Input: Json File (included)
Output: 4 serialized objects that represent the store's status at the end of the run.  

NOTE: I still have the assignment's PDF, But bcz of copyright issues I can't publish it.
