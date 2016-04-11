import logging
import logging.config

logging.config.fileConfig('/home/david/Programming/Python/UCrawL/src/utils/logging.conf')

logger = logging.getLogger('UCrawL')


logger.debug("debug meesage")
logger.info('info message')
logger.warn('warn message')
logger.error('error message')
logger.critical('critical message')
