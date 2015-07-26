

from socket import socket,AF_INET,SOCK_STREAM
import time




HOST = '127.0.0.1'
PORT = 55810
BUFSIZ = 1024
ADDR = (HOST, PORT)


tcpCliSock = socket(AF_INET, SOCK_STREAM)
tcpCliSock.connect(ADDR)

olddata = ""

while True:
    tcpCliSock.send("SI\r\n".encode("ascii"))
    data1 = []
    while True:
        c = tcpCliSock.recv(1)
        data1.append(c)
        if c == "\n".encode("ascii"):
            break
        
    time.sleep(1)
    print(data1)

tcpCliSock.close()