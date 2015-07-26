
import re
import time
import struct




    
class Port():
    PrintCMD = None
    EOL_CRLF, EOL_CR, EOL_LF = (b'\r\n', b'\r', b'\n')
    EOLS  = (EOL_CRLF, EOL_CR, EOL_LF)
    EOL_CRLF_STR, EOL_CR_STR, EOL_LF_STR = ('\\r\\n', '\\r', '\\n')
    EOLS_STR =  ( EOL_CRLF_STR, EOL_CR_STR, EOL_LF_STR)
    
    def __init__(self,tPort=1,eol=EOL_CRLF,printCMD = True):        
        super().__init__()
        self.ID = tPort
        self.port = "COM{0}".format(tPort)
        self.eol = eol
        self.PrintCMD = printCMD
        
        self.RESPOND_PATTERN_S_UNSTABLE = "S D {0}.00 g\r\n"
        self.RESPOND_PATTERN_S_STABLE = "S S {0}.00 g\r\n"
        self.RESPOND_PATTERN_T = "T S {0}.00 g\r\n"
        self.RESPOND_PATTERN_Z = "Z A\r\n"
        self.RESPOND_PATTERN_ERROR_SYNTAX = "ES\r\n"
        
        self.response = bytearray()
        self.receive = bytearray()
        
        self.responsecount = 0
        self.responselen = len(self.response)
        
        self.COMMAND_T = "T"
        self.COMMAND_Z = "Z"
        self.COMMAND_SI = "SI"
        
    def open(self):
        pass
    
    
    def close(self):
        pass
    
    
    def __getResponse(self):
        
        received_command = self.receive.decode("ascii")
        self.receive = bytearray()
        
        if received_command == self.COMMAND_SI:
            self.response = bytearray(self.RESPOND_PATTERN_S_STABLE.format(self.responsecount).encode("ascii"))
        elif received_command == self.COMMAND_T:
            time.sleep(5)
            self.response = bytearray(self.RESPOND_PATTERN_T.format(self.responsecount).encode("ascii"))
        elif received_command == self.COMMAND_Z:
            time.sleep(10)
            self.response = bytearray(self.RESPOND_PATTERN_Z.encode("ascii"))
        else:
            self.response = bytearray(self.RESPOND_PATTERN_ERROR_SYNTAX.encode("ascii"))
            self.responsecount -= 1
            
        self.responsecount+=1
                    
    def write(self,c):
        if c == b'\r':
            return
        
        if c == b'\n':
            
            time.sleep(0.2)
            self.__getResponse()
            return
        

        self.receive += c
        
            
            
    

    
    def read(self,length=1):
        

        if len(self.response)<= 0:
            time.sleep(self.timeout)
            return None
        #time.sleep(0.1)
        c = self.response.pop(0)
        return struct.pack("b",c)
  
    
    def setTimeout(self,timeout):
        self.timeout = timeout

    def setEOL(self, eol):
        """Change EndOfLine setting."""
        if eol not in self.EOLS: raise ValueError("Not a valid EndOfLine: %r" % (eol,))
        self.eol = eol

    


class _Global:
    CurrentPort = None
    PortList = None

class __PortConfig:    
    baudrate = 9600
    bytesize = 8
    parity = 'N'
    stopbits = 1
    timeout = None
    xonxoff = False
    rtscts = False
    dsrdtr = False    