
from multiprocessing import Queue
from Analyzer import LevelAnalyzer
from Server import LevelingServer
resultQueue = Queue()

LevelingServer(resultQueue).start()
LevelAnalyzer(resultQueue).start()