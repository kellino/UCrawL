import inspect
import logging
import os

debugFlag = True
# GREEN = '\033[92m'
GREEN = '#68b782'
PINK = '#68b782'
BLUE = '#68b782'
RED = '#68b782'
YELLOW = '#68b782'
NOCOLOUR = '#68b782'
# PINK = '\033[95m'
# BLUE = '\033[94m'
# RED = '\033[91m'
# YELLOW = '\033[93m'
# NOCOLOR = '\033[0m'

colour = [GREEN, PINK, BLUE, RED, YELLOW, NOCOLOUR]


def init(path, logName):
    # basePath = os.path.dirname(sys.argv[0])
    basePath = "/home/david/Programming/Python/
    if basePath:
        basePath = basePath + "/"
    path = basePath + path
    if not os.path.exists(path):
        os.makedirs(path)
    logging.basicConfig(filename=path+logName, format='%(asctime)s %(levelname)s:%(message)s', level=logging.DEBUG)


def debugFlag(flag):
    debugFlag = flag
    if debugFlag:
        logging.disable(logging.NOTSET)
    else:
        logging.disable(logging.DEBUG)


def log(level, message):
    # message formatting
    if level == logging.DEBUG:
        func = inspect.currentframe().f_back.f_code
        fileName = ''.join(func.co_filename.split('/')[-1])
        line = func.co_firstlineno
        message = "[" + str(func.co_name) + " - " + str(fileName) + ", " + str(line) + "] " + message

    if level == logging.CRITICAL:
        message = "\n\n ************************\n" + message

    # printing to logs
    if debugFlag and level == logging.DEBUG:
        print(message)
    elif level is not logging.DEBUG:
        print(message)

    for c in colour:
        message = message.replace(c, "")

    logging.log(level, message)


def formatBrackets(message):
    return "[" + str(message) + "]"
