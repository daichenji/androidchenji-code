from socket import socket,AF_INET,SOCK_STREAM,gethostbyname,gethostname
import threading
import re

#import time




class LevelingServer(threading.Thread):
    HOST = ""
    PORT = 55810
    BUFSIZ = 1024
    ADDR = (HOST, PORT)
    PORTNUMBER = 1
   


    def __init__(self, resultQueue):
        super().__init__()
        print ("Server Address:",gethostbyname(gethostname()),", Port:",self.PORT,"\n\n")

        self.tcpSerSock = socket(AF_INET, SOCK_STREAM)
        self.tcpSerSock.bind(self.ADDR)        
        self.tcpSerSock.listen(5)
        self.resultQueue = resultQueue
            
    def run(self):
        from Analyzer import LevelAnalyzer

        
        
        
        try:
            while True:
         
                print ('waiting for connection...')
                acceptedSocket, addr = self.tcpSerSock.accept()
                print ('...connected from:', addr)
                
                try:
                    self.resultQueue.get(False)
                except:
                    pass
                
                try:
                
                    while True:
                        cycleData = self.resultQueue.get(True)
                        
                        acceptedSocket.send(cycleData.getString().encode("utf-8"))
                        #print(cycleData.getString())
                    
                    
                
                except:
                    pass
                print("Socket abort!\n")
                
        
        except ConnectionResetError:
            pass



        self.tcpSerSock.close()

