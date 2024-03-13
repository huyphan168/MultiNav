
from src.common import BaseNav
import jedi

class PyNav(BaseNav):
    def __init__(self, root, max_workers=16):
        super().__init__(root, max_workers)
        multinave = jedi.Project(root, environment_path=get_env_path())
        
    def execute(self, file, line, column):
        return self.multinav.execute(file, line, column)