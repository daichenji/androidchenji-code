
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
        
        self.RESPOND_PATTERN_ERROR_SYNTAX = "ES\r\n"
        self.RESPOND_PATTERN_T01 = [
                                    "T01 A 2.9;  11.1;  19.58\r\n",
                                    "T01 A 3.9;  11.2;  19.46\r\n",
                                    "T01 A -21.04;  27.78;  19.30\r\n",
                                    "T01 A -20.99;  27.78;  19.12\r\n",
                                    "T01 A -20.78;  27.92;  19.44\r\n",
                                    "T01 A -20.94;  28.03;  19.66\r\n",
                                    "T01 A -20.81;  27.86;  19.48\r\n",
                                    "T01 A -20.91;  27.88;  19.30\r\n",
                                    "T01 A -20.94;  27.88;  19.25\r\n",
                                    "T01 A -20.99;  27.82;  19.23\r\n",
                                    "T01 A -20.84;  27.87;  19.23\r\n",
                                    "T01 A -20.75;  27.84;  19.22\r\n",
                                    ]
        
        
        self.response = bytearray()
        self.receive = bytearray()
        
        self.responsecount = 0
        self.responselen = len(self.response)
        
        self.COMMAND_T01 = "T01 1"

        
    def open(self):
        pass
    
    
    def close(self):
        pass
    
    
    def __getResponse(self):
        
        received_command = self.receive.decode("ascii")
        self.receive = bytearray()
        
        if received_command == self.COMMAND_T01:
            self.response = bytearray(self.RESPOND_PATTERN_T01[self.responsecount].encode("ascii"))

        else:
            self.response = bytearray(self.RESPOND_PATTERN_ERROR_SYNTAX.encode("ascii"))
            self.responsecount -= 1
            
        self.responsecount = (1 + self.responsecount)%len(self.RESPOND_PATTERN_T01)
                    
    def write(self,c_sequence):
        
        for _c in c_sequence:
            c_item = bytes([_c,])
            if c_item == b'\r':
                pass
            
            elif c_item == b'\n':
                
                time.sleep(0.2)
                self.__getResponse()

            else:    
                self.receive += c_item

        return
        

        
        
    def send(self,cmd):
        """Send command to Serial port"""
        toPrint = cmd + self.EOLS_STR[self.EOLS.index(self.eol)]
        if self.PrintCMD:
            print("\t<<<",toPrint)
           
        cmd += self.PortDecode(self.eol)        

      
        command = self.PortEncode(cmd)
        return self.write(bytes(command))
            
    
    def PortDecode(self,text): 
        """Decode from ASCII(8bits,256) to unicode"""
        line = ""
        for b in text:
            line += chr(b)              
        return line
    
    def PortEncode(self,text): 
        """Encode from unicode to ASCII(8bits,256)"""
               
        line = bytearray()
        for c in text: 
            #cHex = str(hex(ord(c)))[2:]
            cHex = '%x' % ord(c) 
            if len(cHex)<=1:
                cHex = "0"+cHex
            line += bytes.fromhex(cHex)        
        return line
    
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