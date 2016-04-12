import yaml
import logging
import logging.config

path = '/home/david/Programming/Python/UCrawL/src/utils/conf.yaml'


def load_logger():
    with open(path, 'r') as stream:
        D = yaml.load(stream)
        logging.config.dictConfig(D)
    logger = logging.getLogger('UCrawL')
    return logger
