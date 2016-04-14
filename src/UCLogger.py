import yaml
import logging
import logging.config

path = './conf.yaml'


def logger():
    with open(path, 'r') as stream:
        D = yaml.load(stream)
        logging.config.dictConfig(D)
    logger = logging.getLogger('UCrawL')
    return logger
