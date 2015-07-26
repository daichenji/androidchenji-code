from multiprocessing import Queue
from Collector import DataCollector
from Analyzer.Calculator import LocationCalculator
from Analyzer.T01Command import T01
import threading

class LevelAnalyzer():
    
    def __init__(self,resultQueue):
        #super().__init__()
        self.resultQueue = resultQueue
        self.receiveQueue = Queue()
        self.dataCollector = DataCollector(T01.SEND_CMD,self.receiveQueue)
        self.calculator = LocationCalculator()

        self.adjust = False

    def start(self):
        self.dataCollector.start() # start the thread to get data

        while True:
            t01receive = self.receiveQueue.get(True)
            
            t01 = T01(t01receive)
            
            
            if self.adjust:
                self.calculator.setDefaultAxisValue(t01.getX(), t01.getY(), t01.getTemperature())
                self.adjust = False    
            currentAx = t01.getX()
            currentAy = t01.getY()

            radium,angel = self.calculator.getRadiueAndAngel(currentAx,currentAy)

            self.resultQueue.put(CycleData(radium,angel),False)

            
    def adjustment(self):
        self.adjust = True
        
class CycleData:
    def __init__(self,radium,angel):
        self.radium = radium
        self.angel = angel
        
    def getRadium(self):
        return self.radium
    
    def getAngel(self):
        return self.angel
    
    def getString(self):
        return str(self.radium)+" "+str(self.angel)+"\r\n"
    
