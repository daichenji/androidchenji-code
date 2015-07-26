
from socket import socket,AF_INET,SOCK_STREAM,gethostbyname,gethostname
import threading
import re
#import time

if re.match("192\.168\.2\..+", gethostbyname(gethostname())):
    from FakePort import Port
elif re.match("172\.30\..+\..+", gethostbyname(gethostname())):
    from Port import Port
else:
    from Port import Port


class WGServer(threading.Thread):
    HOST = ""
    PORT = 55810
    BUFSIZ = 1024
    ADDR = (HOST, PORT)
    PORTNUMBER = 1
    
    
    EOL = Port.EOL_CRLF
            


    def __init__(self):
        super().__init__()
        print ("Server Address:",gethostbyname(gethostname()),", Port:",self.PORT,"\n\n")

        self.tcpSerSock = socket(AF_INET, SOCK_STREAM)
        self.tcpSerSock.bind(self.ADDR)        
        self.tcpSerSock.listen(5)

            
    def run(self):
        self.port = Port(self.PORTNUMBER,printCMD=False)
        self.port.setEOL(self.EOL)
        self.port.setTimeout(0.01)
        self.port.open()
        
        
        try:
            while True:
         
                print ('waiting for connection...')
                acceptedSocket, addr = self.tcpSerSock.accept()
                print ('...connected from:', addr)
                serialToSocketThread = _SerialToSocketThread(self.port, acceptedSocket)
                socketToSerialThread = _SocketToSerialThread(self.port, acceptedSocket)
                
                serialToSocketThread.start()
                socketToSerialThread.start()
                
                # block the main thread, until the 2 children thread exiting
                serialToSocketThread.join()
                socketToSerialThread.join()
                
                print("Socket abort!\n")
                    
        except ConnectionResetError:
            pass



        self.tcpSerSock.close()
        self.port.close()

class _SerialToSocketThread(threading.Thread):
    """
    Get the data from serial, send to socket
    """
    def __init__(self,serialPort,acceptedSocketToClient):
        super().__init__()
        self.port = serialPort
        self.socket = acceptedSocketToClient
        
    def run(self):
        try:
            dataarray = bytearray() # dataarray is only for testing
            while True:                
                c = self.port.read(1)                
                if c:
                    
                    dataarray += c

                    if dataarray[-len(self.port.eol):] == self.port.eol:               
                        #print(">>>",bytes(dataarray))
                        dataarray = bytearray()  
                    if not self.socket._closed:             
                        self.socket.send(c)
                
                if self.socket._closed:
                    break
            
            if not self.socket._closed:
                self.socket.close()
            
        except ConnectionAbortedError:

            self.socket.close()
        except ConnectionResetError:
            print("error")
#        except OSError as e:
#            print("_SocketToSerialThread",e.__class__.__name__)

                
        
class _SocketToSerialThread(threading.Thread):
    """
    Receive the data from socket, send to serial port
    """
    def __init__(self,serialPort, acceptedSocketToClient):
        super().__init__()
        self.port = serialPort
        self.socket = acceptedSocketToClient
        
    def run(self):
        try:
            dataarray = bytearray() # dataarray is only for testing
            while True:
                
                if not self.socket._closed:

                    c = self.socket.recv(1)
           
                    if c:
                        dataarray += c

                        if dataarray[-len(self.port.eol):] == self.port.eol:               
                            #print("<<<",bytes(dataarray))
                            dataarray = bytearray()                        
                        self.port.write(c)
                    else:
                        break # socked is disconnected
                else:
                    break

            if not self.socket._closed:
                self.socket.close()
            
        except ConnectionAbortedError:
            self.socket.close()
        except ConnectionResetError:
            print("error")
#        except OSError as e:
#            print("_SocketToSerialThread",e.__class__.__name__)
                
        
        
WGServer().start()