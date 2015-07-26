import serial
import re




    
class Port(serial.Serial):
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
        
    def open(self):

        if not self.isOpen():
            super().open()            
        _Global.CurrentPort = self        
    
    
    def close(self):
        if (not self.isOpen()):
            return
        super().close()
        for p in _Global.PortList:
            if p != None and p.isOpen():
                _Global.CurrentPort = p   
        else:
            _Global.CurrentPort = None
                
    def send(self,cmd):
        """Send command to Serial port"""
        toPrint = cmd + self.EOLS_STR[self.EOLS.index(self.eol)]
        if self.PrintCMD:
            print("\t<<<",toPrint)
           
        cmd += self.PortDecode(self.eol)        

      
        command = self.PortEncode(cmd)
        return self.write(bytes(command))
    
    def receive(self, timeout=None):
        """Receive command from Serial Port"""
        if isinstance(timeout, int):
            self.setTimeout(timeout)
            
        rcv = self.readline()
        receive =self.PortDecode(rcv)        

        receive = receive[:-len(self.eol)]
        
        toPrint = receive + Port.EOLS_STR[Port.EOLS.index(self.eol)]
        if self.PrintCMD:
            print("\t>>>",toPrint) 
           
        return receive
    
    def receiveMatch(self,matchText):       
        textRcv = self.receive()
        if re.search(matchText,textRcv) == None:
            raise ValueError("Receive not match, {0}, {1}".format(matchText,textRcv))
        else:
            return True        
    
    def readline(self, size=None):
        """read a line which is terminated with end-of-line (eol) character 
        or until timeout."""
        leneol = len(self.eol)
        line = bytearray()
        while True:
            c = self.read(1)
            if c:
                line += c
                if line[-leneol:] == self.eol:
                    break
                if size is not None and len(line) >= size:
                    break
            else:
                break 
        return bytes(line)
    
    def setTimeout(self,timeout):
        self.timeout = timeout

    def setEOL(self, eol):
        """Change EndOfLine setting."""
        if eol not in self.EOLS: raise ValueError("Not a valid EndOfLine: %r" % (eol,))
        self.eol = eol
        
    def getEOL(self):
        """Get EndOfLine setting."""
        return self.eol
    
    def getID(self):
        """Get the ID of the port"""
        return self.ID
    
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
    
    def PortDecode(self,text): 
        """Decode from ASCII(8bits,256) to unicode"""
        line = ""
        for b in text:
            line += chr(b)              
        return line
    

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