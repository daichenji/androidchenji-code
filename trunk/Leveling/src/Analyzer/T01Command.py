import re

class T01:
    
    SEND_CMD = "T01 1"
    RECEIVE_PATTERN = re.compile("T01 A (\-?\d+\.?\d*)\;[ ]+(\-?\d+\.?\d*)\;[ ]+(\-?\d+\.?\d*)")
    
    __response = ""
    __x = 0
    __y = 0
    __z = 0
    __temperature = 0
    
    def __init__(self,response):
        self.response = response
        self.__parse(response)
        
    def __parse(self,response):
        m = self.__class__.RECEIVE_PATTERN.match(response)
        
        if m!=None:
            self.__x = float(m.group(1))
            self.__y = float(m.group(2))
            self.__temperature = float(m.group(3))


            
    def getX(self):
        return self.__x
    
    def getY(self):
        return self.__y
    
    def getZ(self):
        return self.__z
    
    def getTemperature(self):
        return self.__temperature