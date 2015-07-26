import math
from Analyzer.Parameters import PARA




       
class LocationCalculator:
    E = 0.0000001
    
    
    
    def __init__(self):   
        
        self.adjust = False
        self.Ax0 = PARA.AX0
        self.Ay0 = PARA.AY0
        self.Az0 = 0
        self.temperature0 = 25
        
        self.savedX = self.Ax0
        self.savedY = self.Ay0
        
        self.sensitivity = (PARA.DISTANCE_DEGREE_RATE * 60 * 180 / math.pi) / 1000
        
        

        
    def setDefaultAxisValue(self,x0,y0,temperature0):
        self.Ax0 = x0
        self.Ay0 = y0        
        self.temperature0 = temperature0

    
    def __updateLocation(self,currentAx,currentAy):
        currentX = self.__getDiffFromOriginal(currentAx, self.Ax0)
        currentY = self.__getDiffFromOriginal(currentAy, self.Ay0)
        
        diff_factor = 0.2
        
        if currentX-self.savedX > diff_factor:
            self.savedX = currentX - diff_factor
        elif currentX-self.savedX < -diff_factor:
            self.savedX = currentX + diff_factor
        else:
            self.savedX = currentX
        
        if currentY-self.savedY > diff_factor:
            self.savedY = currentY - diff_factor
        elif currentY-self.savedY < -diff_factor:
            self.savedY = currentY + diff_factor
        else:
            self.savedY = currentY
            
#        self.savedX = currentX
#        self.savedY = currentY
            
    def __getXYLocation(self):       
        return self.savedX,self.savedY
    
    def __getDiffFromOriginal(self,currentV, originalV):
        dv = self.sensitivity *(currentV-originalV)
        return dv
    
    def __calculateRadiueAndAngel(self,px,py):
        Radius = math.sqrt(px*px + py*py)
        if Radius > LocationCalculator.E:
            if py > LocationCalculator.E:
                
                angel = math.acos(px/Radius)
                
                
            elif py < -LocationCalculator.E:
                angel = 2*math.pi - math.acos(px/Radius)
                
                
                
            else:
                if py > LocationCalculator.E:
                    angel = 0
                elif py < -LocationCalculator.E:
                    angel = math.pi
                else:
                    angel = 0
        else:
            Radius = 0
            angel = 0
    
        
        return Radius,angel  # Radius, Angel
        
    def getRadiueAndAngel(self,currentAx,currentAy):
        """
        update data for data processing and calculate
        """
        self.__updateLocation(currentAx,currentAy)
        return self.__calculateRadiueAndAngel(self.savedX, self.savedY)
            
        

            
            
    