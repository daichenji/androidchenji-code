from socket import gethostbyname,gethostname
import threading
import re

if re.match("192\.168\.2\..+", gethostbyname(gethostname())):
    from Collector.FakePort import Port
elif re.match("172\.30\..+\..+", gethostbyname(gethostname())):
    from Collector.Port import Port
else:
    from Collector.Port import Port


class DataCollector(threading.Thread):
    EOL = Port.EOL_CRLF
    
    PORTNUMBER = 1
    
    def __init__(self,activeCMD,receiveQueue):
        super().__init__()
        self.port = Port(self.PORTNUMBER,printCMD=False)
        self.port.setEOL(self.EOL)
        self.port.setTimeout(0.01)
        self.activeCMD = activeCMD
        self.receiveQueue = receiveQueue


    def run(self):
        self.port.open()

        self.port.send(self.activeCMD)

        dataarray = bytearray()
   
        while True:                
            c = self.port.read(1)

            if c:   
                dataarray += c

                if dataarray[-len(self.port.eol):] == self.port.eol:
                    try:
                        self.receiveQueue.put(dataarray.decode(encoding='utf_8'),False)
                    except:
                        pass

                    dataarray = bytearray()
                    #self.port.send(self.activeCMD)